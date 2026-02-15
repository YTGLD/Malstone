package com.ytgld.malstone.items.init;

import com.sammy.malum.registry.common.SoundRegistry;
import com.ytgld.malstone.Light;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;

public class WhiteArrow extends Item implements ICurioItem {
    public WhiteArrow(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        Component component =super.getName(p_41458_);
        return component.copy().setStyle(Style.EMPTY.withColor(color()));
    }

    @Override
    public @NotNull ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new  ICurio.SoundInfo(SoundRegistry.VOID_TRINKET_EQUIP.get(),1,1);
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        List<Component> components = new ArrayList<>();
        for (Component component : tooltips) {
            MutableComponent mutableComponent = component.copy();
            mutableComponent.setStyle(Style.EMPTY.withColor(Light.ARGB.color(255, 255, 100, 255)));
            components.add((mutableComponent));
        }
        return components;
    }
    public int color(){
        return Light.ARGB.color(255, 250, 190, 220);
    }
}
