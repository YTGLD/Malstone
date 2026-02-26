package com.ytgld.malstone.effects;

import com.ytgld.malstone.Malstone;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Effects {
    public static final DeferredRegister<MobEffect> REGISTER =DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Malstone.MODID);
    public static final DeferredHolder<MobEffect,MobEffect> fFallCurse =
            REGISTER.register("fall_curse", FallCurse::new);
}
