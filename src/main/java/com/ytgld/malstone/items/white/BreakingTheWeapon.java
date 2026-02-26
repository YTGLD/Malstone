package com.ytgld.malstone.items.white;

import com.sammy.malum.common.capability.MalumPlayerDataCapability;
import com.sammy.malum.common.item.IVoidItem;
import com.sammy.malum.core.handlers.SoulWardHandler;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.WhiteArrow;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
        BreakingTheWeapon.INSTANCE.send(PacketDistributor.SERVER.noArg(),
                new CPacketOpenCurios(ItemStack.EMPTY));

    }
    public static void pack(PlayerInteractEvent.LeftClickBlock event){
        BreakingTheWeapon.INSTANCE.send(PacketDistributor.SERVER.noArg(),
                new CPacketOpenCurios(ItemStack.EMPTY));
    }
    public static void attack(Player player){
        if (!player.level().isClientSide()) {
            if (Handler.hascurio(player, ItemRegs.BreakingTheWeapon_.get())) {
                if (getPlayerLookTarget(player.level(), player) instanceof LivingEntity living) {
                    LivingDamageEvent damageEvent =  new LivingDamageEvent(living,
                            living.damageSources().playerAttack(player),
                            (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    living.hurt(damageEvent.getSource(),damageEvent.getAmount());
                }
            }
        }
    }
    public static void attackADamage(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.BreakingTheWeapon_.get())) {
                double shield = 0;
                if (event.getEntity() instanceof Player e) {
                    SoulWardHandler handler = MalumPlayerDataCapability.getCapability(e).soulWardHandler;
                    shield = handler.soulWard;
                    handler.soulWard = 0;
                }
                float armor = (float) (event.getEntity().getArmorValue() + shield);
                armor /= 4;
                if (armor > maxArmorDamage(player)) {
                    armor = maxArmorDamage(player);
                }
                float c = 1;
                if (event.getEntity().getHealth() >= event.getEntity().getMaxHealth()) {
                    c *= healthDamage(player);
                }
                event.setAmount(event.getAmount() * c + armor);
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
        double range = living.getAttributeValue(ForgeMod.ENTITY_REACH.get());
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
        int d = Config.getTheBreakingTheWeapon().get();
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



    public static SimpleChannel INSTANCE;
    private static final String PTC_VERSION = "1";
    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Malstone.MODID, "main"))
                .networkProtocolVersion(() -> PTC_VERSION).clientAcceptedVersions(PTC_VERSION::equals)
                .serverAcceptedVersions(PTC_VERSION::equals).simpleChannel();
        register(CPacketOpenCurios.class, CPacketOpenCurios::encode, CPacketOpenCurios::decode,
                CPacketOpenCurios::handle);
    }
    private static int id = 0;
    private static <M> void register(Class<M> messageType, BiConsumer<M, FriendlyByteBuf> encoder,
                                     Function<FriendlyByteBuf, M> decoder,
                                     BiConsumer<M, Supplier<NetworkEvent.Context>> messageConsumer) {
        INSTANCE.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
    }
    public static class CPacketOpenCurios {

        private final ItemStack carried;

        public CPacketOpenCurios(ItemStack stack) {
            this.carried = stack;
        }

        public static void encode(CPacketOpenCurios msg, FriendlyByteBuf buf) {
            buf.writeItem(msg.carried);
        }

        public static CPacketOpenCurios decode(FriendlyByteBuf buf) {
            return new CPacketOpenCurios(buf.readItem());
        }

        public static void handle(CPacketOpenCurios msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    attack(player);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
