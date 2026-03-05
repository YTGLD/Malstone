package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.client.screen.codex.screens.progression.ArcanaProgressionScreen;
import com.sammy.malum.core.handlers.GeasEffectHandler;
import com.sammy.malum.core.systems.geas.GeasEffectType;
import com.ytgld.malstone.client.CommonEntries;
import com.ytgld.malstone.items.twisted.ChessFell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GeasEffectHandler.class)
public class GeasEffectHandlerMixin {
    @Inject(method = "addGeasEffect", at = @At(value = "RETURN" ))
    private static void addGeasEffect(LivingEntity entity, GeasEffectType geas, CallbackInfoReturnable<Boolean> cir) {
        if ( entity instanceof Player player) {
            ChessFell.add(player);
        }
    }
}
