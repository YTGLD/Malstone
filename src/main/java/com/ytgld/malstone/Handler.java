package com.ytgld.malstone;

import com.sammy.malum.registry.common.MalumAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

public class Handler {
    public static boolean hascurio(LivingEntity entity, Item curio) {
        return CuriosApi.getCuriosInventory(entity).isPresent()
                && CuriosApi.getCuriosInventory(entity).get().isEquipped(curio);
    }
    public static boolean isEq(LivingEntity living , EquipmentSlot slot, Item item){
        if (living!=null) {
            if (living.getItemBySlot(slot).is(item)) {
                return true;
            }
        }
        return false;
    }
    public static float getArcaneHarmonics(LivingEntity entity){
        if (entity != null) {
            AttributeInstance attributeInstance =  entity.getAttribute(MalumAttributes.ARCANE_RESONANCE);
            if (attributeInstance != null) {
                return (float) attributeInstance.getValue() * Config.getArcaneHarmonics().get().floatValue();
            }
        }else {
            return 1;
        }
        return 1;
    }

    public static float whiteArcaneHarmonics(LivingEntity entity){
        if (entity != null) {
            AttributeInstance attributeInstance =  entity.getAttribute(MalumAttributes.ARCANE_RESONANCE);
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
