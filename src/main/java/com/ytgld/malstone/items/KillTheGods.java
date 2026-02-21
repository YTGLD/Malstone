package com.ytgld.malstone.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sammy.malum.common.item.IVoidItem;
import com.sammy.malum.registry.common.MalumDamageTypes;
import com.sammy.malum.registry.common.MalumParticleEffectTypes;
import com.sammy.malum.registry.common.MalumSoundEvents;
import com.sammy.malum.visual_effects.networked.MalumNetworkedWeaponParticleEffectType;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import team.lodestar.lodestone.helpers.DamageTypeHelper;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.helpers.SoundHelper;
import team.lodestar.lodestone.registry.common.LodestoneAttributes;
import team.lodestar.lodestone.systems.network.WeaponParticleEffectType;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.awt.*;
import java.util.List;

/**
 * 弑神令
 * <p>
 * 击中生物将产生精血
 * <p>
 * 拾取精血获得血盾
 * <p>
 * 按下'绑定键'摧毁血盾恢复生命值和提升属性
 * <p>
 * 但是会有一定的冷却时间，在冷却时间内无法收获精血
 */
public class KillTheGods extends WhiteArrow implements IVoidItem {
    public static final String  power = "PowerKillTheGods";

    public KillTheGods(Properties properties) {
        super(properties);
    }
    public static void attackPost(LivingDamageEvent.Pre event) {
        LivingEntity target = event.getEntity();
        if (event.getSource().getEntity() instanceof Player attacker){
            if (!attacker.getCooldowns().isOnCooldown(ItemRegs.KillTheGods_.get())) {
                if (Handler.hascurio(attacker, ItemRegs.KillTheGods_.get())) {
                    dropSpirits(target, attacker);
                    attacker.getCooldowns().addCooldown(ItemRegs.KillTheGods_.get(),2);
                }
            }
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<net.minecraft.network.chat.Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.malstone.kill_the_gods.text.1").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.kill_the_gods.text.2").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.kill_the_gods.text.3").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.kill_the_gods.text.4").setStyle(Style.EMPTY.withColor(color())));
    }
    public static void  use(Player player){
        if (Handler.hascurio(player, ItemRegs.KillTheGods_.get())) {
            if (!player.getCooldowns().isOnCooldown(ItemRegs.KillTheGods_.get())) {
                AscensionHandler.triggerAscension(player.level(),player);
                CompoundTag compoundTag = player.getPersistentData();
                int amout =(int)(float) player.getData(AttReg.BloodShield_);
                if (amout > 100) {
                    amout = 100;
                }
                compoundTag.putInt(power,amout);
                player.heal(amout / 4f);
                SoundEvent sound = MalumSoundEvents.TOTEM_ACTIVATED.get();
                SoundHelper.playSound(player, sound, 0.4F, RandomHelper.randomBetween(player.getRandom(), 1.25F, 1.75F));
                player.setData(AttReg.BloodShield_,0f);
                player.getCooldowns().addCooldown(ItemRegs.KillTheGods_.get(),300);
            }
        }
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity() instanceof Player player) {
            if (!player.level().isClientSide()) {
                player.getAttributes().addTransientAttributeModifiers(doAttribute(player));
                if (player.tickCount % 20 == 1) {
                    CompoundTag compoundTag = player.getPersistentData();
                    if (compoundTag.getInt(power) > 0) {
                        compoundTag.putInt(power, compoundTag.getInt(power) - 1);
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

        float p = 0;
        if (!player.level().isClientSide()) {
            CompoundTag compoundTag = player.getPersistentData();
            float powerStronger = compoundTag.getInt(power);
            powerStronger /= 100f;
            if (powerStronger > 1) {
                powerStronger = 1;
            }
            p = powerStronger;
        }


        modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier( ResourceLocation.parse(this.getDescriptionId()),
                p, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier( ResourceLocation.parse(this.getDescriptionId()),
                p, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(Attributes.ARMOR, new AttributeModifier( ResourceLocation.parse(this.getDescriptionId()),
                p, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier( ResourceLocation.parse(this.getDescriptionId()),
                p, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(LodestoneAttributes.MAGIC_DAMAGE, new AttributeModifier( ResourceLocation.parse(this.getDescriptionId()),
                p, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(LodestoneAttributes.MAGIC_RESISTANCE, new AttributeModifier( ResourceLocation.parse(this.getDescriptionId()),
                p, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return modifiers;
    }

    public static Handler.SpiritSpawner spawnSpirits(Entity target) {
        return new Handler.SpiritSpawner(target);
    }
    public static void dropSpirits(LivingEntity target, LivingEntity attacker) {
        Level level = target.level();
        Handler.SpiritSpawner spiritSpawner = spawnSpirits(target).setPreferredCollector(attacker);
        spiritSpawner.spawnSpirits(level);
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = super.getAttributeModifiers(slotContext, id, stack);
        CuriosApi.addSlotModifier(modifierMultimap,"rune",id,1, AttributeModifier.Operation.ADD_VALUE);
        modifierMultimap.put(AttReg.MaxBloodShield,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                40, AttributeModifier.Operation.ADD_VALUE));
        return modifierMultimap;
    }

    public static int maxShield (Player player){
        return (int) player.getAttributeValue(AttReg.MaxBloodShield);
    }

    @Override
    public boolean canUseSkill() {
        return true;
    }

    public static class AscensionHandler {
        public static void triggerAscension(Level level, Player player) {
            if (level instanceof ServerLevel serverLevel) {
                RandomSource random = serverLevel.getRandom();
                float baseDamage = (float)player.getAttributes().getValue(Attributes.ATTACK_DAMAGE) * 8;
                float magicDamage = (float)player.getAttributes().getValue(LodestoneAttributes.MAGIC_DAMAGE) * 4;
                AABB aabb = player.getBoundingBox().inflate((double)4.0F, (double)1.0F, (double)4.0F);
                SoundEvent sound = MalumSoundEvents.VOID_TRANSMUTATION.get();
                MalumNetworkedWeaponParticleEffectType.MalumWeaponParticleEffectBuilder<WeaponParticleEffectType.WeaponParticleEffectData> particle = MalumParticleEffectTypes.SCYTHE_ASCENSION_SPIN.createEffect(player).mirroredRandomly(random);
                particle.color(new Color(255,0,0));

                for(Entity target : serverLevel.getEntities(player, aabb, (t) -> ascensionCanHitEntity(player, t))) {
                    DamageSource damageSource = DamageTypeHelper.create(serverLevel, MalumDamageTypes.SCYTHE_ASCENSION, player);
                    target.invulnerableTime = 0;
                    boolean success = target.hurt(damageSource, baseDamage);
                    if (success && target instanceof LivingEntity livingentity) {
                        if (magicDamage > 0.0F && !livingentity.isDeadOrDying()) {
                            livingentity.invulnerableTime = 0;
                            livingentity.hurt(DamageTypeHelper.create(serverLevel, MalumDamageTypes.VOODOO, player), magicDamage);
                        }

                        SoundHelper.playSound(player, sound, 0.8F, RandomHelper.randomBetween(random, 0.75F, 1.25F));
                    }
                }
                Vec3 slashPosition = player.position().add((double)0.0F, (double)player.getBbHeight() * (double)0.75F, (double)0.0F);
                Vec3 slashDirection = player.getLookAngle().multiply((double)1.0F, (double)0.0F, (double)1.0F);
                particle.at(slashPosition).aimedAt(slashDirection).spawn(serverLevel);

                for(int i = 0; i < 3; ++i) {
                    SoundHelper.playSound(player, sound, 0.4F, RandomHelper.randomBetween(random, 1.25F, 1.75F));
                }

                SoundHelper.playSound(player, sound, 0.8F, RandomHelper.randomBetween(random, 1.25F, 1.5F));
            }
        }

        protected static boolean ascensionCanHitEntity(Player attacker, Entity pTarget) {
            if (pTarget instanceof TamableAnimal tamableAnimal) {
                if (tamableAnimal.isTame()) {
                    return false;
                }
            }

            if (!pTarget.canBeHitByProjectile()) {
                return false;
            } else {
                return pTarget != attacker && !attacker.isPassengerOfSameVehicle(pTarget);
            }
        }
    }

}
