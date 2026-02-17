package com.ytgld.malstone.items.init;

import com.sammy.malum.common.item.curiosities.curios.runes.AbstractRuneCurioItem;
import com.sammy.malum.core.systems.spirit.MalumSpiritType;
import com.sammy.malum.registry.common.SoundRegistry;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.Light;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.ArrayList;
import java.util.List;

public class Runes extends AbstractRuneCurioItem {
    public Runes(Properties builder, MalumSpiritType spiritType) {
        super(builder, spiritType);
    }
    @Override
    public Component getName(ItemStack p_41458_) {
        Component component =super.getName(p_41458_);
        return component.copy().setStyle(Style.EMPTY.withColor(color()));
    }

    @Override
    public @NotNull ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new  ICurio.SoundInfo(SoundRegistry.RUNE_TRINKET_EQUIP.get(),1,1);
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return !Handler.hascurio(slotContext.entity(), this);
    }
    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        List<Component> components = new ArrayList<>();
        for (Component component : tooltips) {
            MutableComponent mutableComponent = component.copy();
            mutableComponent.setStyle(Style.EMPTY.withColor(Light.ARGB.color(255, 147, 121, 224)));
            components.add((mutableComponent));
        }
        return components;
    }
    public int color(){
        return Light.ARGB.color(255, 147, 121, 224);
    }
}
