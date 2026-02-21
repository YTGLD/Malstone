package com.ytgld.malstone.event.key;

import com.ytgld.malstone.items.KillTheGods;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class UseSkillHandler {
    public static void register(final PayloadRegistrar registrar) {
        registrar.playToServer(UseSkill.TYPE, UseSkill.CHEST_CURIOS_STREAM_CODEC,
                handlerUse::handleOpenCurios);

    }
    public static HandlerUse handlerUse = new HandlerUse();
    public static class HandlerUse {
        public void handleOpenCurios(final UseSkill data, final IPayloadContext ctx) {
            ctx.enqueueWork(() -> {
                Player player = ctx.player();
                KillTheGods.use(player);

            });
        }
    }
}
