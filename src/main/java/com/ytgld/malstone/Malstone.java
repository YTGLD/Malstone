package com.ytgld.malstone;

import com.mojang.logging.LogUtils;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.client.entity.SpiritRenderer;
import com.ytgld.malstone.entity.Entitys;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Tab;
import com.ytgld.malstone.items.white.BreakingTheWeapon;
import com.ytgld.malstone.magic.MalstoneSpirits;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

import java.util.function.Function;

@Mod(Malstone.MODID)
public class Malstone
{
    public static final String MODID = "malstone";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Malstone(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(new MyEvent());
        Tab.TABS.register(modEventBus);
        AttReg.REGISTRY.register(modEventBus);
        ItemRegs.REGISTER.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        Entitys.REGISTER.register(modEventBus);
        MalstoneSpirits.SPIRIT_TYPES.register(modEventBus);
        modEventBus.addListener(this::registerPayloadHandler);
    }
    private void registerPayloadHandler(final RegisterPayloadHandlersEvent evt) {
        BreakingTheWeapon.BreakingTheWeaponNetworkHandler.register(evt.registrar("1.0"));
    }
    public static ResourceLocation malstonePath(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void renderSpirit(EntityRenderersEvent.RegisterRenderers event){
            event.registerEntityRenderer(Entitys.BLOOD_SPIRIT.get(), SpiritRenderer::new);
        }
    }


}
