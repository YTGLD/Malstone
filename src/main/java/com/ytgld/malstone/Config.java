package com.ytgld.malstone;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.DoubleValue life_max_BreakingTheLife = BUILDER
            .comment("破命刀的血量最多恢复到的值")
            .comment("The maximum amount of health restored to the value of the Life-Breaking Knife")
            .defineInRange("life_max_BreakingTheLife",0.5,0,1);
    private static final ForgeConfigSpec.DoubleValue WhiteArrowBladeAttack = BUILDER
            .comment("白镴刀锋的物理伤害")
            .comment("The physical damage of the white blade")
            .defineInRange("WhiteArrowBladeAttack",1.3,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue WhiteArrowBladeMagic = BUILDER
            .comment("白镴刀锋的恶念转化")
            .comment("The transformation of the evil thoughts of the blade of the white arrow")
            .defineInRange("WhiteArrowBladeMagic",0.5f,0,Integer.MAX_VALUE);



    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static ForgeConfigSpec.DoubleValue getWhiteArrowBladeAttack() {
        return WhiteArrowBladeAttack;
    }

    public static ForgeConfigSpec.DoubleValue getWhiteArrowBladeMagic() {
        return WhiteArrowBladeMagic;
    }

    public static ForgeConfigSpec.DoubleValue getLife_max_BreakingTheLife() {
        return life_max_BreakingTheLife;
    }
}
