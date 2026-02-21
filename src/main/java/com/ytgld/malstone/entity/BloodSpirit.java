package com.ytgld.malstone.entity;

import com.sammy.malum.client.SpiritBasedParticleBuilder;
import com.sammy.malum.common.entity.FloatingItemDestinationData;
import com.sammy.malum.common.entity.FloatingItemEntity;
import com.sammy.malum.registry.common.MalumSoundEvents;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.entity.BloodSpirit;
import com.sammy.malum.common.item.spirit.SpiritShardItem;
import com.sammy.malum.core.systems.spirit.type.SpiritArcanaType;
import com.sammy.malum.core.systems.spirit.type.SpiritLike;
import com.sammy.malum.registry.common.MalumParticles;
import com.ytgld.malstone.items.KillTheGods;
import com.ytgld.malstone.magic.MalstoneSpirits;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.helpers.SoundHelper;
import team.lodestar.lodestone.helpers.VecHelper;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BloodSpirit extends FloatingItemEntity {
    public int soundCooldown;
    public LivingEntity owner;
    public int myAge;

    public BloodSpirit(Level level) {
        super(Entitys.BLOOD_SPIRIT.get(), level);
        this.soundCooldown = 20 + this.random.nextInt(100);
        this.maxAge = 3000;
    }

    public BloodSpirit(Level level, @Nullable LivingEntity owner, ItemStack stack, Vec3 position, Vec3 velocity) {
        this(level);
        this.owner = owner;
        if (owner != null) {
            this.setDestination(new FloatingItemDestinationData(owner));
        }
        canSeeTime = 30;
        myAge= 0;
        canSee = true;
        this.setItem(ItemStack.EMPTY);
        this.setPos(position);
        this.setDeltaMovement(velocity);
        this.maxAge = 4000;
        Item var7 = stack.getItem();
        if (var7 instanceof SpiritShardItem spiritShardItem) {
            this.setSpirit(spiritShardItem.getSpirit());
        }

    }

    @Override
    public float getVisualEffectScalar() {
        return 1.0f;
    }

    @Override
    public SpiritArcanaType getSpiritType() {
        return MalstoneSpirits.BLOOD.getSpirit();
    }

    public void collect(ServerLevel level) {
    }


    @Override
    public void remove(RemovalReason reason) {
        if (canSeeTime <= 0)
            super.remove(reason);
    }

    public boolean canSee = true;
    public int canSeeTime;
    public void tick() {
        if (canSee) {
            super.tick();
        }
        Level var2 = this.level();
        if (var2 instanceof ServerLevel) {
            this.age = 1;
            this.setXRot(0);
            this.setYBodyRot(0);
            this.setYRot(0);
            this.setRot(0,0);
            myAge++;
            if (myAge > maxAge){
                this.discard();
            }
            if (!canSee) {
                canSeeTime --;
                if (canSeeTime <= 0) {
                    this.discard();
                }
                this.setDeltaMovement(0,0,0);
            }else {
                if (this.owner != null && this.owner instanceof Player player) {
                    float speed = 0.33f;
                    Vec3 playerPos = this.position().add(0, 0.5, 0);
                    float range = 0.25f;
                    List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(playerPos.x - range, playerPos.y - range, playerPos.z - range, playerPos.x + range, playerPos.y + range, playerPos.z + range));
                    for (LivingEntity entity : entities) {
                        if (entity.is(this.owner)) {
                            float shield = player.getData(AttReg.BloodShield_.get());
                            if (shield < KillTheGods.maxShield(player)) {
                                SoundEvent sound = MalumSoundEvents.SPIRIT_PICKUP.get();
                                SoundHelper.playSound(player, sound, 0.4F, RandomHelper.randomBetween(player.getRandom(), 1, 1));
                                player.setData(AttReg.BloodShield_.get(),player.getData(AttReg.BloodShield_.get()) + (float) 1);
                            }
                            canSee = false;
                        }
                    }
                    Vec3 targetPos = this.owner.position().add(0, 0, 0);

                    Vec3 currentPos = this.position();
                    Vec3 direction = targetPos.subtract(currentPos).normalize();

                    Vec3 currentDirection = this.getDeltaMovement().normalize();

                    double angle = Math.acos(currentDirection.dot(direction)) * (180.0 / Math.PI);

                    if (angle > 40) {
                        double angleLimit = Math.toRadians(40);

                        Vec3 limitedDirection = currentDirection.scale(Math.cos(angleLimit))
                                .add(direction.normalize().scale(Math.sin(angleLimit)));

                        this.setDeltaMovement(limitedDirection.x * speed, limitedDirection.y * speed, limitedDirection.z * speed);
                    } else {
                        this.setDeltaMovement(direction.x * speed, direction.y * speed, direction.z * speed);
                    }
                }
            }
        } else {
            if (canSee) {
                Vec3 position = position();
                this.trail.addTrailPoint(position);
                this.longTrail.addTrailPoint(position);
                this.trail.tickTrailPoints();
                this.longTrail.tickTrailPoints();
            }
        }
    }
}

