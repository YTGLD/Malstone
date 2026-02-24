package com.ytgld.malstone.items.rune;

import com.google.common.collect.Multimap;
import com.sammy.malum.common.item.curiosities.weapons.scythe.MalumScytheItem;
import com.sammy.malum.registry.common.MalumDamageTypes;
import com.sammy.malum.registry.common.MalumParticleEffectTypes;
import com.sammy.malum.registry.common.MalumSoundEvents;
import com.sammy.malum.registry.common.magic.MalumSpiritTypes;
import com.sammy.malum.visual_effects.networked.MalumNetworkedWeaponParticleEffectType;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Runes;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import team.lodestar.lodestone.helpers.DamageTypeHelper;
import team.lodestar.lodestone.helpers.SoundHelper;
import team.lodestar.lodestone.systems.network.WeaponParticleEffectType;
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
        super(builder, MalumSpiritTypes.UMBRAL_SPIRIT, MalumTrinketType.RUNE);
    }

    public static void doMaxScy(LivingDamageEvent.Pre event) {
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity living) {
            if (Handler.hascurio(living, ItemRegs.BladeOath_.get())) {
                if (!event.getSource().is(MalumDamageTypes.SCYTHE_MELEE)) {
                    event.setNewDamage(event.getNewDamage() * 0.05f);
                }
            }
        }
    }

    public static void doMaxScy(LivingDamageEvent.Pre event, LivingEntity attacker, LivingEntity target,ItemStack stack) {
        if (Handler.hascurio(attacker, ItemRegs.BladeOath_.get())) {
            Level level = attacker.level();
            if (level instanceof ServerLevel serverLevel) {
                if (event.getSource().is(MalumDamageTypes.SCYTHE_MELEE)) {
                    MalumNetworkedWeaponParticleEffectType.MalumWeaponParticleEffectBuilder<WeaponParticleEffectType.WeaponParticleEffectData> particle =
                            MalumParticleEffectTypes.SCYTHE_SLASH.createEffect().originatesFrom(attacker)
                                    .targets(target).color(stack.getItem())
                                    .upwardOffset(-0.4F).forwardOffset(0.8F);
                    SoundHelper.playSound(attacker, MalumSoundEvents.SOULSTONE_PLACE.get(), 1.0F, 1.0F);
                    particle.mirrored(true).spawn(serverLevel);
                    float damage = event.getNewDamage() * doubleDAMAGE(attacker);
                    float radius = 3;
                    level.getEntities(attacker, target.getBoundingBox().inflate(radius)).forEach((e) -> {
                        if (e instanceof LivingEntity livingEntity) {
                            if (livingEntity.isAlive()) {
                                livingEntity.invulnerableTime = 0;
                                attacker.heal(event.getNewDamage() / 10f);
                                livingEntity.hurt(DamageTypeHelper.create(level, MalumDamageTypes.SCYTHE_SWEEP, attacker), damage);
                                livingEntity.knockback(0.4, (double) Mth.sin(attacker.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(attacker.getYRot() * ((float) Math.PI / 180F))));
                            }
                        }

                    });
                }
            }
        }
    }
    public static float doubleDAMAGE(LivingEntity living) {
        return Handler.doArcaneHarmonics(living, Config.getDoubleBladeOath().get().floatValue());
    }
    @Override
    public void addAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> map, SlotContext slotContext, ItemStack stack) {
        this.addAttributeModifier(map, Attributes.ATTACK_DAMAGE,
                new AttributeModifier( ResourceLocation.parse(this.getDescriptionId()),
                        -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        
        this.addAttributeModifier(map, Attributes.ATTACK_DAMAGE,
                new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                        -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        this.addAttributeModifier(map, Attributes.ATTACK_SPEED,
                new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                        -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.malstone.blade_dath.text").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.blade_dath.text.1").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.blade_dath.text.3").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.translatable("item.malstone.blade_dath.text.2").setStyle(Style.EMPTY.withColor(color())));
    }

}
