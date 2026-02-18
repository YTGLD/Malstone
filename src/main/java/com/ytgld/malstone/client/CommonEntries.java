package com.ytgld.malstone.client;

import com.sammy.malum.client.screen.codex.WidgetDesignType;
import com.sammy.malum.client.screen.codex.pages.recipe.SpiritInfusionPage;
import com.sammy.malum.client.screen.codex.pages.text.HeadlineTextPage;
import com.sammy.malum.client.screen.codex.screens.progression.ArcanaProgressionScreen;
import com.ytgld.malstone.items.init.ItemRegs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CommonEntries {
    public static void setupEntries(ArcanaProgressionScreen screen) {
        Item EMPTY = ItemStack.EMPTY.getItem();
        screen.addEntry("soul_steel_components", 0, -2, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.SoulSteelComponents_)
                        .setDesign(WidgetDesignType.DEFAULT, WidgetDesignType.FrameType.SOULWOOD, WidgetDesignType.FillingType.PAPER))
                .addPage(new HeadlineTextPage("soul_steel_components", "soul_steel_components.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.SoulSteelComponents_.get()))
        );

        screen.addEntry("martyrdom", 1, -3, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.Martyrdom_)
                        .setDesign(WidgetDesignType.DEFAULT, WidgetDesignType.FrameType.SOULWOOD, WidgetDesignType.FillingType.PAPER))
                .addPage(new HeadlineTextPage("martyrdom", "martyrdom.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.Martyrdom_.get()))
        );

        screen.addEntry("hungrier", -1, -3, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.Hungrier_)
                        .setDesign(WidgetDesignType.DEFAULT, WidgetDesignType.FrameType.SOULWOOD, WidgetDesignType.FillingType.PAPER))
                .addPage(new HeadlineTextPage("hungrier", "hungrier.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.Hungrier_.get()))
        );


        screen.addEntry("soul_device", 0, -4, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.SoulDevice_)
                        .setDesign(WidgetDesignType.DEFAULT, WidgetDesignType.FrameType.SOULWOOD, WidgetDesignType.FillingType.PAPER))
                .addPage(new HeadlineTextPage("soul_device", "soul_device.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.SoulDevice_.get()))
        );
    }
}
