package com.ytgld.malstone;

import com.sammy.malum.registry.common.AttributeRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

public class Handler {
    public static boolean hascurio(LivingEntity entity, Item curio) {
        return CuriosApi.getCuriosInventory(entity).resolve().isPresent()
                && CuriosApi.getCuriosInventory(entity).resolve().get().isEquipped(curio);
    }
    public static boolean isEq(LivingEntity living , EquipmentSlot slot, Item item){
        if (living!=null) {
            if (living.getItemBySlot(slot).is(item)) {
                return true;
            }
        }
        return false;
    }
    public static float doArcaneHarmonics(LivingEntity entity,float dif){
        if (entity != null) {
            return getArcaneHarmonics(entity) * dif;
        }
        return dif;
    }

    private static float getArcaneHarmonics(LivingEntity entity){
        if (entity != null) {
            AttributeInstance attributeInstance =  entity.getAttribute(AttributeRegistry.ARCANE_RESONANCE.get());
            if (attributeInstance != null) {
                return (float) attributeInstance.getValue() * Config.getArcaneHarmonics().get().floatValue();
            }
        }else {
            return 1;
        }
        return 1;
    }
    public static float doWhiteArcaneHarmonics(LivingEntity entity,float dif){
        if (entity != null) {
            return whiteArcaneHarmonics(entity) * dif;
        }
        return dif;
    }

    private static float whiteArcaneHarmonics(LivingEntity entity){
        if (entity != null) {
            AttributeInstance attributeInstance =  entity.getAttribute(AttributeRegistry.ARCANE_RESONANCE.get());
            if (attributeInstance != null) {
                float s = (float) attributeInstance.getValue();
                float doIt = 1 - s;
                if (doIt < 0.5f) {
                    doIt = 0.5f;
                }
                return 1 + doIt * Config.getArcaneHarmonics().get().floatValue();
            }
        }else {
            return 1;
        }
        return 1;
    }
}
