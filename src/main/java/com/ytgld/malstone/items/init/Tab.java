package com.ytgld.malstone.items.init;

import com.ytgld.malstone.Malstone;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class Tab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Malstone.MODID);
    public static final RegistryObject<CreativeModeTab> TAB_REGISTRY_OBJECT = TABS.register("malstone",()-> CreativeModeTab.builder()
            .icon(()->new ItemStack(ItemRegs.HugeSouls_.get()))
            .title(Component.translatable("itemGroup.malstone"))
            .displayItems((a,b)->{
                b.accept(ItemRegs.HugeSouls_.get());
            })
            .build()
    );
}
