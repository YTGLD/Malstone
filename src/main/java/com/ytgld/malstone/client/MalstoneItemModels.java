package com.ytgld.malstone.client;

import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.items.init.ItemRegs;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import team.lodestar.lodestone.systems.datagen.ItemModelSmithTypes;
import team.lodestar.lodestone.systems.datagen.itemsmith.AbstractItemModelSmith;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneItemModelProvider;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MalstoneItemModels extends LodestoneItemModelProvider {
    public MalstoneItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Malstone.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Consumer<Supplier<? extends Item>> consumer =   (supplier)-> ItemRegs.WallowAxe_.get();
        AbstractItemModelSmith.ItemModelSmithData data = new AbstractItemModelSmith.ItemModelSmithData(this,consumer);
        ItemModelSmithTypes.HANDHELD_ITEM.act(data, ItemRegs.WallowAxe_);
    }
}
