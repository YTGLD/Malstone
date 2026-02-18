package com.ytgld.malstone.items.init;

import com.sammy.malum.common.item.curiosities.curios.runes.AbstractRuneCurioItem;
import com.sammy.malum.core.systems.registry.SpiritHolder;
import com.sammy.malum.core.systems.spirit.type.SpiritArcanaType;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.Light;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;

public class Runes extends AbstractRuneCurioItem {

    public Runes(Properties builder, SpiritHolder<SpiritArcanaType> spirit, MalumTrinketType type) {
        super(builder, spirit, type);
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        Component component =super.getName(p_41458_);
        return component.copy().setStyle(Style.EMPTY.withColor(color()));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return !Handler.hascurio(slotContext.entity(), this);
    }
    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, TooltipContext context, ItemStack stack) {
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
