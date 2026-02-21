package com.ytgld.malstone.magic;

import com.sammy.malum.core.systems.registry.DeferredSpiritTypes;
import com.sammy.malum.core.systems.registry.SpiritHolder;
import com.sammy.malum.core.systems.spirit.type.SpiritArcanaType;
import com.sammy.malum.core.systems.spirit.type.SpiritColorProperties;
import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.items.init.ItemRegs;
import team.lodestar.lodestone.systems.easing.Easing;

import java.awt.*;

public class MalstoneSpirits {
    public static final DeferredSpiritTypes SPIRIT_TYPES = DeferredSpiritTypes.create(Malstone.MODID);

    public static SpiritColorProperties BLOOD_COLORS() {
        return SpiritColorProperties.create(new Color(255, 0, 0), new Color(255,  0, 0)).setColorCoefficient(0.8F).setColorEasing(Easing.QUAD_IN).build();
    }
    public static final SpiritHolder<SpiritArcanaType> BLOOD = SPIRIT_TYPES.register("blood",
            () -> new SpiritArcanaType(BLOOD_COLORS(), ItemRegs.BloodSpirit_));
}
