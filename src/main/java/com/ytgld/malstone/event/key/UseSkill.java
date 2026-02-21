package com.ytgld.malstone.event.key;

import com.ytgld.malstone.Malstone;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public  record UseSkill(ItemStack carried) implements CustomPacketPayload {

    public static final Type<UseSkill> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Malstone.MODID, "use_skill"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UseSkill> CHEST_CURIOS_STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.OPTIONAL_STREAM_CODEC,
                    UseSkill::carried,
                    UseSkill::new);

    @Nonnull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
