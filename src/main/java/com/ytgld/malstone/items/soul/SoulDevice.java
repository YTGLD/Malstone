package com.ytgld.malstone.items.soul;

import com.google.common.collect.Multimap;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.SoulSteel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.helpers.ItemHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 魂魄装置
 * <p>
 * 拾取精魂时有概率使数量翻倍
 */
public class SoulDevice extends SoulSteel {
    public SoulDevice(Properties properties) {
        super(properties);
    }
    public static void doubleSpirit(LivingEntity collector, ItemStack stack) {
        if (Handler.hascurio(collector, ItemRegs.SoulDevice_.get())) {
            if (Mth.nextInt(RandomSource.create(), 0, 100) < 50) {
                ItemHelper.giveItemToEntity(collector, stack);
            }
        }
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = super.getAttributeModifiers(slotContext, id, stack);
        CuriosApi.addSlotModifier(modifierMultimap,"charm",id,1, AttributeModifier.Operation.ADD_VALUE);
        return modifierMultimap;
    }

    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.translatable("item.malstone.soul_device.text.1").setStyle(Style.EMPTY.withColor(color())));
        return new MalstoneText(stack,tooltipComponents);
    }
}
