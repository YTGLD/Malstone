package com.ytgld.malstone.items.twisted;

import com.google.common.collect.Multimap;
import com.mojang.serialization.Lifecycle;
import com.sammy.malum.core.handlers.GeasEffectHandler;
import com.sammy.malum.core.systems.geas.GeasEffectType;
import com.sammy.malum.registry.common.MalumAttributes;
import com.sammy.malum.registry.common.magic.MalumGeasEffectTypes;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Twisted;
import com.ytgld.malstone.magic.DataReg;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

/**
 * 棋陨
 * <p>
 * 死亡时遗失所有存在的誓令
 * <p>
 * 死亡时永久降低5%的最大生命值，直到最后50%
 */
public class ChessFell extends Twisted {
    public ChessFell(Properties properties) {
        super(properties);
    }
    public static final String lost = "lostChessFell";
    public static void drop(LivingDropsEvent event){
        if (event.getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.ChessFell_.get())) {
                for (GeasEffectType effectType : new MappedRegistry<>(MalumGeasEffectTypes.GEAS_TYPES_KEY, Lifecycle.stable(), true)) {
                    GeasEffectHandler.removeGeasEffect(player, effectType);
                }
                CompoundTag compoundTag = player.getPersistentData();
                if (compoundTag.getInt(lost) < getLostMaxHealth()) {
                    compoundTag.putInt(lost, compoundTag.getInt(lost) + 1);
                }
            }
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        CompoundTag stackTag = stack.get(DataReg.tag);


        if (slotContext.entity() instanceof Player player) {
            CompoundTag compoundTag  =player.getPersistentData();
            if (stackTag != null) {
                if (stackTag.getInt(lost) != compoundTag.getInt(lost)) {
                    stackTag.putInt(lost, compoundTag.getInt(lost));
                }
            }else {
                stack.set(DataReg.tag,new CompoundTag());
            }
            if (compoundTag.getInt(lost)  > getLostMaxHealth()) {
                compoundTag.putInt(lost, getLostMaxHealth());
            }
        }
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        CompoundTag compoundTag = slotContext.entity().getPersistentData();
        return compoundTag.getInt(lost) <= 0;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> get = super.getAttributeModifiers(slotContext, id, stack);
        get.put(MalumAttributes.GEAS_LIMIT,new AttributeModifier(id,
                2, AttributeModifier.Operation.ADD_VALUE));
        float lostLast = 0;
        if (slotContext.entity() instanceof Player player) {
            CompoundTag compoundTag  =player.getPersistentData();
            int number = compoundTag.getInt(lost);
            float pain = 0.05f;
            if (this.hasWeepingWllPower(stack)) {
                pain /= 2f;
            }
            lostLast = pain * number;
        }
        get.put(Attributes.MAX_HEALTH,new AttributeModifier(id,
                -lostLast, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return get;
    }

    public static int getLostMaxHealth(){
        return Config.getLostChessFell().hashCode();
    }

    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {
        CompoundTag stackTag = stack.get(DataReg.tag);
        if (stackTag == null) {
            tooltipComponents.add(Component.translatable("item.malstone.chess_fell.text.3").setStyle(Style.EMPTY.withColor(color())));
        }else if (stackTag.getInt(lost) <= 0){
            tooltipComponents.add(Component.translatable("item.malstone.chess_fell.text.3").setStyle(Style.EMPTY.withColor(color())));
        }else {
            tooltipComponents.add(Component.translatable("item.malstone.chess_fell.text.4").setStyle(Style.EMPTY.withColor(color())));
        }
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.translatable("item.malstone.chess_fell.text.1").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.chess_fell.text.2").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.literal(""));
        if (this.hasWeepingWllPower(stack)) {
            tooltipComponents.add(Component.translatable("item.malstone.chess_fell.text.5").setStyle(Style.EMPTY.withColor(color())));
        }
        return new MalstoneText(stack,tooltipComponents);
    }

    @Override
    public boolean canUseWeepingPower() {
        return true;
    }

    @Override
    public int maxWeepingPower(ItemStack stack) {
        return 1800;
    }
}
