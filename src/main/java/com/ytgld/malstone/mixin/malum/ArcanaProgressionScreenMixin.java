package com.ytgld.malstone.mixin.malum;


import com.sammy.malum.client.screen.codex.screens.progression.ArcanaProgressionScreen;
import com.ytgld.malstone.client.CommonEntries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ArcanaProgressionScreen.class)
public class ArcanaProgressionScreenMixin {
    @Inject(method = "setupEntries", at = @At(value = "RETURN" ))
    private void canSweep(CallbackInfo ci) {
        ArcanaProgressionScreen arcanaProgressionScreen =  (ArcanaProgressionScreen)(Object)this;
        CommonEntries.setupEntries(arcanaProgressionScreen);
    }
}
