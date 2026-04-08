package com.ytgld.malstone.items.soul;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.sammy.malum.common.container.SpiritPouchContainer;
import com.sammy.malum.common.entity.spirit.SpiritItemEntity;
import com.sammy.malum.common.item.curiosities.SpiritPouchItem;
import com.sammy.malum.common.item.spirit.SpiritShardItem;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.SoulSteel;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.helpers.ItemHelper;
import team.lodestar.lodestone.systems.container.ItemInventory;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

/**
 * 魂魄装置
 * <p>
 * 拾取精魂时有概率使数量翻倍
 */
public class SoulDevice extends SoulSteel {
    public SoulDevice(Properties properties) {
        super(properties);
    }
    public static void doubleSpirit(LivingEntity collector, ItemStack stack) {
        if (collector instanceof Player player) {
            if (Handler.hascurio(collector, ItemRegs.SoulDevice_.get())) {
                if (Mth.nextInt(RandomSource.create(), 0, 100) < 50) {
                    for (NonNullList<ItemStack> playerInventory : ImmutableList.of(player.getInventory().items, player.getInventory().armor, player.getInventory().offhand)) {
                        for (ItemStack item : playerInventory) {
                            if (item.getItem() instanceof SpiritPouchItem) {
                                ItemInventory inventory = SpiritPouchItem.getInventory(item);
                                ItemStack result = inventory.addItem(stack);
                                if (result.isEmpty()) {
                                    if (player.containerMenu instanceof SpiritPouchContainer pouchMenu) {
                                        pouchMenu.update(inventory);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                    ItemHelper.giveItemToEntity(collector, stack);
                }
            }
        }
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifierMultimap = super.getAttributeModifiers(slotContext, uuid, stack);
        CuriosApi.addSlotModifier(modifierMultimap,"charm",uuid,1, AttributeModifier.Operation.ADDITION);
        return modifierMultimap;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.malstone.soul_device.text.1").setStyle(Style.EMPTY.withColor(color())));
    }
}
