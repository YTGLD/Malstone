package com.ytgld.malstone.items.twisted;

import com.google.common.collect.Multimap;
import com.sammy.malum.common.data.attachment.soul_data.GeasSoulData;
import com.sammy.malum.core.handlers.GeasEffectHandler;
import com.sammy.malum.registry.common.MalumAttachmentTypes;
import com.sammy.malum.registry.common.MalumAttributes;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Map;

/**
 * 棋陨
 * <p>
 *     +2 最大誓令存在
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
    public static void add(Player player){
        if (Handler.hascurio(player, ItemRegs.ChessFell_.get())) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                Map<String, ICurioStacksHandler> curios = handler.getCurios();
                for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                    ICurioStacksHandler stacksHandler = entry.getValue();
                    IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                    for (int i = 0; i < stacksHandler.getSlots(); i++) {
                        ItemStack stack = stackHandler.getStackInSlot(i);
                        if (stack.is(ItemRegs.ChessFell_.get())) {
                            CompoundTag compoundTag = stack.get(DataReg.tag);
                            if (compoundTag != null) {
                                if (compoundTag.getInt(lost) > 0) {
                                    compoundTag.putInt(lost, compoundTag.getInt(lost) - 1);
                                }
                            }
                        }
                    }
                }
            });
        }
    }
    public static void drop(LivingDeathEvent event){
        if (event.getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.ChessFell_.get())) {
                CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                    Map<String, ICurioStacksHandler> curios = handler.getCurios();
                    for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                        ICurioStacksHandler stacksHandler = entry.getValue();
                        IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                        for (int i = 0; i < stacksHandler.getSlots(); i++) {
                            ItemStack stack = stackHandler.getStackInSlot(i);
                            player.setData(MalumAttachmentTypes.GEAS_SOUL_INFO, new GeasSoulData());
                            if (stack.is(ItemRegs.ChessFell_.get())) {
                                CompoundTag compoundTag = stack.get(DataReg.tag);
                                if (compoundTag != null){
                                    if (compoundTag.getInt(lost) < getLostMaxHealth()) {
                                        compoundTag.putInt(lost, compoundTag.getInt(lost) + 1);
                                    }
                                }
                            }
                        }
                    }
                });

            }
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        CompoundTag stackTag = stack.get(DataReg.tag);
        if (stackTag!=null) {
            if (stackTag.getInt(lost)  > getLostMaxHealth()) {
                stackTag.putInt(lost, getLostMaxHealth());
            }
            if (stackTag.getInt(lost) < 0) {
                stackTag.putInt(lost, 0);
            }
        }
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, boolean recentlyHit, ItemStack stack) {
        return super.getDropRule(slotContext, source, recentlyHit, stack);
    }

    @Override
    public @NotNull ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> get = super.getAttributeModifiers(slotContext, id, stack);
        CuriosApi.addSlotModifier(get,"belt",id,1, AttributeModifier.Operation.ADD_VALUE);
        get.put(MalumAttributes.GEAS_LIMIT,new AttributeModifier(id,
                2, AttributeModifier.Operation.ADD_VALUE));
        float lostLast = 0;
        CompoundTag compoundTag  =stack.get(DataReg.tag);
        if (compoundTag!=null   ) {
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
        tooltipComponents.add(Component.translatable("item.malstone.chess_fell.text.6").setStyle(Style.EMPTY.withColor(color())));
        if (this.hasWeepingWllPower(stack)) {
            tooltipComponents.add(Component.literal(""));
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
