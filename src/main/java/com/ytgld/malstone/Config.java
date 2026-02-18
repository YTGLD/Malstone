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
    private static final ForgeConfigSpec.DoubleValue armorHugeSouls = BUILDER
            .comment("白镴方体的每个恶念要塞装备或者饰品给予的邪念之护")
            .comment("The protection of evil thoughts given by each evil thought fortress equipment in the white cypress cube")
            .defineInRange("armorHugeSouls",0.025f,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue armorDefHugeSouls = BUILDER
            .comment("白镴方体的给予的邪念之护")
            .comment("The protection of evil thoughts given by the white cypress cube")
            .defineInRange("armorDefHugeSouls",0.1f,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue speedEternalFallenSoul = BUILDER
            .comment("永堕灵魂符文的叠加速度")
            .comment("The stacking speed of the Eternal Fallen Soul rune")
            .defineInRange("speedEternalFallenSoul",0.025f,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue maxEternalFallenSoul = BUILDER
            .comment("永堕灵魂符文的最大叠加值")
            .comment("The maximum stack value of the Eternal Fallen Soul Rune")
            .defineInRange("maxEternalFallenSoul",0.25f,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue doubleBladeOath = BUILDER
            .comment("锋刀誓言符文的第二次伤害的值")
            .comment("The value of the second damage of the Blade Oath rune")
            .defineInRange("doubleBladeOath",0.5f,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue mMartyrdomDamage = BUILDER
            .comment("殉锋符文记录的上一次伤害比率")
            .comment("The last damage ratio recorded by the Rune of the Martyrdom")
            .defineInRange("mMartyrdomDamage",0.9f,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue attackDoubleMartyrdom = BUILDER
            .comment("殉锋符文在触发连击奖励时给予的次数")
            .comment("The number of times the Rune of Martyrdom is given when triggering a combo bonus")
            .defineInRange("attackDoubleMartyrdom",1,1,Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC  = BUILDER.build();

    public static ForgeConfigSpec.DoubleValue getmMartyrdomDamage() {
        return mMartyrdomDamage;
    }

    public static ForgeConfigSpec.IntValue getAttackDoubleMartyrdom() {
        return attackDoubleMartyrdom;
    }

    public static ForgeConfigSpec.DoubleValue getDoubleBladeOath() {
        return doubleBladeOath;
    }

    public static ForgeConfigSpec.DoubleValue getMaxEternalFallenSoul() {
        return maxEternalFallenSoul;
    }

    public static ForgeConfigSpec.DoubleValue getSpeedEternalFallenSoul() {
        return speedEternalFallenSoul;
    }

    public static ForgeConfigSpec.DoubleValue getArmorDefHugeSouls() {
        return armorDefHugeSouls;
    }
    public static ForgeConfigSpec.DoubleValue getArmorHugeSouls() {
        return armorHugeSouls;
    }

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
