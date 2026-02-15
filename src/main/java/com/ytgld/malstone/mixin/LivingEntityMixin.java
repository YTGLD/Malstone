package com.ytgld.malstone.mixin;

import com.ytgld.malstone.attribute.AttReg;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "getArmorValue", at = @At(value = "RETURN"), cancellable = true)
    private void getAttributeValue(CallbackInfoReturnable<Integer> cir) {
        LivingEntity living =  (LivingEntity)(Object)this;
        List<Float> floats  = new ArrayList<>();
        if (living instanceof Player player) {
            for (AttributeInstance attributeInstance : player.getAttributes().getSyncableAttributes()){
                for (AttributeModifier attributeModifier : attributeInstance.getModifiers()){
                    if (attributeModifier != null) {
                        if (attributeModifier.getOperation() == AttributeModifier.Operation.ADDITION) {
                            if (attributeInstance.getAttribute() ==Attributes.ARMOR) {
                                floats.add((float) attributeModifier.getAmount());
                            }
                        }
                    }
                }
            }
            int armor = 0;
            for (Float fl : floats){
                armor+=fl;
            }
            AttributeInstance attribute = player.getAttribute(AttReg.SuperMalicious.get());
            if (attribute != null) {
                float value = (float) attribute.getValue();
                cir.setReturnValue((int) (cir.getReturnValue() + (value * armor)));
            }
        }
    }
}
