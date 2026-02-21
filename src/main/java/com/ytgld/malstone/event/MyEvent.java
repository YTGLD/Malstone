package com.ytgld.malstone.event;

import com.sammy.malum.common.data.attachment.SoulWardData;
import com.sammy.malum.registry.common.MalumAttachmentTypes;
import com.sammy.malum.registry.common.MalumAttributes;
import com.ytgld.malstone.Light;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.KillTheGods;
import com.ytgld.malstone.items.init.BaseItem;
import com.ytgld.malstone.items.init.Runes;
import com.ytgld.malstone.items.init.SoulSteel;
import com.ytgld.malstone.items.init.WhiteArrow;
import com.ytgld.malstone.items.rune.BladeOath;
import com.ytgld.malstone.items.rune.Martyrdom;
import com.ytgld.malstone.items.white.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class MyEvent {
    @SubscribeEvent
    public void LivingHealEvent(LivingHealEvent event){
        HugeSouls.lLivingHealEvent(event);
        BreakingTheLife.lLivingHealEvent(event);
        LivingEntity living = event.getEntity();
        if (living instanceof Player player) {
            AttributeInstance attribute = player.getAttribute(AttReg.ChaosErosion);
            AttributeInstance capacity = player.getAttribute(MalumAttributes.SOUL_WARD_CAPACITY);
            if (attribute != null && capacity!=null) {
                SoulWardData handler = player.getData(MalumAttachmentTypes.SOUL_WARD);
                float value = (float) attribute.getValue();
                boolean canHeal = Mth.nextInt(RandomSource.create(), 0, 100) <= (value * 100);
                if (canHeal) {
                    if (handler.getSoulWard() < capacity.getValue()) {
                        handler.recoverSoulWard(player,1);
                        event.setCanceled(true);
                    }
                }
            }
        }
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
    public void LivingDamageEvent(LivingDamageEvent.Pre event){
        hyperplasiaShield(event);
        BladeOath.doMaxScy(event);
        HugeSouls.lLivingDamageEvent(event);
        WhiteArrowBlade.lLivingDamageEvent(event);
        RingOfAuthority.attack(event);
        Martyrdom.attackPost(event);
        Martyrdom.attackPre(event);
        BreakingTheWeapon.attackADamage(event);
        KillTheGods.attackPost(event);
    }
    @SubscribeEvent
    public void tooltip(ItemTooltipEvent event){
        if (event.getItemStack().getItem() instanceof BaseItem baseItem){
            if (baseItem.canUseSkill()) {
                event.getToolTip().add(1, Component.literal(""));
                event.getToolTip().add(1, Component.translatable("item.malstone.skill_use", Keys.KEY_MAPPING_LAZY_R.getKey().getDisplayName())
                        .withStyle(Style.EMPTY.withColor(baseItem.color())));
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
    }
    public void hyperplasiaShield (LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player living) {
            AttributeInstance stronger = living.getAttribute(AttReg.BloodShieldStronger);
            if (stronger != null) {
                float value = (float) stronger.getValue();
                float data = living.getData(AttReg.BloodShield_);
                if (data > 0) {
                    float damage = event.getNewDamage();
                    int newData = (int) (data - 1 - ((int) (damage * 0.5f)));
                    living.setData(AttReg.BloodShield_,(float)newData);
                    float modify = (float) Math.sqrt(value);
                    if (modify < 0.3f) {
                        modify = 0.3f;
                    }
                    float newDamage = damage * (0.3f / modify);
                    event.setNewDamage(newDamage);
                } else {
                    living.setData(AttReg.BloodShield_, 0f);
                }
            }
        }
    }
}
