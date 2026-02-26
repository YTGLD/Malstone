package com.ytgld.malstone.effects;

import com.ytgld.malstone.Malstone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class FallCurse extends MobEffect {
    protected FallCurse() {
        super(MobEffectCategory.BENEFICIAL, 0XFFFF00FF);
        this.addAttributeModifier(Attributes.MAX_HEALTH,
                "6ae8fe40-3ccb-3755-8fd2-c5445728b386",-0.2f, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
