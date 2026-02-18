package com.ytgld.malstone.items.rune;

import com.sammy.malum.registry.common.magic.MalumSpiritTypes;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Runes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Consumer;

import static com.sammy.malum.core.helpers.ComponentHelper.positiveEffect;

public class Hungrier extends Runes {
    public Hungrier(Properties builder) {
        super(builder, MalumSpiritTypes.UMBRAL_SPIRIT, MalumTrinketType.RUNE);
    }
    public static void addHungrier(LivingEntity entity){
        if (entity instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.Hungrier_.get())) {
                player.getFoodData().eat(1, 0.25f);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.malstone.hungrier.text").setStyle(Style.EMPTY.withColor(color())));
    }


    @Override
    public void addExtraTooltipLines(Consumer<Component> consumer) {
        consumer.accept(positiveEffect("hungrier"));
        consumer.accept(positiveEffect("hungrier.1"));
    }

}
