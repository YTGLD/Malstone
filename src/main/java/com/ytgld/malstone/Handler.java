package com.ytgld.malstone;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

public class Handler {
    public static boolean hascurio(LivingEntity entity, Item curio) {
        return CuriosApi.getCuriosInventory(entity).resolve().isPresent()
                && CuriosApi.getCuriosInventory(entity).resolve().get().isEquipped(curio);
    }
}
