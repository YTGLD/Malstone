package com.ytgld.malstone.items;

import com.google.common.collect.Multimap;
import com.sammy.malum.common.data.attachment.CachedSpiritDropsData;
import com.sammy.malum.core.listeners.ReapingDataReloadListener;
import com.sammy.malum.core.systems.spirit.EntitySpiritDropData;
import com.sammy.malum.registry.common.MalumAttachmentTypes;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import team.lodestar.lodestone.helpers.ItemHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

/**
 * 弑神令
 * <p>
 *击中生物将产生血色精魂
 * <p>
 * 拾取血色精魂获得伤害增益
 * <p>
 * 拾取过多的血色精魂会产生血爆
 */
public class KillTheGods extends WhiteArrow {
    public KillTheGods(Properties properties) {
        super(properties);
    }
    public static void attackPost(LivingDamageEvent.Pre event) {
        LivingEntity target = event.getEntity();
        if (event.getSource().getEntity() instanceof Player attacker){
            if (Handler.hascurio(attacker, ItemRegs.KillTheGods_.get())) {
                dropSpirits(target, attacker);
            }
        }
    }

    public static Handler.SpiritSpawner spawnSpirits(Entity target) {
        return new Handler.SpiritSpawner(target);
    }
    public static void dropSpirits(LivingEntity target, LivingEntity attacker) {
        Level level = target.level();
        Handler.SpiritSpawner spiritSpawner = spawnSpirits(target).setPreferredCollector(attacker);
        spiritSpawner.spawnSpirits(level);
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = super.getAttributeModifiers(slotContext, id, stack);
        CuriosApi.addSlotModifier(modifierMultimap,"rune",id,1, AttributeModifier.Operation.ADD_VALUE);
        return modifierMultimap;
    }
}
