package com.ytgld.malstone.effects;

import com.ytgld.malstone.Malstone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FallCurse extends MobEffect {
    protected FallCurse() {
        super(MobEffectCategory.BENEFICIAL, 0XFFFF00FF);
        this.addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.fromNamespaceAndPath(Malstone.MODID,"fall_curse"),-0.2f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
