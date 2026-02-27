package com.ytgld.malstone.items.twisted;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sammy.malum.common.entity.spirit.SpiritItemEntity;
import com.sammy.malum.registry.common.MalumAttributes;
import com.sammy.malum.registry.common.item.MalumItems;
import com.ytgld.malstone.Config;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.items.init.BaseItem;
import com.ytgld.malstone.items.init.ItemRegs;
import com.ytgld.malstone.items.init.Twisted;
import com.ytgld.malstone.magic.DataReg;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.registry.common.LodestoneAttributes;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * 极殇
 * <p>
 * 天然精魂在飞行过程中会对经过生物造成伤害，同时削弱目标
 * <p>
 * 天然精魂不再可以被拾取，并在接触后短暂提高自身的魔法伤害
 * <p>
 * 若被哭泣之井强化，则可能在杀死目标时产生幽影精魂
 * <p>
 * 强化状态下获得大量的精魂劫掠等级
 */
public class ExtremelyDead extends Twisted {
    public static final String  magicDAMAGE = "ExtremelyDeadmagicDAMAGE";
    public ExtremelyDead(Properties properties) {
        super(properties);
    }
    public static void killThis(LivingEntity player , ItemStack spiritItemEntity, CallbackInfo ci){
        if (Handler.hascurio(player, ItemRegs.ExtremelyDead_.get())) {
            if (!spiritItemEntity.is(MalumItems.UMBRAL_SPIRIT.get())) {
                CompoundTag compoundTag = player.getPersistentData();
                if (compoundTag.getInt(magicDAMAGE) < 100) {
                    compoundTag.putInt(magicDAMAGE,compoundTag.getInt(magicDAMAGE) +1);
                }
                spiritItemEntity.shrink(1);
                ci.cancel();
            }
        }
    }

    @Override
    public boolean canUseWeepingPower() {
        return true;
    }

    @Override
    public int maxWeepingPower(ItemStack stack) {
        return 2400;
    }

    public static void killBlack(LivingDeathEvent event){
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.hascurio(player, ItemRegs.ExtremelyDead_.get())) {
                if (Mth.nextInt(RandomSource.create(),1,100) <= lvl(player)) {
                    CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                        Map<String, ICurioStacksHandler> curios = handler.getCurios();
                        for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                            ICurioStacksHandler stacksHandler = entry.getValue();
                            IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                            for (int i = 0; i < stacksHandler.getSlots(); i++) {
                                ItemStack stack = stackHandler.getStackInSlot(i);
                                if (stack.is(ItemRegs.ExtremelyDead_.get())) {
                                    if (stack.getItem() instanceof BaseItem item) {
                                        if (item.canUseWeepingPower() && item.hasWeepingWllPower(stack)) {
                                            SpiritItemEntity entity = new SpiritItemEntity(player.level(), player, new ItemStack(MalumItems.UMBRAL_SPIRIT), event.getEntity().position().add(0, 1, 0), new Vec3(0, 0, 0));
                                            player.level().addFreshEntity(entity);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    });
                }

            }
        }
    }
    public static void flyDamage(LivingEntity player ,SpiritItemEntity spiritItemEntity){
        if (Handler.hascurio(player, ItemRegs.ExtremelyDead_.get())) {
            if (spiritItemEntity.tickCount % 4  == 1) {
                Vec3 playerPos = player.position();
                int range = 1;
                List<LivingEntity> entitiesOfClass = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(playerPos.x - range, playerPos.y - range, playerPos.z - range, playerPos.x + range, playerPos.y + range, playerPos.z + range));
                for (LivingEntity entity : entitiesOfClass){
                    if (!entity.is(player) && player instanceof Player doi) {
                        entity.invulnerableTime = 0;
                        float damage = damage(doi);
                        if (!entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,0,false,false))){
                            damage *= 2f;
                        }
                        entity.hurt(entity.damageSources().playerAttack(doi),damage);
                    }
                }
            }
        }
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity() instanceof Player player) {
            if (!player.level().isClientSide()) {
                player.getAttributes().addTransientAttributeModifiers(doAttribute(stack,player));
                if (player.tickCount % 20 == 1) {
                    CompoundTag compoundTag = stack.get(DataReg.tag);
                    if (compoundTag.getInt(magicDAMAGE) > 0) {
                        compoundTag.putInt(magicDAMAGE,compoundTag.getInt(magicDAMAGE) - 4);
                    }
                }
            }
        }
    }
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide()) {
            entity.getAttributes().removeAttributeModifiers(doAttribute(stack,entity));
        }
    }
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(ItemStack stack, LivingEntity entity) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();

        float add = 0;
        if (this.hasWeepingWllPower(stack)) {
            add = entity.getPersistentData().getInt(magicDAMAGE) / 100f;
        }


        float spirit = 0;
        if (this.hasWeepingWllPower(stack)) {
            spirit = 5;
        }
        modifiers.put(MalumAttributes.SPIRIT_SPOILS,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                spirit, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(LodestoneAttributes.MAGIC_DAMAGE,new AttributeModifier(ResourceLocation.parse(this.getDescriptionId()),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        return modifiers;
    }
    public static int lvl(Player player){
        return (int) Handler.doArcaneHarmonics(player,Config.getLvlExtremelyDead().get());
    }

    public static float damage(Player player){
        return Handler.doArcaneHarmonics(player,Config.getDamageExtremelyDead().get().floatValue());
    }
    @Override
    public @Nullable MalstoneText malstoneText(ItemStack stack, List<Component> tooltipComponents) {
//        tooltipComponents.add(Component.translatable("item.malstone.extremely_dead.text.1").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.extremely_dead.text.2").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.extremely_dead.text.3").setStyle(Style.EMPTY.withColor(color())));
        tooltipComponents.add(Component.translatable("item.malstone.extremely_dead.text.4").setStyle(Style.EMPTY.withColor(color())));
        return new MalstoneText(stack,tooltipComponents);
    }
}