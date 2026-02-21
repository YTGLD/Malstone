package com.ytgld.malstone.entity;

import com.sammy.malum.MalumMod;
import com.sammy.malum.common.entity.AscendingBlockEntity;
import com.sammy.malum.common.entity.spirit.SpiritItemEntity;
import com.ytgld.malstone.Malstone;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Entitys {
    public static final DeferredRegister<EntityType<?>> REGISTER =DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Malstone.MODID);
    public static final DeferredHolder<EntityType<?>, EntityType<BloodSpirit>> BLOOD_SPIRIT  =
            REGISTER.register("blood_spirit",
                    () -> EntityType.Builder.<BloodSpirit>of((e, w) ->
                            new BloodSpirit(w), MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(10)
                            .build(ResourceLocation.fromNamespaceAndPath(Malstone.MODID,"blood_spirit").toString()));


}
