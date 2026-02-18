package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.core.handlers.SoulHarvestHandler;
import com.ytgld.malstone.items.rune.Hungrier;
import com.ytgld.malstone.items.soul.SoulDevice;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SoulHarvestHandler.class,remap = false)
public class SpiritHarvestHandlerMixin {
    @Inject(method = "pickupSpirit", at = @At(value = "RETURN"))
    private static void pickupSpirit$Malstone(LivingEntity collector, ItemStack stack, CallbackInfo ci) {
        SoulDevice.doubleSpirit(collector,stack);
        Hungrier.addHungrier(collector);
    }

}