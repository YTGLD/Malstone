package com.ytgld.malstone.items.rune;

import com.sammy.malum.registry.common.SpiritTypeRegistry;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Runes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class Hungrier extends Runes {
    public Hungrier(Properties builder) {
        super(builder, SpiritTypeRegistry.EARTHEN_SPIRIT);
    }
    public static void addHungrier(LivingEntity entity){
        if (entity instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.Hungrier_.get())) {
                player.getFoodData().eat(1, 0.25f);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.malstone.hungrier.text").setStyle(Style.EMPTY.withColor(color())));
    }


    @Override
    public void addExtraTooltipLines(Consumer<Component> consumer) {
        consumer.accept(positiveEffect("hungrier"));
        consumer.accept(positiveEffect("hungrier.1"));
    }

}
