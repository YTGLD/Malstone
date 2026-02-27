package com.ytgld.malstone.items.twisted;

import com.sammy.malum.common.entity.spirit.SpiritItemEntity;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Twisted;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 邪祟引擎
 * <p>
 * 收获的精魂会被销毁
 * <p>
 * 每次销毁精魂都会恢复生命
 * <p>
 * 若无需治疗，则恢复饥饿值和饱和度
 * <p>
 * 若无需进食，则获得经验值
 */
public class EvilEngine extends Twisted {
    public EvilEngine(Properties properties) {
        super(properties);
    }

    public static void killThis(LivingEntity player , ItemStack spiritItemEntity , CallbackInfo ci){
        if (Handler.hascurio(player, ItemRegs.EvilEngine_.get())) {
            if (player.getHealth() < player.getMaxHealth()) {
                heal(player);
            }else if (needFood(player)){
                east(player);
            }else {
                xp(player);
            }
            spiritItemEntity.shrink(1);
            ci.cancel();
        }
    }
    private static boolean needFood(LivingEntity player){
        if (player instanceof Player p) {
            return p.getFoodData().needsFood();
        }
        return false;
    }
    private static void heal(LivingEntity player){
        if (Handler.hascurio(player, ItemRegs.EvilEngine_.get())) {
            player.heal(4);
        }
    }
    private static void xp(LivingEntity player){
        if (Handler.hascurio(player, ItemRegs.EvilEngine_.get())) {
            if (player instanceof Player p) {
                p.giveExperiencePoints(3);
            }
        }
    }
    private static void east(LivingEntity player){
        if (Handler.hascurio(player, ItemRegs.EvilEngine_.get())) {
            if (player instanceof Player p) {
                p.getFoodData().eat(1,0.5f);
            }
        }
    }
    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {

        tooltipComponents.add(Component.translatable("item.malstone.evil_engine.text.1").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.evil_engine.text.2").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.evil_engine.text.3").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.evil_engine.text.4").setStyle(Style.EMPTY.withColor(color())));
        return new MalstoneText(stack,tooltipComponents);
    }
}
