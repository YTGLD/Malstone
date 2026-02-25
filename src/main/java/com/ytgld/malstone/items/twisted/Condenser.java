package com.ytgld.malstone.items.twisted;

import com.google.common.collect.Multimap;
import com.sammy.malum.common.entity.spirit.SpiritItemEntity;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Twisted;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

/**
 * 瞬时凝聚器
 * <p>
 * 生物产生的精魂可瞬间到达你的位置
 */
public class Condenser extends Twisted {
    public Condenser(Properties properties) {
        super(properties);
    }

    public static void tpPlayer(LivingEntity player , SpiritItemEntity spiritItemEntity){
        if (Handler.hascurio(player, ItemRegs.Condenser_.get())) {
            if (spiritItemEntity.tickCount == 41) {
                spiritItemEntity.setPos(player.position());
            }
        }
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = super.getAttributeModifiers(slotContext, id, stack);
        CuriosApi.addSlotModifier(modifierMultimap,"belt",id,1, AttributeModifier.Operation.ADD_VALUE);
        return modifierMultimap;
    }
    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.translatable("item.malstone.condenser.text.1").setStyle(Style.EMPTY.withColor(color())));
        return new MalstoneText(stack,tooltipComponents);
    }
}
