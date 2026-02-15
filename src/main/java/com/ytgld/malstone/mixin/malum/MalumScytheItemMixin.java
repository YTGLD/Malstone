package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.common.item.curiosities.weapons.scythe.MalumScytheItem;
import com.ytgld.malstone.items.WhiteArrowBlade;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MalumScytheItem.class,remap = false)
public class MalumScytheItemMixin {
    @Inject(method = "canSweep", at = @At(value = "RETURN"), cancellable = true)
    private static void canSweep(LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (WhiteArrowBlade.canSweep(attacker)) {
            cir.setReturnValue(true);
        }
    }
}
