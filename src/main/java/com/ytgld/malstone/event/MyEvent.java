package com.ytgld.malstone.event;

import com.sammy.malum.common.data.attachment.SoulWardData;
import com.sammy.malum.registry.common.MalumAttachmentTypes;
import com.sammy.malum.registry.common.MalumAttributes;
import com.sammy.malum.registry.common.MalumSoundEvents;
import com.ytgld.malstone.Light;
import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.KillTheGods;
import com.ytgld.malstone.items.init.*;
import com.ytgld.malstone.items.rune.BladeOath;
import com.ytgld.malstone.items.rune.Martyrdom;
import com.ytgld.malstone.items.twisted.ExtremelyDead;
import com.ytgld.malstone.items.twisted.FallingWell;
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
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.helpers.SoundHelper;

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
    public void TickEvt(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            tickShield(player);
        }
    }
    @SubscribeEvent
    public void LeftClickEmpty(LivingDeathEvent event) {
        ExtremelyDead.killBlack(event);
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
        theDecayShield(event);
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
            if (baseItem.canUseWeepingPower()) {
                if (!baseItem.hasWeepingWllPower(event.getItemStack())) {
                    event.getToolTip().add(1, Component.literal(""));
                    event.getToolTip().add(1, Component.translatable("item.malstone.weeping_power", Keys.KEY_MAPPING_LAZY_R.getKey().getDisplayName())
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


    public void theDecayShield (LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player living) {
            AttributeInstance stronger = living.getAttribute(AttReg.MaxDecayShield);
            float base = 0.5f;
            float minDamage = 0.3f;
            if (stronger != null) {
                float value = (float) stronger.getValue();
                float data = living.getData(AttReg.DecayShield);
                if (data > 0) {
                    float damage = event.getNewDamage();
                    int newData = (int) (data - 1 - ((int) (damage * 0.5f)));
                    living.setData(AttReg.DecayShield,(float)newData);
                    float modify = (float) Math.sqrt(value);
                    if (modify < minDamage) {
                        modify = minDamage;
                    }
                    float newDamage = damage * (base / modify);
                    event.setNewDamage(newDamage);
                    SoundHelper.playSound(living, MalumSoundEvents.SOUL_WARD_HIT.get(), 0.8F, RandomHelper.randomBetween(living.getRandom(), 1, 1));
                } else {
                    living.setData(AttReg.DecayShield, 0f);
                }
            }
        }
    }
    public void tickShield(LivingEntity living){
        AttributeInstance maxShield = living.getAttribute(AttReg.MaxDecayShield);
        AttributeInstance speed = living.getAttribute(AttReg.SpeedDecayShield);

        if (maxShield != null && speed != null) {
            float time = (float) (10 * speed.getValue());
            if (time < 1) {
                time = 1;
            }

            float value = (float) maxShield.getValue();
            float data = living.getData(AttReg.DecayShield);

            if (living.tickCount % (time * 10) == 1) {
                if (data < value) {
                    living.setData(AttReg.DecayShield, data + 1);
                    SoundHelper.playSound(living, MalumSoundEvents.VOID_HEARTBEAT.get(), 0.8F, RandomHelper.randomBetween(living.getRandom(), 1, 1));
                }
            }
            if (data < 0) {
                living.setData(AttReg.DecayShield, 0f);
            }
        }
    }
}
