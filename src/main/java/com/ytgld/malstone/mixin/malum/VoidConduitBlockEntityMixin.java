package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.common.block.curiosities.weeping_well.VoidConduitBlockEntity;
import com.ytgld.malstone.items.init.BaseItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = VoidConduitBlockEntity.class,remap = false)
public class VoidConduitBlockEntityMixin {
    @Shadow
    @Final
    public List<ItemStack> eatenItems;

    @Inject(method = "spitOutItem", at = @At(value = "RETURN"))
    private void Malstone$spitOutItem(ItemStack stack, CallbackInfoReturnable<Item> cir) {
        if (stack.getItem() instanceof BaseItem item) {
            item.eatAtWeepingWell(stack);
        }
    }
    @Inject(method = "tick", at = @At(value = "RETURN"))
    private void Malstone$eatGunk(CallbackInfo ci) {
        for (ItemStack stack : this.eatenItems) {
            if (stack.getItem() instanceof BaseItem item) {
                if (item.canUseWeepingPower()) {
                    if (stack.getTag() == null   ){
                        stack.getOrCreateTag();
                    }
                }
            }
        }
    }
}
