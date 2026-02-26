package com.ytgld.malstone.items.rune;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sammy.malum.registry.common.AttributeRegistry;
import com.sammy.malum.registry.common.SpiritTypeRegistry;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.Runes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

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
        super(builder, SpiritTypeRegistry.UMBRAL_SPIRIT);
    }
    @Override
    public void addAttributeModifiers(Multimap<Attribute, AttributeModifier> map, SlotContext slotContext, ItemStack stack) {
        this.addAttributeModifier(map, AttributeRegistry.SOUL_WARD_CAP.get(),
                (uuid) -> new AttributeModifier(uuid, this.getDescriptionId(),
                        4, AttributeModifier.Operation.ADDITION));

        this.addAttributeModifier(map, AttReg.ChaosErosion.get(),
                (uuid) -> new AttributeModifier(uuid, this.getDescriptionId(),
                        0.2, AttributeModifier.Operation.ADDITION));
    }

    public static void healSOUL_WARD_CAP(Player player){
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



    public Multimap<Attribute, AttributeModifier> doAttribute(LivingEntity player) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        float add = 0;
        if (!player.level().isClientSide()) {
            CompoundTag compoundTag = player.getPersistentData();
            add = compoundTag.getFloat(namePath);
        }
        if (add > maxDamageAndArmor(player)) {
            add = maxDamageAndArmor(player);
        }


        modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("0ef55c79-9bc5-371f-96a7-29e4485d0c04"), this.getDescriptionId(),
                add, AttributeModifier.Operation.MULTIPLY_BASE));

        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("0ef55c79-9bc5-371f-96a7-29e4485d0c04"), this.getDescriptionId(),
                add, AttributeModifier.Operation.MULTIPLY_BASE));

        modifiers.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("0ef55c79-9bc5-371f-96a7-29e4485d0c04"), this.getDescriptionId(),
                add, AttributeModifier.Operation.MULTIPLY_BASE));

        modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("0ef55c79-9bc5-371f-96a7-29e4485d0c04"), this.getDescriptionId(),
                add, AttributeModifier.Operation.MULTIPLY_BASE));

        return modifiers;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.malstone.eternal_fallen_soul.text").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.eternal_fallen_soul.text.1",maxDamageAndArmor(null)*100f).setStyle(Style.EMPTY.withColor(color())));
    }

    public  static float maxDamageAndArmor(LivingEntity living){
        return Handler.doArcaneHarmonics(living,Config.getMaxEternalFallenSoul().get().floatValue());
    }
    public  static float speed(LivingEntity living){
        return Handler.doArcaneHarmonics(living,Config.getSpeedEternalFallenSoul().get().floatValue());
    }

}
