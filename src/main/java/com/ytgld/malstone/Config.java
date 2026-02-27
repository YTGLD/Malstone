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
    private static final ForgeConfigSpec.DoubleValue addPowerFallingWell = BUILDER
            .comment("堕井充能后的属性加成")
            .comment("Attribute bonus after falling well charge")
            .defineInRange("addPowerFallingWell",0.2f,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue lvlExtremelyDead = BUILDER
            .comment("极殇的幽影精魂机率")
            .comment("The probability of the ghost spirit of extreme death")
            .defineInRange("lvlExtremelyDead_1",2,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue damageExtremelyDead = BUILDER
            .comment("极殇的伤害")
            .comment("Extremely deadly injuries")
            .defineInRange("damageExtremelyDead",0.25f,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue attributeWeepingImmortal = BUILDER
            .comment("泣仙的属性加成")
            .comment("Weeping Immortal's attribute bonus")
            .defineInRange("attributeWeepingImmortal",0.15f,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue theBreakingTheWeapon = BUILDER
            .comment("破极兵刃的最大宽容度数")
            .comment("The maximum tolerance of the Broken Blade")
            .defineInRange("theBreakingTheWeapon",45,0,360);
    private static final ForgeConfigSpec.IntValue maxArmorDamageBreakingTheWeapon = BUILDER
            .comment("破极兵刃的最大额外伤害（护甲，灵魂护盾）")
            .comment("The maximum bonus damage of the Extreme Blade（Armor，Soul shield）")
            .defineInRange("maxArmorDamageBreakingTheWeapon",30,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue healthDamageBreakingTheWeapon = BUILDER
            .comment("破极兵刃的最大额外伤害倍率（满生命值）")
            .comment("The maximum bonus damage multiplier of the Extreme Blade（Full health）")
            .defineInRange("healthDamageBreakingTheWeapon",1.5f,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.BooleanValue attributeFallCurse = BUILDER
            .comment("启用堕落诅咒的属性")
            .comment("Enables the Curse of the Corruption attribute")
            .define("attributeFallCurse",true);








    private static final ForgeConfigSpec.IntValue ArcaneHarmonics = BUILDER
            .comment("奥术谐振对物品的属性影响")
            .comment("Arcane resonance affects the attributes of items")
            .defineInRange("ArcaneHarmonics",1,0,Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC  = BUILDER.build();

    public static ForgeConfigSpec.BooleanValue getAttributeFallCurse() {
        return attributeFallCurse;
    }

    public static ForgeConfigSpec.DoubleValue getHealthDamageBreakingTheWeapon() {
        return healthDamageBreakingTheWeapon;
    }

    public static ForgeConfigSpec.IntValue getMaxArmorDamageBreakingTheWeapon() {
        return maxArmorDamageBreakingTheWeapon;
    }

    public static ForgeConfigSpec.IntValue getTheBreakingTheWeapon() {
        return theBreakingTheWeapon;
    }
    public static ForgeConfigSpec.DoubleValue getAttributeWeepingImmortal() {
        return attributeWeepingImmortal;
    }

    public static ForgeConfigSpec.IntValue getLvlExtremelyDead() {
        return lvlExtremelyDead;
    }

    public static ForgeConfigSpec.DoubleValue getDamageExtremelyDead() {
        return damageExtremelyDead;
    }

    public static ForgeConfigSpec.DoubleValue getAddPowerFallingWell() {
        return addPowerFallingWell;
    }
    public static ForgeConfigSpec.IntValue getArcaneHarmonics() {
        return ArcaneHarmonics;
    }

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
