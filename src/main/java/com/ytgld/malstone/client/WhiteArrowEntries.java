package com.ytgld.malstone.client;

import com.sammy.malum.client.screen.codex.BookWidgetStyle;
import com.sammy.malum.client.screen.codex.pages.recipe.RuneworkingPage;
import com.sammy.malum.client.screen.codex.pages.recipe.SpiritInfusionPage;
import com.sammy.malum.client.screen.codex.pages.text.HeadlineTextPage;
import com.sammy.malum.client.screen.codex.pages.text.TextPage;
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
        screen.addEntry("ring_of_authority",  1, -3, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.RingOfAuthority_)
                        .setStyle(BookWidgetStyle.WITHERED))
                .addPage(new HeadlineTextPage("ring_of_authority", "ring_of_authority.1"))
                .addPage(new TextPage("ring_of_authority.2"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.RingOfAuthority_.get()))
        );
        screen.addEntry("white_arrow_blade",  0, -3, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.WhiteArrowBlade_)
                        .setStyle(BookWidgetStyle.WITHERED))
                .addPage(new HeadlineTextPage("white_arrow_blade", "white_arrow_blade.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.WhiteArrowBlade_.get()))
        );

        screen.addEntry("eternal_fallen_soul",  0, -4, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.EternalFallenSoul_)
                        .setStyle(BookWidgetStyle.TOTEMIC_SOULWOOD))
                .addPage(new HeadlineTextPage("eternal_fallen_soul", "eternal_fallen_soul.1"))
                .addPage(RuneworkingPage.fromOutput(ItemRegs.EternalFallenSoul_.get()))
        );

        screen.addEntry("blade_dath",  0, -5, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.BladeOath_)
                        .setStyle(BookWidgetStyle.TOTEMIC_SOULWOOD))
                .addPage(new HeadlineTextPage("blade_dath", "blade_dath.1"))
                .addPage(RuneworkingPage.fromOutput(ItemRegs.BladeOath_.get()))
        );


        screen.addEntry("breaking_the_weapon",  0, -6, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.BreakingTheWeapon_)
                        .setStyle(BookWidgetStyle.TOTEMIC_SOULWOOD))
                .addPage(new HeadlineTextPage("breaking_the_weapon", "breaking_the_weapon.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.BreakingTheWeapon_.get()))
        );


        screen.addEntry("falling_well", 1, -7, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.FallingWell_)
                        .setStyle(BookWidgetStyle.TOTEMIC_SOULWOOD))
                .addPage(new HeadlineTextPage("falling_well", "falling_well.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.FallingWell_.get()))
        );


        screen.addEntry("condenser", -1, -7, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.Condenser_)
                        .setStyle(BookWidgetStyle.TOTEMIC_SOULWOOD))
                .addPage(new HeadlineTextPage("condenser", "condenser.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.Condenser_.get()))
        );



        screen.addEntry("evil_engine", 0, -8, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.EvilEngine_)
                        .setStyle(BookWidgetStyle.TOTEMIC_SOULWOOD))
                .addPage(new HeadlineTextPage("evil_engine", "evil_engine.1"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.EvilEngine_.get()))
        );

        screen.addEntry("weeping_immortal", 0, -9, b -> b
                .configureWidget(w -> w.setIcon(ItemRegs.WeepingImmortal_)
                        .setStyle(BookWidgetStyle.TOTEMIC_SOULWOOD))
                .addPage(new HeadlineTextPage("weeping_immortal", "weeping_immortal.1"))
                .addPage(new TextPage("weeping_immortal.2"))
                .addPage(new TextPage("weeping_immortal.3"))
                .addPage(SpiritInfusionPage.fromOutput(ItemRegs.WeepingImmortal_.get()))
        );
    }

}
