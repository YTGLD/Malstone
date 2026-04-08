package com.ytgld.malstone.items.twisted;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.sammy.malum.common.container.SpiritPouchContainer;
import com.sammy.malum.common.entity.spirit.SpiritItemEntity;
import com.sammy.malum.common.item.curiosities.SpiritPouchItem;
import com.sammy.malum.core.handlers.SpiritHarvestHandler;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Twisted;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.container.ItemInventory;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

/**
 * 瞬时凝聚器
 * <p>
 * 生物产生的精魂可瞬间到达你的位置
 */
public class Condenser extends Twisted {
    public Condenser(Properties properties) {
        super(properties);
    }

    public static void tpPlayer(Player player , SpiritItemEntity spiritItemEntity){
        if (Handler.hascurio(player, ItemRegs.Condenser_.get())) {
            for (NonNullList<ItemStack> playerInventory : ImmutableList.of(player.getInventory().items, player.getInventory().armor, player.getInventory().offhand)) {
                for (ItemStack item : playerInventory) {
                    if (item.getItem() instanceof SpiritPouchItem) {
                        ItemInventory inventory = SpiritPouchItem.getInventory(item);
                        ItemStack result = inventory.addItem(spiritItemEntity.itemStack);
                        if (result.isEmpty()) {
                            if (player.containerMenu instanceof SpiritPouchContainer pouchMenu) {
                                pouchMenu.update(inventory);
                            }
                            spiritItemEntity.discard();;
                            return;
                        }
                    }
                }
            }
            SpiritHarvestHandler.pickupSpirit(player, spiritItemEntity.itemStack);
            spiritItemEntity.discard();;
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifierMultimap = super.getAttributeModifiers(slotContext, uuid, stack);
        CuriosApi.addSlotModifier(modifierMultimap,"belt",uuid,1, AttributeModifier.Operation.ADDITION);
        return modifierMultimap;
    }
    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.translatable("item.malstone.condenser.text.1").setStyle(Style.EMPTY.withColor(color())));
        return new MalstoneText(stack,tooltipComponents);
    }
}
