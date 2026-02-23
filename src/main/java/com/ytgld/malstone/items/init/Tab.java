package com.ytgld.malstone.items.init;

import com.ytgld.malstone.Malstone;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Tab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Malstone.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_REGISTRY_OBJECT = TABS.register("malstone",()-> CreativeModeTab.builder()
            .icon(()->new ItemStack(ItemRegs.BreakingTheLife_.get()))
            .title(Component.translatable("itemGroup.malstone"))
            .displayItems((a,b)->{
                b.accept(ItemRegs.HugeSouls_.get());
                b.accept(ItemRegs.BreakingTheLife_.get());
                b.accept(ItemRegs.WhiteArrowBlade_.get());
                b.accept(ItemRegs.RingOfAuthority_.get());
                b.accept(ItemRegs.SoulSteelComponents_.get());
                b.accept(ItemRegs.EternalFallenSoul_.get());
                b.accept(ItemRegs.BladeOath_.get());
                b.accept(ItemRegs.Martyrdom_.get());
                b.accept(ItemRegs.Hungrier_.get());
                b.accept(ItemRegs.SoulDevice_.get());
                b.accept(ItemRegs.BreakingTheWeapon_.get());
//                b.accept(ItemRegs.KillTheGods_.get());
            })
            .build()
    );
}
