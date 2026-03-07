package com.ytgld.malstone;


import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.DoubleValue life_max_BreakingTheLife = BUILDER
            .comment("破命刀的血量最多恢复到的值")
            .comment("The maximum amount of health restored to the value of the Life-Breaking Knife")
            .defineInRange("life_max_BreakingTheLife",0.5,0,1);
    private static final ModConfigSpec.DoubleValue WhiteArrowBladeAttack = BUILDER
            .comment("白镴刀锋的物理伤害")
            .comment("The physical damage of the white blade")
            .defineInRange("WhiteArrowBladeAttack",1.3,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue WhiteArrowBladeMagic = BUILDER
            .comment("白镴刀锋的恶念转化")
            .comment("The transformation of the evil thoughts of the blade of the white arrow")
            .defineInRange("WhiteArrowBladeMagic",0.5f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue armorHugeSouls = BUILDER
            .comment("白镴方体的每个恶念要塞装备或者饰品给予的邪念之护")
            .comment("The protection of evil thoughts given by each evil thought fortress equipment in the white cypress cube")
            .defineInRange("armorHugeSouls",0.025f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue armorDefHugeSouls = BUILDER
            .comment("白镴方体的给予的邪念之护")
            .comment("The protection of evil thoughts given by the white cypress cube")
            .defineInRange("armorDefHugeSouls",0.1f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue speedEternalFallenSoul = BUILDER
            .comment("永堕灵魂符文的叠加速度")
            .comment("The stacking speed of the Eternal Fallen Soul rune")
            .defineInRange("speedEternalFallenSoul",0.025f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue maxEternalFallenSoul = BUILDER
            .comment("永堕灵魂符文的最大叠加值")
            .comment("The maximum stack value of the Eternal Fallen Soul Rune")
            .defineInRange("maxEternalFallenSoul",0.25f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue doubleBladeOath = BUILDER
            .comment("锋刀誓言符文的第二次伤害的值")
            .comment("The value of the second damage of the Blade Oath rune")
            .defineInRange("doubleBladeOath",0.5f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue mMartyrdomDamage = BUILDER
            .comment("殉锋符文记录的上一次伤害比率")
            .comment("The last damage ratio recorded by the Rune of the Martyrdom")
            .defineInRange("mMartyrdomDamage",0.9f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue attackDoubleMartyrdom = BUILDER
            .comment("殉锋符文在触发连击奖励时给予的次数")
            .comment("The number of times the Rune of Martyrdom is given when triggering a combo bonus")
            .defineInRange("attackDoubleMartyrdom",1,1,Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue theBreakingTheWeapon = BUILDER
            .comment("破极兵刃的最大宽容度数")
            .comment("The maximum tolerance of the Broken Blade")
            .defineInRange("theBreakingTheWeapon",45,0,360);
    private static final ModConfigSpec.IntValue maxArmorDamageBreakingTheWeapon = BUILDER
            .comment("破极兵刃的最大额外伤害（护甲，灵魂护盾）")
            .comment("The maximum bonus damage of the Extreme Blade（Armor，Soul shield）")
            .defineInRange("maxArmorDamageBreakingTheWeapon",30,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue healthDamageBreakingTheWeapon = BUILDER
            .comment("破极兵刃的最大额外伤害倍率（满生命值）")
            .comment("The maximum bonus damage multiplier of the Extreme Blade（Full health）")
            .defineInRange("healthDamageBreakingTheWeapon",1.5f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue addPowerFallingWell = BUILDER
            .comment("堕井充能后的属性加成")
            .comment("Attribute bonus after falling well charge")
            .defineInRange("addPowerFallingWell",0.2f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue lvlExtremelyDead = BUILDER
            .comment("极殇的幽影精魂机率")
            .comment("The probability of the ghost spirit of extreme death")
            .defineInRange("lvlExtremelyDead_1",2,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue damageExtremelyDead = BUILDER
            .comment("极殇的伤害")
            .comment("Extremely deadly injuries")
            .defineInRange("damageExtremelyDead",0.25f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue attributeWeepingImmortal = BUILDER
            .comment("泣仙的属性加成")
            .comment("Weeping Immortal's attribute bonus")
            .defineInRange("attributeWeepingImmortal",0.15f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.BooleanValue attributeFallCurse = BUILDER
            .comment("启用堕落诅咒的属性")
            .comment("Enables the Curse of the Corruption attribute")
            .define("attributeFallCurse",true);
    private static final ModConfigSpec.DoubleValue attributeCorpseCauldron = BUILDER
            .comment("尸釜的激活属性")
            .comment("The activation attribute of the cauldron")
            .defineInRange("attributeCorpseCauldron",0.15f,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.ConfigValue<List<? extends String>>  effectFallWell =  BUILDER
            .comment("堕井的堕落诅咒添加生物黑名单")
            .comment("Fallen Curse of the Fall Well adds mob blacklist")
            .defineList("effectFallWell",
                    List.of("minecraft:player"),
                    s->s instanceof String);
    private static final ModConfigSpec.IntValue lostChessFell = BUILDER
            .comment("棋陨最多可降低的最大生命值次数")
            .comment("The maximum number of times the Fall can be reduced")
            .defineInRange("lostChessFell",10,0,Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue attributeChessFell = BUILDER
            .comment("棋陨最多有效的属性，也就是最小可以获得正面属性的次数，不过是正数")
            .comment("Chess falls the most effective attributes，That is, the minimum number of times you can get positive attributes, but it is a positive number")
            .defineInRange("attributeChessFell",-4,0,Integer.MAX_VALUE);


    private static final ModConfigSpec.DoubleValue ArcaneHarmonics = BUILDER
            .comment("奥术谐振对物品的属性影响")
            .comment("Arcane resonance affects the attributes of items")
            .defineInRange("ArcaneHarmonics",1f,0,Integer.MAX_VALUE);

    static final ModConfigSpec SPEC  = BUILDER.build();

    public static ModConfigSpec.IntValue getAttributeChessFell() {
        return attributeChessFell;
    }

    public static ModConfigSpec.IntValue getLostChessFell() {
        return lostChessFell;
    }
    public static ModConfigSpec.ConfigValue<List<? extends String>> getEffectFallWell() {
        return effectFallWell;
    }


    public static ModConfigSpec.BooleanValue getAttributeFallCurse() {
        return attributeFallCurse;
    }
    public static ModConfigSpec.DoubleValue getAttributeCorpseCauldron() {
        return attributeCorpseCauldron;
    }
    public static ModConfigSpec.DoubleValue getAttributeWeepingImmortal() {
        return attributeWeepingImmortal;
    }

    public static ModConfigSpec.IntValue getLvlExtremelyDead() {
        return lvlExtremelyDead;
    }

    public static ModConfigSpec.DoubleValue getDamageExtremelyDead() {
        return damageExtremelyDead;
    }

    public static ModConfigSpec.DoubleValue getAddPowerFallingWell() {
        return addPowerFallingWell;
    }

    public static ModConfigSpec.DoubleValue getHealthDamageBreakingTheWeapon() {
        return healthDamageBreakingTheWeapon;
    }

    public static ModConfigSpec.IntValue getMaxArmorDamageBreakingTheWeapon() {
        return maxArmorDamageBreakingTheWeapon;
    }

    public static ModConfigSpec.IntValue getTheBreakingTheWeapon() {
        return theBreakingTheWeapon;
    }

    public static ModConfigSpec.DoubleValue getArcaneHarmonics() {
        return ArcaneHarmonics;
    }

    public static ModConfigSpec.DoubleValue getmMartyrdomDamage() {
        return mMartyrdomDamage;
    }

    public static ModConfigSpec.IntValue getAttackDoubleMartyrdom() {
        return attackDoubleMartyrdom;
    }

    public static ModConfigSpec.DoubleValue getDoubleBladeOath() {
        return doubleBladeOath;
    }

    public static ModConfigSpec.DoubleValue getMaxEternalFallenSoul() {
        return maxEternalFallenSoul;
    }

    public static ModConfigSpec.DoubleValue getSpeedEternalFallenSoul() {
        return speedEternalFallenSoul;
    }

    public static ModConfigSpec.DoubleValue getArmorDefHugeSouls() {
        return armorDefHugeSouls;
    }
    public static ModConfigSpec.DoubleValue getArmorHugeSouls() {
        return armorHugeSouls;
    }

    public static ModConfigSpec.DoubleValue getWhiteArrowBladeAttack() {
        return WhiteArrowBladeAttack;
    }

    public static ModConfigSpec.DoubleValue getWhiteArrowBladeMagic() {
        return WhiteArrowBladeMagic;
    }

    public static ModConfigSpec.DoubleValue getLife_max_BreakingTheLife() {
        return life_max_BreakingTheLife;
    }
}
