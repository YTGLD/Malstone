package com.ytgld.malstone.items.init;

import com.sammy.malum.common.item.IVoidItem;
import com.sammy.malum.visual_effects.ScreenParticleEffects;
import com.ytgld.malstone.Light;
import com.ytgld.malstone.effects.Effects;
import com.ytgld.malstone.magic.DataReg;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class BaseItem extends Item implements ICurioItem , IVoidItem {
    public BaseItem(Properties properties) {
        super(properties);
    }
    public boolean canUseSkill(){
        return false;
    }
    public int color(){
        return Light.ARGB.color(255, 255, 255, 0);
    }
    public static final String weepingWellPower = "weepingWellPower";
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        CompoundTag compoundTag = stack.get(DataReg.tag);
        if (compoundTag == null) {
            stack.set(DataReg.tag, new CompoundTag());
        }
        if (canUseWeepingPower()) {
            if (this.hasWeepingWllPower(stack)) {
                if (compoundTag != null) {
                    if (this.hasWeepingWllPower(stack)) {
                        if (slotContext.entity().tickCount % 20 == 1) {
                            compoundTag.putInt(weepingWellPower, compoundTag.getInt(weepingWellPower) - 1);
                        }
                    }
                } else {
                    stack.set(DataReg.tag, new CompoundTag());
                }
            }
        }
    }

    @Override
    public void spawnEarlyParticles(ScreenParticleHolder target, Level level, float partialTick, ItemStack stack, float x, float y) {
        if (this.hasWeepingWllPower(stack)) {
            ScreenParticleEffects.spawnVoidItemScreenParticles(target, level, this.getVoidParticleIntensity() * 2, partialTick);
        }else if (canSpawn()){
            ScreenParticleEffects.spawnVoidItemScreenParticles(target, level, this.getVoidParticleIntensity(), partialTick);
        }
    }

    public boolean canSpawn(){
        return false;
    }
    public boolean canUseWeepingPower(){
        return false;
    }

    public boolean hasWeepingWllPower(ItemStack stack){
        if (canUseWeepingPower()) {
            CompoundTag compoundTag = stack.get(DataReg.tag);
            if (compoundTag != null) {
                return compoundTag.getInt(weepingWellPower) > 0;
            }
        }
        return false;
    }
    public void eatAtWeepingWell(ItemStack stack){
        if (canUseWeepingPower()) {
            CompoundTag compoundTag = stack.get(DataReg.tag);
            if (compoundTag == null) {
                stack.set(DataReg.tag, new CompoundTag());
            }
            if (compoundTag != null) {
                compoundTag.putInt(weepingWellPower, this.maxWeepingPower(stack));
            }
        }
    }
    public int maxWeepingPower(ItemStack stack){
        return 0;
    }
    public int getWeepingPower(ItemStack stack){
        CompoundTag compoundTag = stack.get(DataReg.tag);
        if (compoundTag != null) {
            return compoundTag.getInt(weepingWellPower);
        }
        return 0;
    }
    public void addWeepingPower(ItemStack stack){
        addWeepingPower(stack,1.0f);
    }
    public void addWeepingPower(ItemStack stack,float mul){
        CompoundTag compoundTag = stack.get(DataReg.tag);
        if (compoundTag == null) {
            stack.set(DataReg.tag, new CompoundTag());
        }
        if (compoundTag != null) {
            compoundTag.putInt(weepingWellPower, (int) (this.maxWeepingPower(stack) * mul));
        }
    }
}
