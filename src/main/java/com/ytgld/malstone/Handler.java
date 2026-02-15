package com.ytgld.malstone;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
}
