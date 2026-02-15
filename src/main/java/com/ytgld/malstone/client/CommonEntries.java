package com.ytgld.malstone.client;

import com.sammy.malum.client.screen.codex.BookWidgetStyle;
import com.sammy.malum.client.screen.codex.pages.recipe.SpiritInfusionPage;
import com.sammy.malum.client.screen.codex.pages.text.HeadlineTextPage;
import com.sammy.malum.client.screen.codex.screens.ArcanaProgressionScreen;
import com.sammy.malum.client.screen.codex.screens.VoidProgressionScreen;
import com.ytgld.malstone.items.init.ItemRegs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CommonEntries {
    public static void setupEntries(ArcanaProgressionScreen screen) {
        Item EMPTY = ItemStack.EMPTY.getItem();
        screen.addEntry("soul_steel_components", 0, -2, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.SoulSteelComponents_)
                        .setStyle(BookWidgetStyle.SOULWOOD))
                .addPage(new HeadlineTextPage("soul_steel_components", "soul_steel_components.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.SoulSteelComponents_.get()))
        );
    }
}
