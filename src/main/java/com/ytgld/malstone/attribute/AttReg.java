package com.ytgld.malstone.attribute;

import com.mojang.serialization.Codec;
import com.ytgld.malstone.Malstone;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Malstone.MODID)
public class AttReg {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Malstone.MODID);
    public static final Supplier<AttachmentType<Float>> BloodShield_ = ATTACHMENT_TYPES.register(
            "blood_shield", () -> AttachmentType.builder(() -> 0f).sync(new SyncHandler())
                    .serialize(Codec.FLOAT.fieldOf("blood_shield").codec()).build()
    );

    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, Malstone.MODID);

    public static final DeferredHolder<Attribute, Attribute> ChaosErosion  =REGISTRY.register("chaos_erosion", ()->{
        return new RangedAttribute("attribute.name.malstone.chaos_erosion",
                0.0, 0.0, 1024.0).setSyncable(true);
    });
    public static final DeferredHolder<Attribute, Attribute> SuperMalicious  =REGISTRY.register("super_malicious", ()->{
        return new RangedAttribute("attribute.name.malstone.super_malicious",
                0.0, 0.0, 1024.0).setSyncable(true);
    });
    public static final DeferredHolder<Attribute, Attribute> MaxBloodShield  =REGISTRY.register("max_blood_shield", ()->{
        return new RangedAttribute("attribute.name.malstone.max_blood_shield",
                0.0, 0.0, 1024.0).setSyncable(true);
    });
    public static final DeferredHolder<Attribute, Attribute> BloodShieldStronger =REGISTRY.register("blood_shield_stronger", ()->{
        return new RangedAttribute("attribute.name.malstone.blood_shield_stronger",
                1.0, 0.0, 1024.0).setSyncable(true);
    });

    /**
     * DecayShield
     */
    public static final Supplier<AttachmentType<Float>> DecayShield = ATTACHMENT_TYPES.register(
            "decay_shield", () -> AttachmentType.builder(() -> 0f).sync(new SyncHandler())
                    .serialize(Codec.FLOAT.fieldOf("decay_shield").codec()).build()
    );


    public static final int maxDecayShield = 10;
    public static final DeferredHolder<Attribute, Attribute> MaxDecayShield =REGISTRY.register("max_decay_shield", ()->{
        return new RangedAttribute("attribute.name.malstone.max_decay_shield",
                0, 0, maxDecayShield).setSyncable(true);
    });
    public static final DeferredHolder<Attribute, Attribute> StrongerDecayShield =REGISTRY.register("decay_shield_stronger", ()->{
        return new RangedAttribute("attribute.name.malstone.decay_shield_stronger",
                1.0, 0.0, 1024.0).setSyncable(true);
    });
    public static final DeferredHolder<Attribute, Attribute> SpeedDecayShield =REGISTRY.register("decay_shield_speed", ()->{
        return new RangedAttribute("attribute.name.malstone.decay_shield_speed",
                1.0, 0.0, 1024.0).setSyncable(true);
    });

    @SubscribeEvent
    public static void EntityAttributeCreationEvent(EntityAttributeModificationEvent event){
        event.add(EntityType.PLAYER , AttReg.ChaosErosion,0);
        event.add(EntityType.PLAYER , AttReg.SuperMalicious,0);
        event.add(EntityType.PLAYER , AttReg.MaxBloodShield,0);
        event.add(EntityType.PLAYER , AttReg.BloodShieldStronger,1);
        //DecayShield
        event.add(EntityType.PLAYER , AttReg.MaxDecayShield,0);
        event.add(EntityType.PLAYER , AttReg.StrongerDecayShield,1);
        event.add(EntityType.PLAYER , AttReg.SpeedDecayShield,1);

    }
}
