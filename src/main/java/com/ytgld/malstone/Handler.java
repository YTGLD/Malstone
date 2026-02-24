package com.ytgld.malstone;

import com.sammy.malum.config.CommonConfig;
import com.sammy.malum.registry.common.MalumAttributes;
import com.sammy.malum.registry.common.MalumSoundEvents;
import com.ytgld.malstone.entity.BloodSpirit;
import com.ytgld.malstone.items.init.ItemRegs;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.helpers.RandomHelper;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

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
    public static float doArcaneHarmonics(LivingEntity entity,float dif){
        if (entity != null) {
            return getArcaneHarmonics(entity) * dif;
        }
        return dif;
    }

    private static float getArcaneHarmonics(LivingEntity entity){
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
    public static float doWhiteArcaneHarmonics(LivingEntity entity,float dif){
        if (entity != null) {
            return whiteArcaneHarmonics(entity) * dif;
        }
        return dif;
    }

    private static float whiteArcaneHarmonics(LivingEntity entity){
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
            float speed = RandomHelper.randomBetween(random, 0.4f, 0.8f);
            float xSpeed = randomBetween(random, -speed, speed);
            float ySpeed = randomBetween(random, 0.1f, 0.15f);
            float zSpeed = randomBetween(random, -speed, speed);
            var velocity = new Vec3(xSpeed, ySpeed, zSpeed);
            if (CommonConfig.NO_FANCY_SPIRITS.getConfigValue()) {
                var itemEntity = new ItemEntity(level, position.x, position.y, position.z, stack);
                itemEntity.setDefaultPickUpDelay();
                itemEntity.setDeltaMovement(xSpeed * 0.6f, ySpeed * 6f, zSpeed * 0.6f);
                return itemEntity;
            }
            BloodSpirit spirit = new BloodSpirit(level, preferredCollector, stack, position, velocity);
            spirit.setOwner(preferredCollector);
            return spirit;
        }
    }

}
