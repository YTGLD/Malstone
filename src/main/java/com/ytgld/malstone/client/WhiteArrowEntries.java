package com.ytgld.malstone.client;

import com.sammy.malum.client.screen.codex.BookWidgetStyle;
import com.sammy.malum.client.screen.codex.pages.recipe.SpiritInfusionPage;
import com.sammy.malum.client.screen.codex.pages.text.HeadlineTextPage;
import com.sammy.malum.client.screen.codex.screens.ArcanaProgressionScreen;
import com.sammy.malum.client.screen.codex.screens.VoidProgressionScreen;
import com.ytgld.malstone.items.init.ItemRegs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;



public class WhiteArrowEntries {
    public static void setupEntries(VoidProgressionScreen screen) {
        Item EMPTY = ItemStack.EMPTY.getItem();
        screen.addEntry("huge_soul", 0, -2, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.HugeSouls_)
                        .setStyle(BookWidgetStyle.WITHERED))
                .addPage(new HeadlineTextPage("huge_soul", "huge_soul.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.HugeSouls_.get()))
        );
        screen.addEntry("breaking_the_life", -1, -3, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.BreakingTheLife_)
                        .setStyle(BookWidgetStyle.WITHERED))
                .addPage(new HeadlineTextPage("breaking_the_life", "breaking_the_life.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.BreakingTheLife_.get()))
        );
        screen.addEntry("white_arrow_blade",  1, -3, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.WhiteArrowBlade_)
                        .setStyle(BookWidgetStyle.WITHERED))
                .addPage(new HeadlineTextPage("white_arrow_blade", "white_arrow_blade.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.WhiteArrowBlade_.get()))
        );
    }

}
