package com.ytgld.malstone.items.twisted;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Twisted;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 尸釜
 * <p>
 * 吃下的食物会被二次消化，期间缓慢恢复饥饿值,饱和度,生命值
 * <p>
 * 吃下不同的食物会奖励的坚韧之血属性
 * <p>
 * 	若被哭泣之井充能：
 * <p>
 * 	增加一定物理和魔法抗性以及额外的坚韧之血属性
 * <p>
 * 	大幅增加进食速度和消化速度
 */
public class CorpseCauldron extends Twisted {

    public static final String foodData = "foodDataCorpseCauldron";

    public CorpseCauldron(Properties properties) {
        super(properties);
    }
    public static void eatFood(LivingEntityUseItemEvent.Finish event){
        if (event.getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.CorpseCauldron_.get())) {
                ItemStack foodStack = event.getItem();
                CompoundTag compoundTag = player.getPersistentData();
                @Nullable FoodProperties foodProperties = foodStack.getFoodProperties(player);
                if (foodProperties != null) {
                    if (compoundTag.getInt(foodData) < 150) {
                        int food = (int) (foodProperties.getNutrition() + foodProperties.getSaturationModifier());
                        compoundTag.putInt(foodData, compoundTag.getInt(foodData) + food);
                    }
                    CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                        Map<String, ICurioStacksHandler> curios = handler.getCurios();
                        for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                            ICurioStacksHandler stacksHandler = entry.getValue();
                            IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                            for (int i = 0; i < stacksHandler.getSlots(); i++) {
                                ItemStack cc = stackHandler.getStackInSlot(i);
                                if (cc.is(ItemRegs.CorpseCauldron_.get())) {
                                    CompoundTag caTag = cc.getTag();
                                    if (caTag != null) {
                                        String string = foodStack.getDescriptionId();
                                        if (!caTag.getBoolean(string)) {
                                            caTag.putBoolean(string, true);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity() instanceof Player player) {
            int time = 20;
            if (this.hasWeepingWllPower(stack)) {
                time /= 4;
            }
            CompoundTag compoundTag = player.getPersistentData();
            if (player.tickCount % time == 1) {
                if (compoundTag.getInt(foodData) > 0) {
                    if (player.getFoodData().needsFood()) {
                        player.getFoodData().eat(1, 0.2f);
                    }else {
                        player.getFoodData().eat(0, 0.4f);

                    }
                    player.heal(1);
                    compoundTag.putInt(foodData, compoundTag.getInt(foodData) - 1);
                }
            }
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            if (!player.level().isClientSide) {
                player.getAttributes().addTransientAttributeModifiers(doAttribute(player,stack));
            }
        }
    }
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (slotContext.entity() instanceof Player player) {
            if (!entity.level().isClientSide()) {
                entity.getAttributes().removeAttributeModifiers(doAttribute(player,stack));
            }
        }
    }
    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.translatable("item.malstone.corpse_cauldron.text.1").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.corpse_cauldron.text.2").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.translatable("item.malstone.corpse_cauldron.text.3").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.corpse_cauldron.text.4").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.corpse_cauldron.text.5").setStyle(Style.EMPTY.withColor(color())));
        return new MalstoneText(stack,tooltipComponents);
    }
    public Multimap<Attribute, AttributeModifier> doAttribute(Player player,ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        float add = attribute(player);
        CompoundTag compoundTag = stack.getTag();
        float it = 0;
        if (compoundTag != null) {
            int size = compoundTag.size();
            it = size / 3f;
            if (it > 30) {
                it = 30;
            }
        }
        modifiers.put(AttReg.ToughBlood.get(), new AttributeModifier(UUID.fromString("8773eef5-d9ab-33f4-b901-5d723e8af879"), this.getDescriptionId(),
                it, AttributeModifier.Operation.ADDITION));

        if (this.hasWeepingWllPower(stack)) {
            modifiers.put(AttReg.MagicRes.get(), new AttributeModifier(UUID.fromString("3e59bd01-5286-3f1f-b563-8ee24767718b"), this.getDescriptionId(),
                    add, AttributeModifier.Operation.ADDITION));
            modifiers.put(AttReg.DamageRes.get(), new AttributeModifier(UUID.fromString("3e59bd01-5286-3f1f-b563-8ee24767718b"), this.getDescriptionId(),
                    add, AttributeModifier.Operation.ADDITION));
            modifiers.put(AttReg.ToughBlood.get(), new AttributeModifier(UUID.fromString("3e59bd01-5286-3f1f-b563-8ee24767718b"), this.getDescriptionId(),
                    add, AttributeModifier.Operation.MULTIPLY_BASE));
            modifiers.put(AttReg.EatTime.get(), new AttributeModifier(UUID.fromString("3e59bd01-5286-3f1f-b563-8ee24767718b"), this.getDescriptionId(),
                    -add * 2, AttributeModifier.Operation.MULTIPLY_BASE));
        }



        return modifiers;
    }
    public static float attribute(Player player){
        return Handler.doArcaneHarmonics(player,Config.getAttributeCorpseCauldron().get().floatValue());
    }
    @Override
    public boolean canUseWeepingPower() {
        return true;
    }
    @Override
    public int maxWeepingPower(ItemStack stack) {
        return 1200;
    }
}
