package com.ytgld.malstone.items.white;

import com.google.common.collect.Multimap;
import com.sammy.malum.registry.common.item.ItemRegistry;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HugeSouls extends WhiteArrow {
    public HugeSouls(Properties properties) {
        super(properties);
    }

    public static void lLivingHealEvent(LivingHealEvent event){
        if (event.getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.HugeSouls_.get())) {
                event.setAmount(event.getAmount() * 1.15f);
            }
        }
    }

    public static void lLivingDamageEvent(LivingDamageEvent event){
        if (event.getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.HugeSouls_.get())) {
                if (event.getSource().is(DamageTypes.MAGIC)) {
                    event.setAmount(event.getAmount() * 0.85f);
                }
            }
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.malstone.huge_soul.text.1").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.translatable("item.malstone.huge_soul.text.2").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.literal(""));
        components.add(Component.translatable("item.malstone.huge_soul.text.3").setStyle(Style.EMPTY.withColor(color())));
//        components.add(Component.translatable("item.malstone.huge_soul.text.4").setStyle(Style.EMPTY.withColor(color())));
        components.add(Component.literal(""));
        components.add(Component.translatable("item.malstone.white_arrow").setStyle(Style.EMPTY.withColor(color())));

    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> get = super.getAttributeModifiers(slotContext, uuid, stack);
        float m = (float) (double)Config.getArmorDefHugeSouls().get();
        List<Integer> integers = new ArrayList<>();
        CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                ICurioStacksHandler stacksHandler = entry.getValue();
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                for (int i = 0; i < stacksHandler.getSlots(); i++) {
                    ItemStack dotS = stackHandler.getStackInSlot(i);
                    if (dotS.getItem() instanceof WhiteArrow) {
                        integers.add(1);
                    }
                }
            }
        });
        float s = doI(slotContext.entity());
        for (int ignored : integers){
            m += s;
        }
        if (Handler.isEq(slotContext.entity(), EquipmentSlot.HEAD, ItemRegistry.MALIGNANT_STRONGHOLD_HELMET.get())) {
            m += s;
        }
        if (Handler.isEq(slotContext.entity(), EquipmentSlot.CHEST, ItemRegistry.MALIGNANT_STRONGHOLD_CHESTPLATE.get())) {
            m += s;
        }
        if (Handler.isEq(slotContext.entity(), EquipmentSlot.LEGS, ItemRegistry.MALIGNANT_STRONGHOLD_LEGGINGS.get())) {
            m += s;
        }
        if (Handler.isEq(slotContext.entity(), EquipmentSlot.FEET, ItemRegistry.MALIGNANT_STRONGHOLD_BOOTS.get())) {
            m += s;
        }
        get.put(AttReg.SuperMalicious.get(),new AttributeModifier(uuid,this.getDescriptionId(),m, AttributeModifier.Operation.ADDITION));
        return get;
    }


    public float doI(LivingEntity entity){
        return Handler.doWhiteArcaneHarmonics(entity, Config.getArmorHugeSouls().get().floatValue());
    }
}
