package com.ytgld.malstone.items.init;

import com.sammy.malum.common.item.spirit.SpiritShardItem;
import com.sammy.malum.registry.common.magic.MalumSpiritTypes;
import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.items.KillTheGods;
import com.ytgld.malstone.items.rune.BladeOath;
import com.ytgld.malstone.items.rune.EternalFallenSoul;
import com.ytgld.malstone.items.rune.Hungrier;
import com.ytgld.malstone.items.rune.Martyrdom;
import com.ytgld.malstone.items.soul.SoulDevice;
import com.ytgld.malstone.items.soul.SoulSteelComponents;
import com.ytgld.malstone.items.white.*;
import com.ytgld.malstone.magic.MalstoneSpirits;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegs {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(Registries.ITEM, Malstone.MODID);
    public static final DeferredHolder<Item, Item> HugeSouls_ = REGISTER.register("huge_soul",
            ()->new HugeSouls(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> BreakingTheLife_ = REGISTER.register("breaking_the_life",
            ()->new BreakingTheLife(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> WhiteArrowBlade_ = REGISTER.register("white_arrow_blade",
            ()->new WhiteArrowBlade(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> SoulSteelComponents_ = REGISTER.register("soul_steel_components",
            ()->new SoulSteelComponents(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> RingOfAuthority_ = REGISTER.register("ring_of_authority",
            ()->new RingOfAuthority(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> EternalFallenSoul_ = REGISTER.register("eternal_fallen_soul",
            ()->new EternalFallenSoul(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> BladeOath_ = REGISTER.register("blade_dath",
            ()->new BladeOath(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> Martyrdom_ = REGISTER.register("martyrdom",
            ()->new Martyrdom(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> Hungrier_ = REGISTER.register("hungrier",
            ()->new Hungrier(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> SoulDevice_ = REGISTER.register("soul_device",
            ()->new SoulDevice(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> BreakingTheWeapon_ = REGISTER.register("breaking_the_weapon",
            ()->new BreakingTheWeapon(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredHolder<Item, Item> KillTheGods_ = REGISTER.register("kill_the_gods",
            ()->new KillTheGods(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));



    public static final DeferredHolder<Item, SpiritShardItem> BloodSpirit_ = REGISTER.register("blood_spirit",
            ()->new SpiritShardItem(new Item.Properties(), MalstoneSpirits.BLOOD));

}
