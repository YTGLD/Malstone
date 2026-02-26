package com.ytgld.malstone.items.white;

import com.sammy.malum.common.data.attachment.SoulWardData;
import com.sammy.malum.common.item.IVoidItem;
import com.sammy.malum.registry.common.MalumAttachmentTypes;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 破极兵刃
 * <p>
 * 即使你的攻击准星偏移了目标
 * <p>
 * 但在合理偏移范围内仍然可以攻击到目标
 * <p>
 * 进行补正的攻击几乎无法暴击
 */
public class BreakingTheWeapon extends WhiteArrow implements IVoidItem {
    public BreakingTheWeapon(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSpawn() {
        return true;
    }

    public static void pack(PlayerInteractEvent.LeftClickEmpty event){
        PacketDistributor.sendToServer(new UseCurio(ItemStack.EMPTY));
    }
    public static void pack(PlayerInteractEvent.LeftClickBlock event){
        PacketDistributor.sendToServer(new UseCurio(ItemStack.EMPTY));
    }
    public static void attack(Player player){
        if (!player.level().isClientSide()) {
            if (Handler.hascurio(player, ItemRegs.BreakingTheWeapon_.get())) {
                if (getPlayerLookTarget(player.level(), player) instanceof LivingEntity living) {

                    DamageContainer damageContainer =new DamageContainer(living.damageSources().playerAttack(player),
                            (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    LivingDamageEvent.Pre damageEvent = NeoForge.EVENT_BUS.post(new LivingDamageEvent.Pre(living,damageContainer));
                    living.hurt(damageContainer.getSource(),damageEvent.getNewDamage());
                }
            }
        }
    }
    public static void attackADamage(LivingDamageEvent.Pre event){
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.BreakingTheWeapon_.get())){
                SoulWardData handler = event.getEntity().getData(MalumAttachmentTypes.SOUL_WARD);
                double shield = handler.getSoulWard();
                float armor = (float) (event.getEntity().getArmorValue() + shield);
                armor /= 4;
                if (armor > maxArmorDamage(player)) {
                    armor = maxArmorDamage(player);
                }
                float c = 1;
                if (event.getEntity().getHealth() >= event.getEntity().getMaxHealth()) {
                    c *= healthDamage(player);
                }
                handler.setSoulWard(0);
                event.setNewDamage(event.getNewDamage() * c + armor);
            }
        }
    }
    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.translatable("item.malstone.breaking_the_weapon.text.1").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.breaking_the_weapon.text.2").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.breaking_the_weapon.text.3").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.translatable("item.malstone.breaking_the_weapon.text.4").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.breaking_the_weapon.text.5").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.translatable("item.malstone.breaking_the_weapon.text.6").setStyle(Style.EMPTY.withColor(color())));
        return new MalstoneText(stack,tooltipComponents);
    }
    public static Entity getPlayerLookTarget(Level level, Player living) {
        Entity pointedEntity = null;
        double range = living.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
        Vec3 srcVec = living.getEyePosition();
        Vec3 lookVec = living.getViewVector(1.0F);

        double maxAngle = du(living);
        Vec3 targetVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

        AABB aabb = new AABB(srcVec.x, srcVec.y, srcVec.z, targetVec.x, targetVec.y, targetVec.z);

        List<Entity> entitiesInRange = level.getEntities(living, aabb);

        for (Entity entity : entitiesInRange) {
            if (entity instanceof LivingEntity) {
                Vec3 entityPosition = entity.position().add(0, entity.getEyeHeight() * 0.5, 0);
                Vec3 directionToEntity = entityPosition.subtract(srcVec).normalize();

                double dotProduct = lookVec.dot(directionToEntity);
                double angle = Math.acos(dotProduct) * (180.0 / Math.PI);

                if (angle <= maxAngle) {
                    pointedEntity = entity;
                    break;
                }
            }
        }
        return pointedEntity;
    }
    public static float du(Player player){
        int d = Config.getTheBreakingTheWeapon().getAsInt();
        return Handler.doWhiteArcaneHarmonics(player, d);

    }
    public static float maxArmorDamage(Player player){
        float d = Config.getMaxArmorDamageBreakingTheWeapon().get().floatValue();
        return Handler.doWhiteArcaneHarmonics(player, d);
    }

    public static float healthDamage(Player player){
        float d = Config.getHealthDamageBreakingTheWeapon().get().floatValue();
        return Handler.doWhiteArcaneHarmonics(player, d);
    }
    public record UseCurio(ItemStack carried) implements CustomPacketPayload {

        public static final Type<UseCurio> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(Malstone.MODID, "breaking_the_weapon"));

        public static final StreamCodec<RegistryFriendlyByteBuf, UseCurio> USE_CURIO_STREAM_CODEC =
                StreamCodec.composite(
                        ItemStack.OPTIONAL_STREAM_CODEC,
                        UseCurio::carried,
                        UseCurio::new);

        @Nonnull
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
    public static class BreakingTheWeaponNetworkHandler {
        public static void register(final PayloadRegistrar registrar) {
            registrar.playToServer(UseCurio.TYPE, UseCurio.USE_CURIO_STREAM_CODEC,
                    handlerUse::handleOpenCurios);

        }
        public static HandlerUse handlerUse = new HandlerUse();
        public static class HandlerUse {
            public void handleOpenCurios(final UseCurio data, final IPayloadContext ctx) {
                ctx.enqueueWork(() -> {
                    Player player = ctx.player();
                    BreakingTheWeapon.attack(player);
                });
            }
        }
    }
}
