package com.ytgld.malstone.items.soul;

import com.google.common.collect.Multimap;
import com.sammy.malum.registry.common.AttributeRegistry;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.SoulSteel;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class SoulSteelComponents extends SoulSteel {
    public SoulSteelComponents(Properties properties) {
        super(properties);
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> get = super.getAttributeModifiers(slotContext, uuid, stack);
        get.put(AttributeRegistry.SOUL_WARD_CAP.get(),new AttributeModifier(uuid,this.getDescriptionId(),
                6, AttributeModifier.Operation.ADDITION));

        get.put(AttReg.ChaosErosion.get(),new AttributeModifier(uuid,this.getDescriptionId(),
                0.3F, AttributeModifier.Operation.ADDITION));
        return get;
    }

}
