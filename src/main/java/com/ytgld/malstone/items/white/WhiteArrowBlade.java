package com.ytgld.malstone.items.white;

import com.google.common.collect.Multimap;
import com.sammy.malum.registry.common.MalumAttributes;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class WhiteArrowBlade extends WhiteArrow {
    public WhiteArrowBlade(Properties properties) {
        super(properties);
    }

    public static void lLivingDamageEvent(LivingDamageEvent.Pre event){
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.WhiteArrowBlade_.get())) {
                if (!event.getSource().is(DamageTypes.MAGIC)) {
                    event.setNewDamage(event.getNewDamage() * damageAttack(player));
                }
            }
        }
    }
    public static boolean canSweep(LivingEntity attacker){
        if (Handler.hascurio(attacker, ItemRegs.WhiteArrowBlade_.get())) {
            return true;
        }
        return false;
    }


    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> get = super.getAttributeModifiers(slotContext, id, stack);
        get.put(MalumAttributes.SCYTHE_PROFICIENCY,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        get.put(MalumAttributes.ARCANE_RESONANCE,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return get;
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
//        tooltipComponents.add(Component.translatable("item.malstone.white_arrow_blade.text.1",damageMagic(null)*100f).setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.white_arrow_blade.text.2",damageAttack(null)*100f).setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.white_arrow_blade.text.3").setStyle(Style.EMPTY.withColor(color())));
    }

    public static float damageMagic(@Nullable LivingEntity entity){
        return (float)(double) Config.getWhiteArrowBladeMagic().get() * Handler.whiteArcaneHarmonics(entity);
    }
    public static float damageAttack(@Nullable LivingEntity entity){
        return  (float)(double) Config.getWhiteArrowBladeAttack().get() * Handler.whiteArcaneHarmonics(entity);

    }
}
