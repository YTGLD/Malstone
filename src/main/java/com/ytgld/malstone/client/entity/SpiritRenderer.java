package com.ytgld.malstone.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.malum.client.RenderUtils;
import com.sammy.malum.client.SpiritBasedWorldVFXBuilder;
import com.sammy.malum.core.systems.spirit.type.SpiritArcanaType;
import com.sammy.malum.registry.client.MalumRenderTypeTokens;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.entity.BloodSpirit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.rendering.rendeertype.LodestoneRenderTypeBuilder;

import java.awt.*;

public class SpiritRenderer extends EntityRenderer<BloodSpirit> {
    public final ItemRenderer itemRenderer;

    public SpiritRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.0F;
        this.shadowStrength = 0.0F;
    }

    @Override
    public boolean shouldRender(BloodSpirit livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public void render(BloodSpirit entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        SpiritArcanaType spiritType = entity.getSpiritType();
        LodestoneRenderTypeBuilder trail = LodestoneRenderTypes.ADDITIVE_TEXTURE.apply(MalumRenderTypeTokens.CONCENTRATED_TRAIL);
        LodestoneRenderTypeBuilder longTrail = LodestoneRenderTypes.ADDITIVE_TEXTURE.apply(MalumRenderTypeTokens.CONCENTRATED_TRAIL);
        SpiritBasedWorldVFXBuilder builder = SpiritBasedWorldVFXBuilder.create(spiritType);
        float effectScalar = entity.getVisualEffectScalar();
        RenderUtils.renderEntityTrail(poseStack, builder.setRenderType(longTrail), entity.longTrail, entity, spiritType.getSecondaryColor(), spiritType.getPrimaryColor(), effectScalar * 0.33f, effectScalar * 0.2F, partialTicks);

        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }
    public ResourceLocation getTextureLocation(BloodSpirit entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}

