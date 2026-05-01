package com.ytgld.malstone.items.white;

import com.google.common.collect.Multimap;
import com.sammy.malum.registry.common.AttributeRegistry;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class WhiteArrowBlade extends WhiteArrow {
    public WhiteArrowBlade(Properties properties) {
        super(properties);
    }

    public static void lLivingDamageEvent(LivingDamageEvent event){
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.WhiteArrowBlade_.get())) {
                if (!event.getSource().is(DamageTypes.MAGIC)) {
                    event.setAmount(event.getAmount() * damageAttack(player));
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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> get = super.getAttributeModifiers(slotContext, uuid, stack);
        get.put(AttributeRegistry.SCYTHE_PROFICIENCY.get(),new AttributeModifier(uuid,this.getDescriptionId(),
                0.25, AttributeModifier.Operation.MULTIPLY_BASE));

        get.put(AttributeRegistry.ARCANE_RESONANCE.get(),new AttributeModifier(uuid,this.getDescriptionId(),
                -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return get;
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
//        components.add(Component.translatable("item.malstone.white_arrow_blade.text.1",damageMagic(null)*100f).setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.white_arrow_blade.text.2",damageAttack(null)*100f).setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.white_arrow_blade.text.3").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.literal(""));
        components.add(Component.translatable("item.malstone.white_arrow").setStyle(Style.EMPTY.withColor(color())));
    }
    public static float damageAttack(@Nullable LivingEntity entity){
        return Handler.doWhiteArcaneHarmonics(entity, Config.getWhiteArrowBladeAttack().get().floatValue());

    }
}
