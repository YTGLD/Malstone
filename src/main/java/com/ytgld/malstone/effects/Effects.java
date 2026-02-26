package com.ytgld.malstone.effects;

import com.ytgld.malstone.Malstone;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Effects {
    public static final DeferredRegister<MobEffect> REGISTER = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Malstone.MODID);
    public static final RegistryObject<MobEffect> fFallCurse =
            REGISTER.register("fall_curse", FallCurse::new);
}
