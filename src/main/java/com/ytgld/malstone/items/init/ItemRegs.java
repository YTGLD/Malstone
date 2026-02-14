package com.ytgld.malstone.items.init;

import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.items.BreakingTheLife;
import com.ytgld.malstone.items.HugeSouls;
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


}
