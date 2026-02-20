package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.common.data.attachment.SoulWardData;
import com.ytgld.malstone.items.rune.EternalFallenSoul;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SoulWardData.class)
public class SoulWardHandlerMixin {
    @Inject(method = "recoverSoulWard", at = @At(value = "RETURN"))
    private void recoverSoulWard(LivingEntity entity, double amount, CallbackInfo ci) {
        EternalFallenSoul.healSOUL_WARD_CAP(entity);
    }

}
