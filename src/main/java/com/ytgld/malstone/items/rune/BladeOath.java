package com.ytgld.malstone.items.rune;

import com.google.common.collect.Multimap;
import com.sammy.malum.common.item.ISpiritAffiliatedItem;
import com.sammy.malum.common.item.curiosities.weapons.scythe.MalumScytheItem;
import com.sammy.malum.core.helpers.ParticleHelper;
import com.sammy.malum.registry.common.*;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Runes;
import com.ytgld.malstone.items.white.WhiteArrowBlade;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.lodestone.helpers.DamageTypeHelper;
import team.lodestar.lodestone.helpers.SoundHelper;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

/**
 * 锋刀誓言符文
 * <p>
 *     使用镰刀类武器将造成二段的瞬时伤害
 * <p>
 *     极大增加镰刀类武器的横扫攻击范围
 *
 */
public class BladeOath extends Runes {
    public BladeOath(Properties builder) {
        super(builder, SpiritTypeRegistry.UMBRAL_SPIRIT);
    }
    public static void doMaxScy(LivingDamageEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity living) {
            if (Handler.hascurio(living, ItemRegs.BladeOath_.get())) {
                if (!event.getSource().is(DamageTypeRegistry.SCYTHE_MELEE)) {
                    event.setAmount(event.getAmount() * 0.05f);
                }
            }
        }
    }

    public static void doMaxScy(LivingHurtEvent event, LivingEntity attacker, LivingEntity target) {
        if (Handler.hascurio(attacker, ItemRegs.BladeOath_.get())) {
            Level level = attacker.level();
            if (!level.isClientSide()) {
                if (event.getSource().is(DamageTypeRegistry.SCYTHE_MELEE)) {
                    boolean canSweep = MalumScytheItem.canSweep(attacker);
                    if (canSweep)  {
                        ParticleHelper.SlashParticleEffectBuilder particle = ParticleHelper.createSlashingEffect(ParticleEffectTypeRegistry.SCYTHE_SLASH);
                        SoundHelper.playSound(attacker, SoundRegistry.SOULSTONE_PLACE.get(), 1.0F, 1.0F);
                        particle.mirrorRandomly(RandomSource.create()).spawnForwardSlashingParticle(attacker);
                        float damage = event.getAmount() * doubleDAMAGE(attacker);
                        float radius = 3;
                        level.getEntities(attacker, target.getBoundingBox().inflate(radius)).forEach((e) -> {
                            if (e instanceof LivingEntity livingEntity) {
                                if (livingEntity.isAlive()) {
                                    livingEntity.invulnerableTime = 0;
                                    attacker.heal(event.getAmount() / 10f);
                                    livingEntity.hurt(DamageTypeHelper.create(level, DamageTypeRegistry.SCYTHE_SWEEP, attacker), damage);
                                    livingEntity.knockback(0.4, (double) Mth.sin(attacker.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(attacker.getYRot() * ((float)Math.PI / 180F))));
                                }
                            }
                        });
                    }
                }
            }
        }
    }
    public static float doubleDAMAGE(LivingEntity living){
        return Handler.doArcaneHarmonics(living,Config.getDoubleBladeOath().get().floatValue());

    }    @Override
    public void addAttributeModifiers(Multimap<Attribute, AttributeModifier> map, SlotContext slotContext, ItemStack stack) {
        this.addAttributeModifier(map, Attributes.ATTACK_DAMAGE,
                (uuid) -> new AttributeModifier(uuid, this.getDescriptionId(),
                        -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL));

        this.addAttributeModifier(map, Attributes.ATTACK_SPEED,
                (uuid) -> new AttributeModifier(uuid, this.getDescriptionId(),
                        -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.malstone.blade_dath.text").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.blade_dath.text.1").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.blade_dath.text.3").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.literal(""));
        components.add(Component.translatable("item.malstone.blade_dath.text.2").setStyle(Style.EMPTY.withColor(color())));
    }

}
