package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.client.screen.codex.screens.VoidProgressionScreen;
import com.ytgld.malstone.client.WhiteArrowEntries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VoidProgressionScreen.class,remap = false)
public class VoidProgressionScreenMixin {
    @Inject(method = "setupEntries", at = @At(value = "RETURN" ))
    private void canSweep(CallbackInfo ci) {
        VoidProgressionScreen arcanaProgressionScreen =  (VoidProgressionScreen)(Object)this;
        WhiteArrowEntries.setupEntries(arcanaProgressionScreen);
    }
}
