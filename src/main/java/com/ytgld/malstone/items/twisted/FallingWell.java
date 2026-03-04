package com.ytgld.malstone.items.twisted;


import com.google.common.collect.Multimap;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.effects.Effects;
import com.ytgld.malstone.items.init.Twisted;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

                if (player.tickCount % 20 == 1) {
                    CompoundTag compoundTag = stack.getTag();
                    if (compoundTag !=null) {
                        addEffect(player,stack);
                    }else {
                        stack.getOrCreateTag();
                    }
                }
            }
        }
    }
    public void addEffect(Player player,ItemStack stack){
        Set<String> blacklist = new HashSet<>();
        for (String s : Config.getEffectFallWell().get()) {
            String[] parts = s.split(":");
            if (parts.length > 0) {
                blacklist.add(parts[0]+":"+parts[1]);
            }
        }
        if (this.hasWeepingWllPower(stack)) {
            if (Config.getAttributeFallCurse().get()) {
                Vec3 playerPos = player.position();
                int range = 12;
                List<LivingEntity> entitiesOfClass = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(playerPos.x - range, playerPos.y - range, playerPos.z - range, playerPos.x + range, playerPos.y + range, playerPos.z + range));
                for (LivingEntity entity : entitiesOfClass) {
                    ResourceLocation resourceLocation = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
                    if (!blacklist.contains(resourceLocation.getNamespace()+":"+resourceLocation.getPath())){
                        if (!entity.is(player)) {
                            if (!entity.addEffect(new MobEffectInstance(Effects.fFallCurse.get(), 200, 1, false, false))) {
                                entity.hurt(entity.damageSources().playerAttack(player), 20);
                            }
                        }
                    }

                }
            }
        }
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID id, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifierMultimap = super.getAttributeModifiers(slotContext, id, stack);

//        CuriosApi.addSlotModifier(modifierMultimap,"belt",id,1, AttributeModifier.Operation.ADDITION);
        CuriosApi.addSlotModifier(modifierMultimap,"necklace",id,1, AttributeModifier.Operation.ADDITION);
//        CuriosApi.addSlotModifier(modifierMultimap,"rune",id,1, AttributeModifier.Operation.ADDITION);

        CuriosApi.addSlotModifier(modifierMultimap,"well",id,2, AttributeModifier.Operation.ADDITION);

        return modifierMultimap;
    }

    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {
        if (Config.getAttributeFallCurse().get()) {
            tooltipComponents.add(Component.translatable("item.malstone.falling_well.text.4").setStyle(Style.EMPTY.withColor(color())));
        }
        return new MalstoneText(stack,tooltipComponents);
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
