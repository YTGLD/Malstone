package com.ytgld.malstone.items.init;

import com.ytgld.malstone.Light;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BaseItem extends Item implements ICurioItem {
    public BaseItem(Properties properties) {
        super(properties);
    }
    public boolean canUseSkill(){
        return false;
    }
    public int color(){
        return Light.ARGB.color(255, 255, 255, 0);
    }

}
