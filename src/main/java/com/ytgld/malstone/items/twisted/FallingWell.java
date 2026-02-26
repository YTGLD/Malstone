package com.ytgld.malstone.items.twisted;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.effects.Effects;
import com.ytgld.malstone.items.init.Twisted;
import com.ytgld.malstone.magic.DataReg;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 堕井
 * <p>
 * 腐朽心盾可像灵魂护盾吸收部分伤害
 * <p>
 * 腐朽心盾的降低会带来属性加成
 * <p>
 * 可以使用哭泣之井为此物品充能
 * <p>
 * 在激活状态下可以降低附近生物的生命上限和伤害减免
 * <p>
 * 激活状态下的堕井会更有效的恢复腐朽心盾和提高强度
 **/


public class FallingWell extends Twisted  {
    public FallingWell(Properties properties) {
        super(properties);
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity() instanceof Player player) {
            if (!player.level().isClientSide()) {
                player.getAttributes().addTransientAttributeModifiers(doAttribute(stack,player));
                if (player.tickCount % 20 == 1) {
                    CompoundTag compoundTag = stack.get(DataReg.tag);
                    if (compoundTag !=null) {
                        if (this.hasWeepingWllPower(stack)) {
                            Vec3 playerPos = player.position();
                            int range = 12;
                            List<LivingEntity> entitiesOfClass = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(playerPos.x - range, playerPos.y - range, playerPos.z - range, playerPos.x + range, playerPos.y + range, playerPos.z + range));
                            for (LivingEntity entity : entitiesOfClass){
                                if (!entity.is(player)) {
                                    if (!entity.addEffect(new MobEffectInstance(Effects.fFallCurse, 200, 1, false, false))) {
                                        entity.hurt(entity.damageSources().playerAttack(player), 20);
                                    }
                                }
                            }
                        }
                    }else {
                        stack.set(DataReg.tag,new CompoundTag());
                    }
                }
            }
        }
    }
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide()) {
            entity.getAttributes().removeAttributeModifiers(doAttribute(stack,entity));
        }
    }
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(ItemStack stack,LivingEntity entity) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();

        float add = 0;
        if (this.hasWeepingWllPower(stack)) {
            add = addPower(entity);
        }
        float lv = (float) (entity.getData(AttReg.DecayShield) / entity.getAttributeValue(AttReg.MaxDecayShield));
        lv *= 100;
        float now = (int) (100 - (lv));
        if (now < 0) {
            now = 0;
        }
        now /= 100f;
        modifiers.put(Attributes.MOVEMENT_SPEED,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                now / 4f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        modifiers.put(Attributes.ATTACK_SPEED,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                now / 5F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        modifiers.put(Attributes.ATTACK_DAMAGE,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                now / 6F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        modifiers.put(Attributes.ARMOR,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                now * 10f, AttributeModifier.Operation.ADD_VALUE));

        modifiers.put(AttReg.SpeedDecayShield,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                -add, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        modifiers.put(AttReg.StrongerDecayShield,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        return modifiers;
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = super.getAttributeModifiers(slotContext, id, stack);
        modifierMultimap.put(AttReg.MaxDecayShield,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                10, AttributeModifier.Operation.ADD_VALUE));
        modifierMultimap.put(AttReg.SpeedDecayShield,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                -0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        modifierMultimap.put(AttReg.StrongerDecayShield,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        CuriosApi.addSlotModifier(modifierMultimap,"well",id,2, AttributeModifier.Operation.ADD_VALUE);

        return modifierMultimap;
    }

    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {

        tooltipComponents.add(Component.translatable("item.malstone.falling_well.text.1").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.falling_well.text.2").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.translatable("item.malstone.falling_well.text.3").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.falling_well.text.4").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.falling_well.text.5").setStyle(Style.EMPTY.withColor(color())));
        return new MalstoneText(stack,tooltipComponents);
    }
    public static float addPower(LivingEntity player){
        return Handler.doArcaneHarmonics(player, Config.getAddPowerFallingWell().get().floatValue());
    }

    @Override
    public boolean canUseWeepingPower() {
        return true;
    }

    @Override
    public int maxWeepingPower(ItemStack stack) {
        return 1800;
    }

}
