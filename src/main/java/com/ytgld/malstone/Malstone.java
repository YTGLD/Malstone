package com.ytgld.malstone;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.logging.LogUtils;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.client.MRender;
import com.ytgld.malstone.client.ShieldRenderHandler;
import com.ytgld.malstone.client.entity.SpiritRenderer;
import com.ytgld.malstone.effects.Effects;
import com.ytgld.malstone.entity.Entitys;
import com.ytgld.malstone.event.Keys;
import com.ytgld.malstone.event.MyEvent;
import com.ytgld.malstone.event.key.ClientEvent;
import com.ytgld.malstone.event.key.UseSkillHandler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Tab;
import com.ytgld.malstone.items.white.BreakingTheWeapon;
import com.ytgld.malstone.magic.DataReg;
import com.ytgld.malstone.magic.MalstoneSpirits;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

import java.io.IOException;

@Mod(Malstone.MODID)
public class Malstone
{
    public static final String MODID = "malstone";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static int clientTime = 0;
    public Malstone(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(new MyEvent());
        NeoForge.EVENT_BUS.register(new ClientEvent());
        Tab.TABS.register(modEventBus);
        AttReg.REGISTRY.register(modEventBus);
        AttReg.ATTACHMENT_TYPES.register(modEventBus);
        ItemRegs.REGISTER.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        Entitys.REGISTER.register(modEventBus);
        Effects.REGISTER.register(modEventBus);
        DataReg.REGISTRY.register(modEventBus);
        MalstoneSpirits.SPIRIT_TYPES.register(modEventBus);
        modEventBus.addListener(this::registerPayloadHandler);
    }
    private void registerPayloadHandler(final RegisterPayloadHandlersEvent evt) {
        BreakingTheWeapon.BreakingTheWeaponNetworkHandler.register(evt.registrar("1.0"));
        UseSkillHandler.register(evt.registrar("1.0"));
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void renderSpirit(EntityRenderersEvent.RegisterRenderers event){
            event.registerEntityRenderer(Entitys.BLOOD_SPIRIT.get(), SpiritRenderer::new);
        }
        @SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
            event.register(Keys.KEY_MAPPING_LAZY_R);
        }
        @SubscribeEvent
        public static void RegisterShadersEvent(RegisterShadersEvent event) {
            try {
                event.registerShader(new ShaderInstance(event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(MODID,"live"),
                        DefaultVertexFormat.POSITION_TEX_COLOR), MRender::set_liveShaderInstance);
            }catch (IOException exception){
            }
        }
        @SubscribeEvent
        public static void registerOverlays(RegisterGuiLayersEvent event) {
            event.registerAbove(VanillaGuiLayers.AIR_LEVEL, ResourceLocation.fromNamespaceAndPath(MODID,"decay_shield"),
                    ShieldRenderHandler::renderShield);
        }
        @SubscribeEvent
        public static void clientTickEvent(ClientTickEvent.Pre event) {
            clientTime++;
            ShieldRenderHandler.tick(event);
        }
    }


}
