package com.ytgld.malstone;

import com.mojang.logging.LogUtils;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Tab;
import com.ytgld.malstone.items.white.BreakingTheWeapon;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

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
        modEventBus.addListener(this::registerPayloadHandler);
    }
    private void registerPayloadHandler(final RegisterPayloadHandlersEvent evt) {
        BreakingTheWeapon.BreakingTheWeaponNetworkHandler.register(evt.registrar("1.0"));
    }
}
