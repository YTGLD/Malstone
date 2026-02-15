package com.ytgld.malstone.items;

import com.google.common.collect.Multimap;
import com.sammy.malum.registry.common.AttributeRegistry;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class HugeSouls extends WhiteArrow {
    public HugeSouls(Properties properties) {
        super(properties);
    }
    public static void lLivingHealEvent(LivingHealEvent event){
        if (event.getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.HugeSouls_.get())) {
                event.setAmount(event.getAmount() * 1.15f);
            }
        }
    }
    public static void lLivingDamageEvent(LivingDamageEvent event){
        if (event.getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.HugeSouls_.get())) {
                if (event.getSource().is(DamageTypes.MAGIC)) {
                    event.setAmount(event.getAmount() * 0.85f);
                }
            }
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.malstone.huge_soul.text.1").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.huge_soul.text.2").setStyle(Style.EMPTY.withColor(color())));

    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> get = super.getAttributeModifiers(slotContext, uuid, stack);
        get.put(AttReg.ChaosErosion.get(),new AttributeModifier(uuid,this.getDescriptionId(),0.25f, AttributeModifier.Operation.ADDITION));
        get.put(AttributeRegistry.SOUL_WARD_CAP.get(),new AttributeModifier(uuid,this.getDescriptionId(),4, AttributeModifier.Operation.ADDITION));
        return get;
    }
}
