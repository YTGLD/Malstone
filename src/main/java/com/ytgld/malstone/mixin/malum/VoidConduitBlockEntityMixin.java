package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.common.block.curiosities.weeping_well.VoidConduitBlockEntity;
import com.ytgld.malstone.items.twisted.FallingWell;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VoidConduitBlockEntity.class)
public class VoidConduitBlockEntityMixin {
    @Inject(method = "spitOutItem", at = @At(value = "RETURN"))
    private void Malstone$spitOutItem(ItemStack stack, CallbackInfo ci) {
        FallingWell.eatAtWeepingWell(stack);
    }
}
