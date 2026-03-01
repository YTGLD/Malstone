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
    public static final RegistryObject<Attribute> SuperMalicious  =REGISTRY.register("super_malicious", ()->{
        return new RangedAttribute("attribute.name.malstone.super_malicious",
                0.0, 0.0, 1024.0).setSyncable(true);
    });
    public static final RegistryObject<Attribute> ToughBlood  =REGISTRY.register("tough_blood", ()->{
        return new RangedAttribute("attribute.name.malstone.tough_blood",
                0.0, 0.0, 1024.0).setSyncable(true);
    });
    public static final RegistryObject<Attribute> MagicRes  =REGISTRY.register("magic_res", ()->{
        return new RangedAttribute("attribute.name.malstone.magic_res",
                0.0, 0.0, 1024.0).setSyncable(true);
    });
    public static final RegistryObject<Attribute> DamageRes  =REGISTRY.register("damage_res", ()->{
        return new RangedAttribute("attribute.name.malstone.damage_res",
                0.0, 0.0, 1024.0).setSyncable(true);
    });
    public static final RegistryObject<Attribute> EatTime =REGISTRY.register("eat_speed", ()->{
        return new RangedAttribute("attribute.name.malstone.eat_speed",
                1, 0.0, 1024.0).setSyncable(true);
    });
    @SubscribeEvent
    public static void EntityAttributeCreationEvent(EntityAttributeModificationEvent event){
        event.add(EntityType.PLAYER , AttReg.ChaosErosion.get(),0);
        event.add(EntityType.PLAYER , AttReg.SuperMalicious.get(),0);
        event.add(EntityType.PLAYER , AttReg.ToughBlood.get(),0);
        event.add(EntityType.PLAYER , AttReg.MagicRes.get(),0);
        event.add(EntityType.PLAYER , AttReg.DamageRes.get(),0);
        event.add(EntityType.PLAYER , AttReg.EatTime.get(),1);
    }
}
