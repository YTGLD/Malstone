package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.common.block.curiosities.weeping_well.VoidConduitBlockEntity;
import com.ytgld.malstone.items.init.BaseItem;
import com.ytgld.malstone.magic.DataReg;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(VoidConduitBlockEntity.class)
public class VoidConduitBlockEntityMixin {
    @Shadow
    @Final
    public List<ItemStack> eatenItems;

    @Inject(method = "spitOutItem", at = @At(value = "RETURN"))
    private void Malstone$spitOutItem(ItemStack stack, CallbackInfo ci) {
        if (stack.getItem() instanceof BaseItem item) {
            item.eatAtWeepingWell(stack);
        }
    }
    @Inject(method = "serverTick", at = @At(value = "RETURN"))
    private void Malstone$eatGunk(ServerLevel level, CallbackInfo ci) {
        for (ItemStack stack : this.eatenItems) {
            if (stack.getItem() instanceof BaseItem item) {
                if (item.canUseWeepingPower()) {
                    if (stack.get(DataReg.tag) == null) {
                        stack.set(DataReg.tag, new CompoundTag());
                    }
                }
            }
        }
    }
}
