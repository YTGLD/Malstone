package com.ytgld.malstone;

import com.sammy.malum.client.screen.codex.screens.ArcanaProgressionScreen;
import com.sammy.malum.common.capability.MalumPlayerDataCapability;
import com.sammy.malum.common.events.SetupMalumCodexEntriesEvent;
import com.sammy.malum.core.handlers.SoulWardHandler;
import com.sammy.malum.registry.common.AttributeRegistry;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.client.WhiteArrowEntries;
import com.ytgld.malstone.items.BreakingTheLife;
import com.ytgld.malstone.items.HugeSouls;
import com.ytgld.malstone.items.WhiteArrowBlade;
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
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void RenderTooltipEven4t(RenderTooltipEvent.Color tooltipEvent){
        ItemStack stack = tooltipEvent.getItemStack();
        if (stack.getItem() instanceof WhiteArrow whiteArrow) {
            tooltipEvent.setBorderStart(whiteArrow.color());
            tooltipEvent.setBorderEnd(Light.ARGB.color(255, 255, 100, 255));
        }
    }

//    @SubscribeEvent
//    public void SetupMalumCodexEntriesEvent(SetupMalumCodexEntriesEvent event){
//        ArcanaProgressionScreen arcanaProgressionScreen = ArcanaProgressionScreen.getScreenInstance();
//        WhiteArrowEntries.setupEntries(arcanaProgressionScreen);
//
//    }
}
