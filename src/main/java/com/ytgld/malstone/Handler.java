package com.ytgld.malstone;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.malum.common.entity.spirit.SpiritItemEntity;
import com.sammy.malum.common.item.IMalumEventResponder;
import com.sammy.malum.config.CommonConfig;
import com.sammy.malum.core.handlers.SoulHarvestHandler;
import com.sammy.malum.core.systems.events.CollectSpiritEvent;
import com.sammy.malum.core.systems.events.ModifySpiritSpoilsEvent;
import com.sammy.malum.core.systems.spirit.EntitySpiritDropData;
import com.sammy.malum.core.systems.spirit.type.SpiritLike;
import com.sammy.malum.registry.common.MalumAttributes;
import com.sammy.malum.registry.common.MalumSoundEvents;
import com.ytgld.malstone.entity.BloodSpirit;
import com.ytgld.malstone.items.init.ItemRegs;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.EventHooks;
import team.lodestar.lodestone.handlers.ItemEventHandler;
import team.lodestar.lodestone.helpers.ColorHelper;
import team.lodestar.lodestone.helpers.ItemHelper;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static team.lodestar.lodestone.helpers.RandomHelper.randomBetween;

public class Handler {
    public static boolean hascurio(LivingEntity entity, Item curio) {
        return CuriosApi.getCuriosInventory(entity).isPresent()
                && CuriosApi.getCuriosInventory(entity).get().isEquipped(curio);
    }
    public static boolean isEq(LivingEntity living , EquipmentSlot slot, Item item){
        if (living!=null) {
            if (living.getItemBySlot(slot).is(item)) {
                return true;
            }
        }
        return false;
    }
    public static float getArcaneHarmonics(LivingEntity entity){
        if (entity != null) {
            AttributeInstance attributeInstance =  entity.getAttribute(MalumAttributes.ARCANE_RESONANCE);
            if (attributeInstance != null) {
                return (float) attributeInstance.getValue() * Config.getArcaneHarmonics().get().floatValue();
            }
        }else {
            return 1;
        }
        return 1;
    }

    public static float whiteArcaneHarmonics(LivingEntity entity){
        if (entity != null) {
            AttributeInstance attributeInstance =  entity.getAttribute(MalumAttributes.ARCANE_RESONANCE);
            if (attributeInstance != null) {
                float s = (float) attributeInstance.getValue();
                float doIt = 1 - s;
                if (doIt < 0.5f) {
                    doIt = 0.5f;
                }
                return 1 + doIt * Config.getArcaneHarmonics().get().floatValue();
            }
        }else {
            return 1;
        }
        return 1;
    }

    public static class SpiritSpawner {

        private final Entity target;
        @Nullable
        private LivingEntity preferredCollector;
        private final List<ItemStack> customItems = Collections.emptyList();

        public SpiritSpawner(Entity target) {
            this.target = target;
        }

        public SpiritSpawner setPreferredCollector(@Nullable LivingEntity preferredCollector) {
            this.preferredCollector = preferredCollector;
            return this;
        }

        public Vec3 getSpawnPosition() {
            return target.position().add(0, target.getBbHeight() / 2f, 0);
        }

        public void spawnSpirits(Level level) {
            var position = getSpawnPosition();
            ItemStack stack = new ItemStack(ItemRegs.BloodSpirit_.get());
            Entity entity = createSpiritEntity(stack, position);
            level.addFreshEntity(entity);
            float pitch = RandomHelper.randomBetween(level.random, 0.7f, 1.3f);
            level.playSound(null, position.x, position.y, position.z, MalumSoundEvents.SOUL_SHATTER, SoundSource.PLAYERS, 1.0F, pitch);
        }

