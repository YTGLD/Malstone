package com.ytgld.malstone;

import com.mojang.logging.LogUtils;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Tab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Malstone.MODID)
public class Malstone
{
    public static final String MODID = "malstone";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Malstone(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new MyEvent());
        Tab.TABS.register(modEventBus);
        AttReg.REGISTRY.register(modEventBus);
        ItemRegs.REGISTER.register(modEventBus);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
