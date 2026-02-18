package com.ytgld.malstone.attribute;

import com.ytgld.malstone.Malstone;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Malstone.MODID)
public class AttReg {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, Malstone.MODID);

    public static final DeferredHolder<Attribute, Attribute> ChaosErosion  =REGISTRY.register("chaos_erosion", ()->{
        return new RangedAttribute("attribute.name.malstone.chaos_erosion",
                0.0, 0.0, 1024.0).setSyncable(true);
    });
    public static final DeferredHolder<Attribute, Attribute> SuperMalicious  =REGISTRY.register("super_malicious", ()->{
        return new RangedAttribute("attribute.name.malstone.super_malicious",
                0.0, 0.0, 1024.0).setSyncable(true);
    });

    @SubscribeEvent
    public static void EntityAttributeCreationEvent(EntityAttributeModificationEvent event){
        event.add(EntityType.PLAYER , AttReg.ChaosErosion,0);
        event.add(EntityType.PLAYER , AttReg.SuperMalicious,0);
    }
}
