package com.ytgld.malstone.items.soul;

import com.google.common.collect.Multimap;
import com.sammy.malum.registry.common.MalumAttributes;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.SoulSteel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class SoulSteelComponents extends SoulSteel {
    public SoulSteelComponents(Properties properties) {
        super(properties);
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> get = super.getAttributeModifiers(slotContext, id, stack);
        get.put(MalumAttributes.SOUL_WARD_CAPACITY,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                6, AttributeModifier.Operation.ADD_VALUE));

        get.put(AttReg.ChaosErosion,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                0.3F, AttributeModifier.Operation.ADD_VALUE));
        return get;
    }

}