        public Entity createSpiritEntity(ItemStack stack, Vec3 position) {
            var level = target.level();
            var random = level.getRandom();
            float speed = RandomHelper.randomBetween(random, 0.2f, 0.4f);
            float xSpeed = randomBetween(random, -speed, speed);
            float ySpeed = randomBetween(random, 0.05f, 0.06f);
            float zSpeed = randomBetween(random, -speed, speed);
            var velocity = new Vec3(xSpeed, ySpeed, zSpeed);
            if (CommonConfig.NO_FANCY_SPIRITS.getConfigValue()) {
                var itemEntity = new ItemEntity(level, position.x, position.y, position.z, stack);
                itemEntity.setDefaultPickUpDelay();
                itemEntity.setDeltaMovement(xSpeed * 0.6f, ySpeed * 6f, zSpeed * 0.6f);
                return itemEntity;
            }
            BloodSpirit spirit = new BloodSpirit(level, preferredCollector, stack, position, velocity);
            spirit.owner = preferredCollector;
            return spirit;
        }
    }

    public static List<ItemStack> applySpiritLootBonuses(EntitySpiritDropData data, LivingEntity target, LivingEntity attacker) {
        List<ItemStack> spirits = new ArrayList<>(data.getSpiritStacks());
        if (spirits.isEmpty()) {
            return spirits;
        }
        var random = attacker.getRandom();
        int bonus = Mth.floor(attacker.getAttributeValue(MalumAttributes.SPIRIT_SPOILS));
        var event = new ModifySpiritSpoilsEvent(target, attacker, bonus);
        ItemEventHandler.getEventResponders(attacker)
                .forEach(lookup -> lookup.run(IMalumEventResponder.class,
                        (eventResponderItem, stack) -> eventResponderItem.modifySpiritSpoilsEvent(event, attacker)));
        NeoForge.EVENT_BUS.post(event);
        bonus = event.getNewSpiritBonus();
        for (int i = 0; i < bonus; i++) {
            int index = random.nextInt(spirits.size());
            spirits.get(index).grow(1);
        }
        return spirits;
    }

    public static class RenderUtils {
        public static void renderEntityTrail(PoseStack poseStack, VFXBuilders.WorldVFXBuilder builder, TrailPointBuilder trailPointBuilder, Entity entity, Color primaryColor, Color secondaryColor, float effectScalar, float partialTicks) {
            renderEntityTrail(poseStack, builder, trailPointBuilder, entity, (Function<Float, Color>)((t) -> primaryColor), (Function<Float, Color>)((t) -> secondaryColor), effectScalar, effectScalar, partialTicks);
        }


        public static void renderEntityTrail(PoseStack poseStack, VFXBuilders.WorldVFXBuilder builder, TrailPointBuilder trailPointBuilder, Entity entity, Color primaryColor, Color secondaryColor, float scaleScalar, float alphaScalar, float partialTicks) {
            renderEntityTrail(poseStack, builder, trailPointBuilder, entity, (Function<Float, Color>)((t) -> primaryColor), (Function<Float, Color>)((t) -> secondaryColor), scaleScalar, alphaScalar, partialTicks);
        }

        public static void renderEntityTrail(PoseStack poseStack, VFXBuilders.WorldVFXBuilder builder, TrailPointBuilder trailPointBuilder, Entity entity, Function<Float, Color> primaryColor, Function<Float, Color> secondaryColor, float size, float alphaScalar, float partialTicks) {
            poseStack.pushPose();
            float trailOffsetX = (float)Mth.lerp((double)partialTicks, entity.xOld, entity.getX());
            float trailOffsetY = (float)Mth.lerp((double)partialTicks, entity.yOld, entity.getY());
            float trailOffsetZ = (float)Mth.lerp((double)partialTicks, entity.zOld, entity.getZ());
            poseStack.translate(-trailOffsetX, -trailOffsetY, -trailOffsetZ);
            float alpha = 0.9f * alphaScalar;
            builder.setAlpha(alpha).renderTrail(trailPointBuilder, (f) -> size, (f) -> builder.setAlpha(alpha * f).setColor(ColorHelper.colorLerp(Easing.SINE_IN, f * 2.0F, (Color)secondaryColor.apply(f), (Color)primaryColor.apply(f))));
            poseStack.translate(trailOffsetX, trailOffsetY, trailOffsetZ);
            poseStack.popPose();
        }
    }

}
