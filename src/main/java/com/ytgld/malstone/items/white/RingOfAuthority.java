package com.ytgld.malstone.items.white;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sammy.malum.common.entity.bolt.DrainingBoltEntity;
import com.sammy.malum.registry.common.item.ItemRegistry;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.*;

/**
 * 扭曲权威之戒
 * <p>
 * 对于挽魂锋镰和宙界重斧：
 * <p>
 * 造成伤害时会逐渐增加攻击速度
 * <p>
 * 使用侵蚀权杖发射侵蚀能量束时对目标造成大量的负面状态
 * <p>
 * 若目标免疫负面效果
 * <p>
 * 则每免疫一种状态都将增加20%侵蚀能量束的伤害
 *
 */
public class RingOfAuthority extends WhiteArrow {

    private static final String attackSpeedTag= "attackSpeedTagOfRingOfAuthority";

    public RingOfAuthority(Properties properties) {
        super(properties);
    }
    public static void attack(LivingDamageEvent event){
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.RingOfAuthority_.get())) {
                if (!player.level().isClientSide()) {
                    Set<Item> white = Set.of(
                            ItemRegistry.EDGE_OF_DELIVERANCE.get(),
                            ItemRegistry.WEIGHT_OF_WORLDS.get(),
                            ItemRegistry.EROSION_SCEPTER.get()
                    );
                    if (white.contains(player.getMainHandItem().getItem())) {
                        CompoundTag compoundTag = player.getPersistentData();
                        if (compoundTag.getFloat(attackSpeedTag) < maxSped(player)) {
                            compoundTag.putFloat(attackSpeedTag, compoundTag.getFloat(attackSpeedTag) + maxSped(player) / 10f);
                        }
                    }
                }
            }
        }
        if (event.getSource().getDirectEntity() instanceof DrainingBoltEntity drainingBoltEntity) {
            if (drainingBoltEntity.getOwner() instanceof Player player) {
                if (Handler.hascurio(player, ItemRegs.RingOfAuthority_.get())) {
                    if (!player.level().isClientSide()) {
                        Set<MobEffect> effects = Set.of(
                                MobEffects.DIG_SLOWDOWN,
                                MobEffects.POISON,
                                MobEffects.WEAKNESS,
                                MobEffects.MOVEMENT_SLOWDOWN,
                                MobEffects.DARKNESS,
                                MobEffects.BLINDNESS
                        );
                        LivingEntity living = event.getEntity();
                        List<Integer> damage = new ArrayList<>();
                        for (MobEffect effect : effects) {
                            if (!living.addEffect(new MobEffectInstance(effect,600,1))){
                                damage.add(1);
                            }
                        }
                        float end = 0.2F;
                        for (int ignored : damage) {
                            end += 0.2F;
                        }
                        event.setAmount(event.getAmount() * (1 + end));
                    }
                }
            }
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity() instanceof Player player) {
            if (!player.level().isClientSide()) {
                player.getAttributes().addTransientAttributeModifiers(doAttribute(player));
                if (player.tickCount % 60 == 1) {
                    CompoundTag compoundTag = player.getPersistentData();
                    if (compoundTag.getFloat(attackSpeedTag) > 0) {
                        compoundTag.putFloat(attackSpeedTag, compoundTag.getFloat(attackSpeedTag) - maxSped(player) / 20f);
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
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.malstone.ring_of_authority.text.1").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.ring_of_authority.text.2").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.literal(""));
        components.add(Component.translatable("item.malstone.ring_of_authority.text.3").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.ring_of_authority.text.4").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.ring_of_authority.text.5").setStyle(Style.EMPTY.withColor(color())));
    }

    public Multimap<Attribute, AttributeModifier> doAttribute(LivingEntity player) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        float speed = 0;
        if (!player.level().isClientSide()) {
            CompoundTag compoundTag = player.getPersistentData();
            speed = compoundTag.getFloat(attackSpeedTag);
        }


        modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("68301082-0c6f-34cc-9a03-f1d8824d1d08"), this.getDescriptionId(),
                speed, AttributeModifier.Operation.MULTIPLY_TOTAL));

        return modifiers;
    }


    public static float maxSped(Player player){
        return 1.2f;
    }
}
