package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.core.handlers.SoulWardHandler;
import com.ytgld.malstone.items.rune.EternalFallenSoul;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SoulWardHandler.class,remap = false)
public class SoulWardHandlerMixin {
    @Inject(method = "recoverSoulWard", at = @At(value = "RETURN"))
    private void recoverSoulWard(Player player, CallbackInfo ci) {
        EternalFallenSoul.healSOUL_WARD_CAP(player);
    }

}
