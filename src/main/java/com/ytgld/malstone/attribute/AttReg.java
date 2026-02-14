package com.ytgld.malstone.attribute;

import com.ytgld.malstone.Malstone;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Malstone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttReg {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Malstone.MODID);

    public static final RegistryObject<Attribute> ChaosErosion  =REGISTRY.register("chaos_erosion", ()->{
        return new RangedAttribute("attribute.name.malstone.chaos_erosion",
                0.0, 0.0, 1024.0).setSyncable(true);
    });

    @SubscribeEvent
    public static void EntityAttributeCreationEvent(EntityAttributeModificationEvent event){
        event.add(EntityType.PLAYER , AttReg.ChaosErosion.get(),0);
    }
}
