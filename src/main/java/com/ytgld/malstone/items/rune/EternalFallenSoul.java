package com.ytgld.malstone.items.rune;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sammy.malum.registry.common.MalumAttributes;
import com.sammy.malum.registry.common.magic.MalumSpiritTypes;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.Runes;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

/**
 * 永堕灵魂符文
 * <p>
 * 恢复灵魂护盾时会短暂叠加你的部分属性
 * <p>
 * 最多叠加到额外%d%%
 */
public class EternalFallenSoul extends Runes {
    private static final String namePath = "EternalDamageAndArmor";

    public EternalFallenSoul(Properties builder) {
        super(builder, MalumSpiritTypes.UMBRAL_SPIRIT, MalumTrinketType.RUNE);
    }
    @Override
    public void addAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> map, SlotContext slotContext, ItemStack stack) {
        this.addAttributeModifier(map, MalumAttributes.SOUL_WARD_CAPACITY,
                new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                        4, AttributeModifier.Operation.ADD_VALUE));

        this.addAttributeModifier(map, AttReg.ChaosErosion,
                new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                        0.2, AttributeModifier.Operation.ADD_VALUE));
    }

    public static void healSOUL_WARD_CAP(LivingEntity player){
        CompoundTag compoundTag = player.getPersistentData();
        if (!player.level().isClientSide()) {
            if (compoundTag.getFloat(namePath) < maxDamageAndArmor(player)) {
                compoundTag.putFloat(namePath, compoundTag.getFloat(namePath) + speed(player));
            }
        }
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        LivingEntity entity = slotContext.entity();
        if (entity instanceof Player player) {
            if (!player.level().isClientSide()) {
                player.getAttributes().addTransientAttributeModifiers(doAttribute(player));
                if (player.tickCount % 20 == 1) {
                    CompoundTag compoundTag = player.getPersistentData();
                    if (compoundTag.getFloat(namePath) > 0) {
                        compoundTag.putFloat(namePath, compoundTag.getFloat(namePath) - 0.005f);
                    }
                }
            }
        }
    }
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide()) {
            entity.getAttributes().removeAttributeModifiers(doAttribute(entity));
        }
    }



    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity player) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();

        float add = 0;
        if (!player.level().isClientSide()) {
            CompoundTag compoundTag = player.getPersistentData();
            add = compoundTag.getFloat(namePath);
        }
        if (add > maxDamageAndArmor(player)) {
            add = maxDamageAndArmor(player);
        }


        modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        modifiers.put(Attributes.ARMOR, new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        return modifiers;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.malstone.eternal_fallen_soul.text").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.eternal_fallen_soul.text.1",maxDamageAndArmor(null)*100f).setStyle(Style.EMPTY.withColor(color())));
    }

    public  static float maxDamageAndArmor(LivingEntity living){
        return Config.getMaxEternalFallenSoul().get().floatValue() * Handler.getArcaneHarmonics(living);
    }
    public  static float speed(LivingEntity living){
        return Config.getSpeedEternalFallenSoul().get().floatValue() * Handler.getArcaneHarmonics(living);
    }

}
