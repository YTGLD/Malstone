package com.ytgld.malstone.items.white;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sammy.malum.registry.common.AttributeRegistry;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.ChatFormatting;
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
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 破命刀
 * <p>
 * 损失生命值带来大量属性加成
 * <p>
 * 生命值最多恢复到50%
 * <p>
 * 饥饿值在生命恢复到50%时停止消耗
 * <p>
 * -100%灵魂护盾
 * <p>
 * +40%最大生命值
 */
public class BreakingTheLife extends WhiteArrow {

    public BreakingTheLife(Properties properties) {
        super(properties);
    }

    public static boolean food(Player player){
        float max = (player.getMaxHealth() * max(player));
        if (player.getHealth() >= max){
            return Handler.hascurio(player, ItemRegs.BreakingTheLife_.get());
        }
        return false;
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.malstone.breaking_the_life.text.1").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.breaking_the_life.text.2",max(null)*100f).setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.breaking_the_life.text.3",max(null)*100f).setStyle(Style.EMPTY.withColor(color())));
    }

    public static void lLivingHealEvent(LivingHealEvent event){
        if (event.getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.BreakingTheLife_.get())) {
                float amout = event.getAmount();
                float max = (player.getMaxHealth() * max(player));
                if (amout + player.getHealth() > max){
                    float s = (amout + player.getHealth()) - max;
                    float a = amout - s;
                    event.setAmount(a);
                }
                if (player.getHealth() >= max) {
                    event.setAmount(0);
                    event.setCanceled(true);
                }
            }
        }
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> get = super.getAttributeModifiers(slotContext, uuid, stack);
        get.put(AttributeRegistry.SOUL_WARD_CAP.get(),new AttributeModifier(uuid,this.getDescriptionId(),
                -1, AttributeModifier.Operation.MULTIPLY_TOTAL));
        get.put(Attributes.MAX_HEALTH,new AttributeModifier(uuid,this.getDescriptionId(),
                0.2f, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return get;
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide()) {
            entity.getAttributes().addTransientAttributeModifiers(doAttribute(entity));
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide()) {
            entity.getAttributes().removeAttributeModifiers(doAttribute(entity));
        }
    }

    public Multimap<Attribute, AttributeModifier> doAttribute(LivingEntity living) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        float lv = living.getHealth() / living.getMaxHealth();
        lv *= 100;
        float now = (int) (100 - (lv));
        if (now < 0) {
            now = 0;
        }
        now /= 100f;

        float speed = 0.85f * now;
        float damage = 0.5f * now;
        float attSpeed = 0.8f * now;
        float armor = 0.3f * now;
        float swimming = 0.9f * now;

        modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("02b93f25-6b3e-31ad-b8e5-b502d04aa517"), this.getDescriptionId(),
                speed, AttributeModifier.Operation.MULTIPLY_BASE));

        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("02b93f25-6b3e-31ad-b8e5-b502d04aa517"), this.getDescriptionId(),
                damage, AttributeModifier.Operation.MULTIPLY_BASE));

        modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("02b93f25-6b3e-31ad-b8e5-b502d04aa517"), this.getDescriptionId(),
                attSpeed, AttributeModifier.Operation.MULTIPLY_BASE));

        modifiers.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("02b93f25-6b3e-31ad-b8e5-b502d04aa517"), this.getDescriptionId(),
                armor, AttributeModifier.Operation.MULTIPLY_BASE));

        modifiers.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(UUID.fromString("02b93f25-6b3e-31ad-b8e5-b502d04aa517"), this.getDescriptionId(),
                swimming, AttributeModifier.Operation.MULTIPLY_BASE));

        return modifiers;
    }

    public static float max(@Nullable Player player){
        return Config.getLife_max_BreakingTheLife().get().floatValue();
    }

}
