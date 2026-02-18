package com.ytgld.malstone.items.rune;

import com.sammy.malum.registry.common.SpiritTypeRegistry;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Runes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * 殉锋符文
 * <p>
 * 轻击造成的伤害为上一次全力造成伤害的%d%%
 */
public class Martyrdom extends Runes {
    public Martyrdom(Properties builder) {
        super(builder, SpiritTypeRegistry.INFERNAL_SPIRIT);
    }
    public static final String attackLast = "attackLastMartyrdom";
    public static final String attackDouble = "attackDoubleMartyrdom";



    public static void attackPre(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.Martyrdom_.get())) {
                float f2 = player.getAttackStrengthScale(0.5F);
                if (f2 >= 1f) {
                    CompoundTag compoundTag = player.getPersistentData();
                    compoundTag.putFloat(attackLast, event.getAmount() * damageP());
                    if (Mth.nextInt(RandomSource.create(), 1, 100) < 70) {
                        compoundTag.putInt(attackDouble,doubleAttack());
                    }
                }
            }
        }
    }


    public static void attackPost(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.Martyrdom_.get())) {
                float f2 = player.getAttackStrengthScale(0.5F);
                if (f2 < 1.0F) {
                    CompoundTag compoundTag = player.getPersistentData();
                    float damage = compoundTag.getFloat(attackLast);
                    if (compoundTag.getInt(attackDouble) <= 0) {
                        event.setAmount(damage);
                        compoundTag.remove(attackLast);
                    }else {
                        event.setAmount(damage);
                        compoundTag.putInt(attackDouble,compoundTag.getInt(attackDouble) - 1);
                        compoundTag.remove(attackLast);
                    }
                }
            }
        }
    }
    public static float damageP(){
        return Config.getmMartyrdomDamage().get().floatValue();
    }
    public static int doubleAttack(){
        return Config.getAttackDoubleMartyrdom().get();
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.malstone.martyrdom.text",damageP() * 100f).setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.martyrdom.text.1").setStyle(Style.EMPTY.withColor(color())));
    }

}
