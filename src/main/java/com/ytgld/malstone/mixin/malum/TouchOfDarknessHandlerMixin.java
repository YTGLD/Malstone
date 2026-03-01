package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.core.handlers.TouchOfDarknessHandler;
import com.ytgld.malstone.items.init.BaseItem;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TouchOfDarknessHandler.class,remap = false)
public class TouchOfDarknessHandlerMixin {
    @Inject(method = "reject", at = @At(value = "RETURN"))
    private void recoverSoulWard(LivingEntity livingEntity, CallbackInfo ci) {
        BaseItem.eatOfPlayer(livingEntity);
    }
}
