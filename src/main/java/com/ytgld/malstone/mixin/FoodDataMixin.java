package com.ytgld.malstone.mixin;

import com.ytgld.malstone.items.BreakingTheLife;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @Shadow
    private int tickTimer;

    @Inject(method = "tick", at = @At(value = "RETURN"), cancellable = true)
    private void isInvulnerableToBase(Player player, CallbackInfo ci) {
        if (BreakingTheLife.food(player)) {
            tickTimer = 0;
        }
    }
}
