package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.core.handlers.WeepingWellRejectionHandler;
import com.ytgld.malstone.items.init.BaseItem;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WeepingWellRejectionHandler.class)
public class WeepingWellRejectionHandlerMixin {
    @Inject(method = "launchPlayer", at = @At(value = "RETURN"))
    private static void recoverSoulWard(Player player, CallbackInfo ci) {
        BaseItem.eatOfPlayer(player);
    }
}
