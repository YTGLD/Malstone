package com.ytgld.malstone.entity;

import com.sammy.malum.common.entity.FloatingItemDestinationData;
import com.sammy.malum.common.entity.FloatingItemEntity;
import com.sammy.malum.registry.common.MalumSoundEvents;
import com.ytgld.malstone.attribute.AttReg;
import com.sammy.malum.common.item.spirit.SpiritShardItem;
import com.sammy.malum.core.systems.spirit.type.SpiritArcanaType;
import com.ytgld.malstone.items.KillTheGods;
import com.ytgld.malstone.magic.MalstoneSpirits;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.helpers.SoundHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BloodSpirit extends ThrowableItemProjectile {
    public int soundCooldown;
    public int myAge;

    public BloodSpirit(Level level) {
        super(Entitys.BLOOD_SPIRIT.get(), level);
        this.soundCooldown = 20 + this.random.nextInt(100);
    }


    public BloodSpirit(Level level, @Nullable LivingEntity owner, ItemStack stack, Vec3 position, Vec3 velocity) {
        this(level);
        canSeeTime = 100;
        this.setNoGravity(true);
        myAge= 0;
        canSee = true;
        this.setItem(ItemStack.EMPTY);
        this.setPos(position);
        this.setDeltaMovement(velocity);
    }
    public boolean canSee = true;
    public int canSeeTime = 100;


    private final List<Vec3> trailPositions = new ArrayList<>();

    public List<Vec3> getTrailPositions() {
        return trailPositions;
    }

    public void tick() {
        super.tick();

        if (canSee) {
            if (this.getOwner() instanceof Player player) {
                float speed = 0.5f;
                Vec3 playerPos = this.position().add(0, 0.5, 0);
                float range = 0.25f;
                List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(playerPos.x - range, playerPos.y - range, playerPos.z - range, playerPos.x + range, playerPos.y + range, playerPos.z + range));
                for (LivingEntity entity : entities) {
                    if (entity.is(player)) {
                        float shield = player.getData(AttReg.BloodShield_.get());
                        if (shield < KillTheGods.maxShield(player)) {
                            SoundEvent sound = MalumSoundEvents.SPIRIT_PICKUP.get();
                            SoundHelper.playSound(player, sound, 0.4F, RandomHelper.randomBetween(player.getRandom(), 1, 1));
                            player.setData(AttReg.BloodShield_.get(),player.getData(AttReg.BloodShield_.get()) + (float) 1);
                        }
                        canSee = false;
                    }
                }
                if (tickCount > 20) {

                    Vec3 targetPos = player.position().add(0, 0, 0); // 将 Y 坐标增加 heightOffset

                    Vec3 currentPos = this.position();
                    Vec3 direction = targetPos.subtract(currentPos).normalize();

                    Vec3 currentDirection = this.getDeltaMovement().normalize();

                    double angle = Math.acos(currentDirection.dot(direction)) * (180.0 / Math.PI);

                    if (angle > 25) {
                        double angleLimit = Math.toRadians(25); // 将5度转为弧度

                        Vec3 limitedDirection = currentDirection.scale(Math.cos(angleLimit)) // 计算缩放因子
                                .add(direction.normalize().scale(Math.sin(angleLimit))); // 根据目标方向进行调整

                        this.setDeltaMovement(limitedDirection.x * speed, limitedDirection.y * speed, limitedDirection.z * speed);
                    } else {
                        this.setDeltaMovement(direction.x * speed, direction.y * speed, direction.z * speed);
                    }
                }
            }
        }else {
            setDeltaMovement(0,0,0);
        }

        if (this.tickCount > 400) {
            canSee = false;
        }
        if (canSee) {
            trailPositions.add(new Vec3(this.getX(), this.getY(), this.getZ()));
        }
        if (!trailPositions.isEmpty()) {
            if (trailPositions.size() > 25||!canSee) {
                trailPositions.removeFirst();
            }
        }
        if (!canSee) {
            canSeeTime--;
        }
        if (canSeeTime<= 0) {
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ITEM_FRAME.asItem();
    }
}

