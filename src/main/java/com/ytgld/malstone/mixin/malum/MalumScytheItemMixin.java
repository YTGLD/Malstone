package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.common.item.curiosities.weapons.scythe.MalumScytheItem;
import com.ytgld.malstone.items.rune.BladeOath;
import com.ytgld.malstone.items.white.WhiteArrowBlade;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MalumScytheItem.class,remap = false)
public class MalumScytheItemMixin {
    @Inject(method = "canSweep", at = @At(value = "RETURN"), cancellable = true)
    private static void canSweep(LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (WhiteArrowBlade.canSweep(attacker)) {
            cir.setReturnValue(true);
        }
    }
    @Inject(method = "outgoingDamageEvent", at = @At(value = "RETURN"))
    private void Malstone$hurtEvent(LivingDamageEvent.Pre event, LivingEntity attacker, LivingEntity target, ItemStack stack, CallbackInfo ci) {
        BladeOath.doMaxScy(event, attacker, target,stack);
    }
}
