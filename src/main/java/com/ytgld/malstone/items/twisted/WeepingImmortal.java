package com.ytgld.malstone.items.twisted;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sammy.malum.registry.common.SoundRegistry;
import com.sammy.malum.registry.common.item.ItemRegistry;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.BaseItem;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Twisted;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.helpers.SoundHelper;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 泣仙
 * <p>
 *跳入哭泣之井会短暂获得大量的属性加成
 * <p>
 * 可以通过给予泣仙幽影精魂来达到跳入哭泣之井的效果
 * <p>
 * 亦或者拾取幽影精魂来达到类似效果，但是强化时长会有所降低
 */
public class WeepingImmortal extends Twisted {
    public WeepingImmortal(Properties properties) {
        super(properties);
    }

    public static final String timeCache = "timeCacheWeepingImmortal" ;


    public static void pickUp(LivingEntity entity, ItemStack other){
        if (Handler.hascurio(entity, ItemRegs.WeepingImmortal_.get())) {
            if (other.is(ItemRegistry.UMBRAL_SPIRIT.get())) {
                CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
                    Map<String, ICurioStacksHandler> curios = handler.getCurios();
                    for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                        ICurioStacksHandler stacksHandler = entry.getValue();
                        IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                        for (int i = 0; i < stacksHandler.getSlots(); i++) {
                            ItemStack stack = stackHandler.getStackInSlot(i);
                            if (stack.is(ItemRegs.WeepingImmortal_.get())) {
                                if (stack.getItem() instanceof BaseItem item) {
                                    if (item.canUseWeepingPower()) {
                                        if (other.is(ItemRegistry.UMBRAL_SPIRIT.get())) {
                                            item.addWeepingPower(stack, 0.1f);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (other.is(ItemRegistry.UMBRAL_SPIRIT.get())) {
            stack.getOrCreateTag();
            addWeepingPower(stack);
            other.shrink(1);
            SoundHelper.playSound(player, SoundRegistry.VOID_TRINKET_EQUIP.get(), 0.8F, RandomHelper.randomBetween(player.getRandom(), 1, 1));
            return true;
        }
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }

    @Override
    public boolean canUseWeepingPower() {
        return true;
    }

    @Override
    public int maxWeepingPower(ItemStack stack) {
        return 3600;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity() instanceof Player player) {
            CompoundTag compoundTag = player.getPersistentData();
            if (this.hasWeepingWllPower(stack)) {
                if (!compoundTag.getBoolean(timeCache)) {
                    compoundTag.putBoolean(timeCache,true);
                }
            }
            CompoundTag stackTag = stack.getTag();
            if (stackTag != null) {
                if (stackTag.getInt(weepingWellPower) <= 10
                        && stackTag.getInt(weepingWellPower) > 4) {
                    if (compoundTag.getBoolean(timeCache)) {
                        compoundTag.putBoolean(timeCache,false);
                    }
                }
            }

            if (!player.level().isClientSide()) {
                player.getAttributes().addTransientAttributeModifiers(doAttribute(player));
            }
        }
    }
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (slotContext.entity() instanceof Player player) {
            if (!entity.level().isClientSide()) {

                CompoundTag compoundTag = player.getPersistentData();
                compoundTag.putBoolean(timeCache, false);

                entity.getAttributes().removeAttributeModifiers(doAttribute(player));
            }
        }
    }
    public Multimap<Attribute, AttributeModifier> doAttribute(Player player) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        float add = 0;
        CompoundTag compoundTag = player.getPersistentData();
        if (compoundTag.getBoolean(timeCache)) {
            add = attribute();
        }
        modifiers.put(Attributes.MOVEMENT_SPEED,new AttributeModifier(UUID.fromString("d6d392a6-44f8-3510-88ef-1f2f06277d32"),this.getDescriptionId(),
                add, AttributeModifier.Operation.MULTIPLY_BASE));
        modifiers.put(Attributes.ATTACK_SPEED,new AttributeModifier(UUID.fromString("d6d392a6-44f8-3510-88ef-1f2f06277d32"),this.getDescriptionId(),
                 add, AttributeModifier.Operation.MULTIPLY_BASE));
        modifiers.put(Attributes.ATTACK_DAMAGE,new AttributeModifier(UUID.fromString("d6d392a6-44f8-3510-88ef-1f2f06277d32"),this.getDescriptionId(),
                 add, AttributeModifier.Operation.MULTIPLY_BASE));

        modifiers.put(LodestoneAttributeRegistry.MAGIC_DAMAGE.get(),new AttributeModifier(UUID.fromString("d6d392a6-44f8-3510-88ef-1f2f06277d32"),this.getDescriptionId(),
                add, AttributeModifier.Operation.MULTIPLY_BASE));
        modifiers.put(LodestoneAttributeRegistry.MAGIC_RESISTANCE.get(),new AttributeModifier(UUID.fromString("d6d392a6-44f8-3510-88ef-1f2f06277d32"),this.getDescriptionId(),
                add, AttributeModifier.Operation.MULTIPLY_BASE));
        return modifiers;
    }

    public static float attribute(){
        return Config.getAttributeWeepingImmortal().get().floatValue();
    }
    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.translatable("item.malstone.weeping_immortal.text.1").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.weeping_immortal.text.2").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.weeping_immortal.text.3").setStyle(Style.EMPTY.withColor(color())));
        return new MalstoneText(stack,tooltipComponents);
    }
}
