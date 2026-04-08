package com.ytgld.malstone;

import com.sammy.malum.common.capability.MalumPlayerDataCapability;
import com.sammy.malum.core.handlers.SoulWardHandler;
import com.sammy.malum.registry.common.AttributeRegistry;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.*;
import com.ytgld.malstone.items.rune.BladeOath;
import com.ytgld.malstone.items.rune.Martyrdom;
import com.ytgld.malstone.items.twisted.CorpseCauldron;
import com.ytgld.malstone.items.twisted.DieAbyss;
import com.ytgld.malstone.items.twisted.ExtremelyDead;
import com.ytgld.malstone.items.white.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MyEvent {
    @SubscribeEvent
    public void eatFood(LivingEntityUseItemEvent.Start event){
        LivingEntity living = event.getEntity();
        if (living instanceof Player player) {
            if (event.getItem().getUseAnimation() == UseAnim.EAT) {
                AttributeInstance attribute = player.getAttribute(AttReg.EatTime.get());
                if (attribute != null) {
                    float value = (float) attribute.getValue();
                    value = Math.max(value, 0.1f);
                    event.setDuration((int) (event.getDuration() * value));
                }
            }
        }
    }

    private void damageAndMagic(LivingDamageEvent event){
        LivingEntity living = event.getEntity();
        if (living instanceof Player player) {
            if (!event.getSource().is(DamageTypes.MAGIC)){
                AttributeInstance attribute = player.getAttribute(AttReg.DamageRes.get());
                if (attribute != null) {
                    float value = (float) attribute.getValue();
                    float doIt = Math.max(1 - value,0.1f);
                    event.setAmount(event.getAmount() * doIt);
                }
            }else {
                AttributeInstance attribute = player.getAttribute(AttReg.MagicRes.get());
                if (attribute != null) {
                    float value = (float) attribute.getValue();
                    float doIt = Math.max(1 - value,0.1f);
                    event.setAmount(event.getAmount() * doIt);
                }
            }
        }
    }
    @SubscribeEvent
    public void Finish(LivingEntityUseItemEvent.Finish event){
        CorpseCauldron.eatFood(event);
    }


    @SubscribeEvent
    public void LivingHealEvent(LivingHealEvent event){
        HugeSouls.lLivingHealEvent(event);
        BreakingTheLife.lLivingHealEvent(event);
        LivingEntity living = event.getEntity();
        if (living instanceof Player player && !player.level().isClientSide) {
            AttributeInstance attribute = player.getAttribute(AttReg.ChaosErosion.get());
            AttributeInstance capacity = player.getAttribute(AttributeRegistry.SOUL_WARD_CAP.get());
            if (attribute != null && capacity!=null) {
                SoulWardHandler handler = MalumPlayerDataCapability.getCapability(player).soulWardHandler;
                float value = (float) attribute.getValue();
                boolean canHeal = Mth.nextInt(RandomSource.create(), 0, 100) <= (value * 100);
                if (canHeal) {
                    if (handler.soulWard < capacity.getValue()) {
                        handler.recoverSoulWard(player);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void LeftClickEmpty(LivingDeathEvent event) {
        ExtremelyDead.killBlack(event);
        DieAbyss.eatOfPlayer(event);
    }
    @SubscribeEvent
    public void LeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        BreakingTheWeapon.pack(event);
    }
    @SubscribeEvent
    public void LeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        BreakingTheWeapon.pack(event);
    }

    @SubscribeEvent
    public void LivingDamageEvent(LivingDamageEvent event){
        damageAndMagic(event);
        BladeOath.doMaxScy(event);
        HugeSouls.lLivingDamageEvent(event);
        WhiteArrowBlade.lLivingDamageEvent(event);
        RingOfAuthority.attack(event);
        Martyrdom.attackPost(event);
        Martyrdom.attackPre(event);
        BreakingTheWeapon.attackADamage(event);
    }
    @SubscribeEvent
    public void tooltip(ItemTooltipEvent event){
        if (event.getItemStack().getItem() instanceof BaseItem baseItem){
            if (baseItem.canUseWeepingPower()) {
                if (!baseItem.hasWeepingWllPower(event.getItemStack())) {
                    event.getToolTip().add(1, Component.literal(""));
                    event.getToolTip().add(1, Component.translatable("item.malstone.weeping_power")
                            .withStyle(Style.EMPTY.withColor(baseItem.color())));
                }else {
                    float sin = (float) Math.sin(Malstone.clientTime / 20f);
                    if (sin < 0) {
                        sin = -sin;
                    }
                    event.getToolTip().add(1, Component.literal(""));
                    event.getToolTip().add(1, Component.translatable("item.malstone.skill_use.text",
                                    baseItem.getWeepingPower(event.getItemStack())).
                            setStyle(Style.EMPTY.withColor(Light.ARGB.color(255, (int) (125 + 100* sin), 100, (int) (130 + 60* sin)))));
                }
            }
        }
    }
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void RenderTooltipEven4t(RenderTooltipEvent.Color tooltipEvent){
        ItemStack stack = tooltipEvent.getItemStack();
        if (stack.getItem() instanceof WhiteArrow whiteArrow) {
            tooltipEvent.setBorderStart(whiteArrow.color());
            tooltipEvent.setBorderEnd(Light.ARGB.color(255, 255, 100, 255));
        }
        if (stack.getItem() instanceof SoulSteel) {
            tooltipEvent.setBorderStart(Light.ARGB.color(255, 255, 243, 178));
            tooltipEvent.setBorderEnd(Light.ARGB.color(255, 147, 121, 224));
        }
        if (stack.getItem() instanceof Runes) {
            tooltipEvent.setBorderStart(Light.ARGB.color(255,210, 0, 203));
            tooltipEvent.setBorderEnd(Light.ARGB.color(255, 147, 121, 224));
        }
        if (stack.getItem() instanceof Twisted twisted) {
            tooltipEvent.setBorderStart(Light.ARGB.color(255, 125, 100, 130));
            tooltipEvent.setBorderEnd(Light.ARGB.color(255, 125, 100, 130));
        }
        if (stack.getItem() instanceof BaseItem item) {
            if (item.hasWeepingWllPower(tooltipEvent.getItemStack())) {
                float sin = (float) Math.sin(Malstone.clientTime / 20f);
                if (sin < 0) {
                    sin = -sin;
                }
                tooltipEvent.setBorderStart(Light.ARGB.color(255, (int) (125 + 60* sin), 100, (int) (130 + 60* sin)));
                tooltipEvent.setBorderEnd(Light.ARGB.color(255, (int) (125 + 60* sin), 100, (int) (130 + 60* sin)));

            }
        }
    }
}
