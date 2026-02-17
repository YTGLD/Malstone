package com.ytgld.malstone;

import com.sammy.malum.common.capability.MalumPlayerDataCapability;
import com.sammy.malum.core.handlers.SoulWardHandler;
import com.sammy.malum.registry.common.AttributeRegistry;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.Runes;
import com.ytgld.malstone.items.init.SoulSteel;
import com.ytgld.malstone.items.white.BreakingTheLife;
import com.ytgld.malstone.items.white.HugeSouls;
import com.ytgld.malstone.items.white.RingOfAuthority;
import com.ytgld.malstone.items.white.WhiteArrowBlade;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MyEvent {
    @SubscribeEvent
    public void LivingHealEvent(LivingHealEvent event){
        HugeSouls.lLivingHealEvent(event);
        BreakingTheLife.lLivingHealEvent(event);
        LivingEntity living = event.getEntity();
        if (living instanceof Player player) {
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
    public void LivingDamageEvent(LivingDamageEvent event){
        HugeSouls.lLivingDamageEvent(event);
        WhiteArrowBlade.lLivingDamageEvent(event);
        RingOfAuthority.attack(event);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void RenderTooltipEven4t(RenderTooltipEvent.Color tooltipEvent){
        ItemStack stack = tooltipEvent.getItemStack();
        if (stack.getItem() instanceof WhiteArrow whiteArrow) {
            tooltipEvent.setBorderStart(whiteArrow.color());
            tooltipEvent.setBorderEnd(Light.ARGB.color(255, 255, 100, 255));
        }
        if (stack.getItem() instanceof SoulSteel soulSteel) {
            tooltipEvent.setBorderStart(Light.ARGB.color(255, 255, 243, 178));
            tooltipEvent.setBorderEnd(Light.ARGB.color(255, 147, 121, 224));
        }
        if (stack.getItem() instanceof Runes runes) {
            tooltipEvent.setBorderStart(Light.ARGB.color(255,210, 0, 203));
            tooltipEvent.setBorderEnd(Light.ARGB.color(255, 147, 121, 224));
        }
    }

//    @SubscribeEvent
//    public void SetupMalumCodexEntriesEvent(SetupMalumCodexEntriesEvent event){
//        ArcanaProgressionScreen arcanaProgressionScreen = ArcanaProgressionScreen.getScreenInstance();
//        WhiteArrowEntries.setupEntries(arcanaProgressionScreen);
//
//    }
}
