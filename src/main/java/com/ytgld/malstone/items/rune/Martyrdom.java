package com.ytgld.malstone.items.rune;

import com.sammy.malum.registry.common.magic.MalumSpiritTypes;
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
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.List;

/**
 * 殉锋符文
 * <p>
 * 轻击造成的伤害为上一次全力造成伤害的%d%%
 */
public class Martyrdom extends Runes {
    public Martyrdom(Properties builder) {
        super(builder, MalumSpiritTypes.UMBRAL_SPIRIT, MalumTrinketType.RUNE);
    }
    public static final String attackLast = "attackLastMartyrdom";
    public static final String attackDouble = "attackDoubleMartyrdom";



    public static void attackPre(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.Martyrdom_.get())) {
                float f2 = player.getAttackStrengthScale(0.5F);
                if (f2 >= 1f) {
                    CompoundTag compoundTag = player.getPersistentData();
                    compoundTag.putFloat(attackLast, event.getNewDamage() * damageP());
                    if (Mth.nextInt(RandomSource.create(), 1, 100) < 70) {
                        compoundTag.putInt(attackDouble,doubleAttack());
                    }
                }
            }
        }
    }


    public static void attackPost(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.Martyrdom_.get())) {
                float f2 = player.getAttackStrengthScale(0.5F);
                if (f2 < 1.0F) {
                    CompoundTag compoundTag = player.getPersistentData();
                    float damage = compoundTag.getFloat(attackLast);
                    if (compoundTag.getInt(attackDouble) <= 0) {
                        event.setNewDamage(damage);
                        compoundTag.remove(attackLast);
                    }else {
                        event.setNewDamage(damage);
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.malstone.martyrdom.text",damageP() * 100f).setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.martyrdom.text.1").setStyle(Style.EMPTY.withColor(color())));
    }

}
