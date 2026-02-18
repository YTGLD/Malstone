package com.ytgld.malstone.items.init;

import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.items.rune.BladeOath;
import com.ytgld.malstone.items.rune.EternalFallenSoul;
import com.ytgld.malstone.items.rune.Hungrier;
import com.ytgld.malstone.items.rune.Martyrdom;
import com.ytgld.malstone.items.soul.SoulDevice;
import com.ytgld.malstone.items.soul.SoulSteelComponents;
import com.ytgld.malstone.items.white.BreakingTheLife;
import com.ytgld.malstone.items.white.HugeSouls;
import com.ytgld.malstone.items.white.RingOfAuthority;
import com.ytgld.malstone.items.white.WhiteArrowBlade;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegs {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Malstone.MODID);
    public static final RegistryObject<Item> HugeSouls_ = REGISTER.register("huge_soul",
            ()->new HugeSouls(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> BreakingTheLife_ = REGISTER.register("breaking_the_life",
            ()->new BreakingTheLife(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> WhiteArrowBlade_ = REGISTER.register("white_arrow_blade",
            ()->new WhiteArrowBlade(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> SoulSteelComponents_ = REGISTER.register("soul_steel_components",
            ()->new SoulSteelComponents(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> RingOfAuthority_ = REGISTER.register("ring_of_authority",
            ()->new RingOfAuthority(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> EternalFallenSoul_ = REGISTER.register("eternal_fallen_soul",
            ()->new EternalFallenSoul(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> BladeOath_ = REGISTER.register("blade_dath",
            ()->new BladeOath(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> Martyrdom_ = REGISTER.register("martyrdom",
            ()->new Martyrdom(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> Hungrier_ = REGISTER.register("hungrier",
            ()->new Hungrier(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> SoulDevice_ = REGISTER.register("soul_device",
            ()->new SoulDevice(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));

}
