package com.ytgld.malstone.mixin.malum;

import com.sammy.malum.common.entity.FloatingItemEntity;
import com.sammy.malum.common.entity.spirit.SpiritItemEntity;
import com.ytgld.malstone.items.twisted.Condenser;
import com.ytgld.malstone.items.twisted.EvilEngine;
import com.ytgld.malstone.items.twisted.ExtremelyDead;
import com.ytgld.malstone.items.twisted.WeepingImmortal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SpiritItemEntity.class,remap = false)
public abstract class SpiritItemEntityMixin extends FloatingItemEntity {
    public SpiritItemEntityMixin(EntityType<? extends FloatingItemEntity> type, Level level) {
        super(type, level);
    }
    @Inject(method = "collect", at = @At(value = "HEAD"))
    private void collect(CallbackInfo ci) {
        if (this.owner instanceof Player collector) {
            EvilEngine.killThis(collector, (SpiritItemEntity) (Object) this);
            WeepingImmortal.pickUp(collector, this.getItem());
            ExtremelyDead.killThis(collector, (SpiritItemEntity) (Object) this);
        }
    }
    @Inject(method = "tick", at = @At(value = "RETURN"))
    private void tick$Malstone(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel level) {
            if (this.owner instanceof Player collector) {
                Condenser.tpPlayer(collector, (SpiritItemEntity) (Object) this);
                ExtremelyDead.flyDamage(collector, (SpiritItemEntity) (Object) this);
            }
        }
    }
}
