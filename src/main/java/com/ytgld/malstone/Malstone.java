package com.ytgld.malstone;

import com.mojang.logging.LogUtils;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.effects.Effects;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Tab;
import com.ytgld.malstone.items.white.BreakingTheWeapon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Malstone.MODID)
public class Malstone
{
    public static final String MODID = "malstone";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static int clientTime = 0;

    public Malstone(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(new MyEvent());
        Tab.TABS.register(modEventBus);
        AttReg.REGISTRY.register(modEventBus);
        ItemRegs.REGISTER.register(modEventBus);
        Effects.REGISTER.register(modEventBus);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    private void setup(FMLCommonSetupEvent evt) {
        BreakingTheWeapon.register();
    }
    @Mod.EventBusSubscriber(modid = Malstone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public void clientTickEvent(TickEvent.ClientTickEvent event) {
            clientTime++;
        }
    }

}
