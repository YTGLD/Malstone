package com.ytgld.malstone.items.init;

import com.sammy.malum.common.item.IVoidItem;
import com.sammy.malum.registry.common.item.ItemRegistry;
import com.sammy.malum.visual_effects.ScreenParticleEffects;
import com.ytgld.malstone.Light;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Map;

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

    @Nullable
    public MalstoneText malstoneText(ItemStack stack,List<Component> tooltipComponents){
        return null;
    }


    @Override
    public void appendHoverText(ItemStack stack, Level context, List<Component> tooltipComponents, TooltipFlag flag) {
        this.malstoneText(stack, tooltipComponents);
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        if (!slotContext.entity().level().isClientSide) {
            CompoundTag compoundTag = stack.getTag();
            if (compoundTag == null) {
                stack.getOrCreateTag();
            }
            if (canUseWeepingPower()) {
                if (this.hasWeepingWllPower(stack)) {
                    if (compoundTag != null) {
                        if (slotContext.entity().tickCount % 20 == 1) {
                            compoundTag.putInt(weepingWellPower, compoundTag.getInt(weepingWellPower) - 1);
                        }
                    } else {
                        stack.getOrCreateTag();
                    }
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

    //--------------------------------客户端粒子--------------------------------------------

    public boolean canSpawn(){
        return false;
    }
    //--------------------------------客户端粒子--------------------------------------------
    //--------------------------------哭泣之井--------------------------------------------
    public static final String weepingWellPower = "weepingWellPower";

    public static void eatOfPlayer(LivingEntity livingEntity){
        if (livingEntity instanceof Player player) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                Map<String, ICurioStacksHandler> curios = handler.getCurios();
                for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                    ICurioStacksHandler stacksHandler = entry.getValue();
                    IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                    for (int i = 0; i < stacksHandler.getSlots(); i++) {
                        ItemStack stack = stackHandler.getStackInSlot(i);
                        if (stack.getItem() instanceof BaseItem item) {
                            if (item.canUseWeepingPower()) {
                                item.eatAtWeepingWell(stack);
                            }
                        }
                    }
                }
            });
        }
    }

    public boolean canUseWeepingPower(){
        return false;
    }
    public boolean hasWeepingWllPower(ItemStack stack){
        if (canUseWeepingPower()) {
            CompoundTag compoundTag = stack.getTag();
            if (compoundTag != null) {
                return compoundTag.getInt(weepingWellPower) > 0;
            }
        }
        return false;
    }
    public void eatAtWeepingWell(ItemStack stack){
        if (canUseWeepingPower()) {
            CompoundTag compoundTag = stack.getTag();
            if (compoundTag == null) {
                stack.getOrCreateTag();
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
        CompoundTag compoundTag = stack.getTag();
        if (compoundTag != null) {
            return compoundTag.getInt(weepingWellPower);
        }
        return 0;
    }
    public void addWeepingPower(ItemStack stack){
        addWeepingPower(stack,1.0f);
    }
    public void addWeepingPower(ItemStack stack,float mul){
        CompoundTag compoundTag = stack.getTag();
        if (compoundTag == null) {
            stack.getOrCreateTag();
        }
        int time = (int) (this.maxWeepingPower(stack) * mul) + getWeepingPower(stack);
        if (time > maxWeepingPower(stack)) {
            time = maxWeepingPower(stack);
        }
        if (compoundTag != null) {
            compoundTag.putInt(weepingWellPower, time);
        }
    }
    //--------------------------------哭泣之井--------------------------------------------
    public record MalstoneText(ItemStack stack,List<Component> tooltipComponents){ }
}
