package com.ytgld.malstone.items;

import com.sammy.malum.common.capability.MalumLivingEntityDataCapability;
import com.sammy.malum.common.entity.activator.SpiritCollectionActivatorEntity;
import com.sammy.malum.common.item.curiosities.weapons.WeightOfWorldsItem;
import com.sammy.malum.registry.common.item.ItemTiers;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.Light;
import com.ytgld.malstone.items.init.ItemRegs;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.systems.item.tools.LodestoneAxeItem;

import java.util.List;

/**
 * 沉沦战斧
 * <p>
 * 3倍暴击伤害
 * <p>
 * 暴击必定触发精魂收割效果
 */
public class WallowAxe extends LodestoneAxeItem {
    public WallowAxe(Properties properties) {
        super(ItemTiers.ItemTierEnum.TYRVING, 10, -0.5f, properties);
    }
    @Override
    public Component getName(ItemStack p_41458_) {
        Component component =super.getName(p_41458_);
        return component.copy().setStyle(Style.EMPTY.withColor(Light.ARGB.color(255,125,125,200)));
    }
    public static void cit(CriticalHitEvent event){
        if (event.isVanillaCritical()) {
            if (event.getEntity().getMainHandItem().is(ItemRegs.WallowAxe_.get())) {
                event.setDamageModifier(event.getDamageModifier() * 3);
                if (event.getTarget() instanceof LivingEntity living) {
                    WallowAxe.hurtEvent(event.getEntity(),living);
                }
            }
        }
    }

    public static void hurtEvent( LivingEntity attacker, LivingEntity target) {
        float speed = 0.4F;
        Level level = attacker.level();
        RandomSource random = level.getRandom();
        Vec3 position = target.position().add(0.0F, target.getBbHeight() / 2.0F, (double)0.0F);
        int amount = Mth.nextInt(RandomSource.create(),1,3);

        for(int i = 0; i < amount; ++i) {
            SpiritCollectionActivatorEntity entity = new SpiritCollectionActivatorEntity(level, attacker.getUUID(), position.x, position.y, position.z, (double) RandomHelper.randomBetween(random, -speed, speed), (double)RandomHelper.randomBetween(random, 0.05F, 0.06F), (double)RandomHelper.randomBetween(random, -speed, speed));
            level.addFreshEntity(entity);
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.malstone.wallow_axe.text.1").setStyle(Style.EMPTY.withColor(Light.ARGB.color(255,125,125,200))));
        components.add(Component.translatable("item.malstone.wallow_axe.text.2").setStyle(Style.EMPTY.withColor(Light.ARGB.color(255,125,125,200))));
    }
}
