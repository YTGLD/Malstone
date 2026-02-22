package com.ytgld.malstone.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.sammy.malum.client.RenderUtils;
import com.sammy.malum.client.SpiritBasedWorldVFXBuilder;
import com.sammy.malum.core.systems.spirit.type.SpiritArcanaType;
import com.sammy.malum.registry.client.MalumRenderTypeTokens;
import com.ytgld.malstone.Handler;
import com.ytgld.malstone.Light;
import com.ytgld.malstone.client.MRender;
import com.ytgld.malstone.entity.BloodSpirit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
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
        double x = Mth.lerp(partialTicks, entity.xOld, entity.getX());
        double y = Mth.lerp(partialTicks, entity.yOld, entity.getY());
        double z = Mth.lerp(partialTicks, entity.zOld, entity.getZ());
        poseStack.pushPose();
        poseStack.translate(entity.getX()-x, entity.getY()-y,entity.getZ() -z);
        setT(poseStack,entity,bufferIn);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }
    private void setT(PoseStack matrices, BloodSpirit entity, MultiBufferSource bufferIn) {
        matrices.pushPose(); // 保存当前的矩阵状态

        // 遍历 entity 的拖尾位置
        for (int i = 1; i < entity.getTrailPositions().size(); i++) {
            Vec3 prevPos = entity.getTrailPositions().get(i - 1);
            Vec3 currPos = entity.getTrailPositions().get(i);

            Vec3 adjustedPrevPos = new Vec3(prevPos.x - entity.getX(), prevPos.y - entity.getY(), prevPos.z - entity.getZ());
            Vec3 adjustedCurrPos = new Vec3(currPos.x - entity.getX(), currPos.y - entity.getY(), currPos.z - entity.getZ());

            float alpha = (float)i / (float)(entity.getTrailPositions().size());
            int color = Light.ARGB.color((int)(150 * alpha), 255, (int) (100*(1-alpha)), 0);
            float size = entity.canSeeTime / 100f / 2.5f;
            matrices.pushPose();
            matrices.translate(adjustedPrevPos.x, adjustedPrevPos.y, adjustedPrevPos.z);
            matrices.scale(size,size,size);
            new RenderUtils(color).renderCube(matrices, bufferIn, MRender.light, alpha);
            matrices.popPose();

            matrices.pushPose();
            matrices.translate(adjustedCurrPos.x, adjustedCurrPos.y, adjustedCurrPos.z);
            matrices.scale(size,size,size);
            new RenderUtils(color).renderCube(matrices, bufferIn, MRender.light, alpha);
            matrices.popPose();
        }

        matrices.popPose(); // 恢复矩阵状态
    }

    public record RenderUtils(int color) {

        public void renderCube(PoseStack poseStack, MultiBufferSource bufferSource,
                               RenderType renderType, float size) {

            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

            Matrix4f matrix = poseStack.last().pose();

            float halfSize = size / 2.0F;

            poseStack.pushPose();

            addVertex(vertexConsumer, matrix, -halfSize, -halfSize, -halfSize, 1.0f, 0.0f, 0.0f, 1.0f); // Front Bottom Left
            addVertex(vertexConsumer, matrix, halfSize, -halfSize, -halfSize, 0.0f, 1.0f, 0.0f, 1.0f);  // Front Bottom Right
            addVertex(vertexConsumer, matrix, halfSize, halfSize, -halfSize, 0.0f, 0.0f, 1.0f, 1.0f);   // Front Top Right
            addVertex(vertexConsumer, matrix, -halfSize, halfSize, -halfSize, 1.0f, 1.0f, 0.0f, 1.0f);  // Front Top Left

// 后面
            addVertex(vertexConsumer, matrix, -halfSize, -halfSize, halfSize, 1.0f, 0.0f, 0.0f, 1.0f);  // Back Bottom Left
            addVertex(vertexConsumer, matrix, halfSize, -halfSize, halfSize, 0.0f, 1.0f, 0.0f, 1.0f);   // Back Bottom Right
            addVertex(vertexConsumer, matrix, halfSize, halfSize, halfSize, 0.0f, 0.0f, 1.0f, 1.0f);    // Back Top Right
            addVertex(vertexConsumer, matrix, -halfSize, halfSize, halfSize, 1.0f, 1.0f, 0.0f, 1.0f);   // Back Top Left

// 左面
            addVertex(vertexConsumer, matrix, -halfSize, -halfSize, -halfSize, 1.0f, 0.0f, 0.0f, 1.0f);  // Front Bottom Left
            addVertex(vertexConsumer, matrix, -halfSize, -halfSize, halfSize, 0.0f, 1.0f, 0.0f, 1.0f);   // Back Bottom Left
            addVertex(vertexConsumer, matrix, -halfSize, halfSize, halfSize, 0.0f, 0.0f, 1.0f, 1.0f);    // Back Top Left
            addVertex(vertexConsumer, matrix, -halfSize, halfSize, -halfSize, 1.0f, 1.0f, 0.0f, 1.0f);   // Front Top Left

// 右面
            addVertex(vertexConsumer, matrix, halfSize, -halfSize, -halfSize, 1.0f, 0.0f, 0.0f, 1.0f);   // Front Bottom Right
            addVertex(vertexConsumer, matrix, halfSize, -halfSize, halfSize, 0.0f, 1.0f, 0.0f, 1.0f);    // Back Bottom Right
            addVertex(vertexConsumer, matrix, halfSize, halfSize, halfSize, 0.0f, 0.0f, 1.0f, 1.0f);     // Back Top Right
            addVertex(vertexConsumer, matrix, halfSize, halfSize, -halfSize, 1.0f, 1.0f, 0.0f, 1.0f);    // Front Top Right

// 顶面
            addVertex(vertexConsumer, matrix, -halfSize, halfSize, -halfSize, 1.0f, 0.0f, 0.0f, 1.0f);    // Front Top Left
            addVertex(vertexConsumer, matrix, halfSize, halfSize, -halfSize, 0.0f, 1.0f, 0.0f, 1.0f);     // Front Top Right
            addVertex(vertexConsumer, matrix, halfSize, halfSize, halfSize, 0.0f, 0.0f, 1.0f, 1.0f);      // Back Top Right
            addVertex(vertexConsumer, matrix, -halfSize, halfSize, halfSize, 1.0f, 1.0f, 0.0f, 1.0f);     // Back Top Left

// 底面
            addVertex(vertexConsumer, matrix, -halfSize, -halfSize, -halfSize, 1.0f, 0.0f, 0.0f, 1.0f);   // Front Bottom Left
            addVertex(vertexConsumer, matrix, halfSize, -halfSize, -halfSize, 0.0f, 1.0f, 0.0f, 1.0f);    // Front Bottom Right
            addVertex(vertexConsumer, matrix, halfSize, -halfSize, halfSize, 0.0f, 0.0f, 1.0f, 1.0f);     // Back Bottom Right
            addVertex(vertexConsumer, matrix, -halfSize, -halfSize, halfSize, 1.0f, 1.0f, 0.0f, 1.0f);    // Back Bottom Left

// 恢复变换矩阵
            poseStack.popPose();

            // 继续为其他面绘制顶点...

        }

        private void addVertex(VertexConsumer vertexConsumer, Matrix4f matrix,
                               float x, float y, float z, float r, float g, float b, float a) {
            // 绘制一个顶点，参数为位置和颜色
            vertexConsumer.addVertex(matrix, x, y, z).setColor(color)
                    .setUv2(255, 255)
                    .setNormal(0, 0, 1);
        }
        public void renderColor(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 start, Vec3 end, RenderType renderType, float size) {
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

            // 正方体的8个顶点，分别是上面和下面的四个角
            Vec3[] vertices = new Vec3[8];
            for (int i = 0; i < 8; i++) {
                double offsetX = (i & 1) == 0 ? -size : size;
                double offsetY = (i & 2) == 0 ? 0 : size;
                double offsetZ = (i & 4) == 0 ? -size : size;
                // 上下偏移，分别对应start和end
                vertices[i] = (i < 4) ? start.add(offsetX, offsetY, offsetZ) : end.add(offsetX, offsetY, offsetZ);
            }

            // 渲染正方体的6个面，每个面是由两个三角形组成
            addCubeFace(vertexConsumer, poseStack, vertices[0], vertices[1], vertices[2], vertices[3]);  // 前面
            addCubeFace(vertexConsumer, poseStack, vertices[4], vertices[5], vertices[6], vertices[7]);  // 后面
            addCubeFace(vertexConsumer, poseStack, vertices[0], vertices[1], vertices[4], vertices[5]);  // 左面
            addCubeFace(vertexConsumer, poseStack, vertices[2], vertices[3], vertices[6], vertices[7]);  // 右面
            addCubeFace(vertexConsumer, poseStack, vertices[1], vertices[2], vertices[5], vertices[6]);  // 上面
            addCubeFace(vertexConsumer, poseStack, vertices[0], vertices[3], vertices[4], vertices[7]);  // 下面
        }
        private void addCubeFace(VertexConsumer vertexConsumer, PoseStack poseStack, Vec3 p1, Vec3 p2, Vec3 p3, Vec3 p4) {
            // 第一个三角形
            vertexConsumer.addVertex(poseStack.last().pose(), (float) p1.x, (float) p1.y, (float) p1.z)
                    .setColor(color)
                    .setUv2(255, 255)
                    .setNormal(0, 0, 1);

            vertexConsumer.addVertex(poseStack.last().pose(), (float) p2.x, (float) p2.y, (float) p2.z)
                    .setColor(color)
                    .setUv2(255, 255)
                    .setNormal(0, 0, 1);

            vertexConsumer.addVertex(poseStack.last().pose(), (float) p3.x, (float) p3.y, (float) p3.z)
                    .setColor(color)
                    .setUv2(255, 255)
                    .setNormal(0, 0, 1);

            // 第二个三角形
            vertexConsumer.addVertex(poseStack.last().pose(), (float) p3.x, (float) p3.y, (float) p3.z)
                    .setColor(color)
                    .setUv2(255, 255)
                    .setNormal(0, 0, 1);

            vertexConsumer.addVertex(poseStack.last().pose(), (float) p4.x, (float) p4.y, (float) p4.z)
                    .setColor(color)
                    .setUv2(255, 255)
                    .setNormal(0, 0, 1);

            vertexConsumer.addVertex(poseStack.last().pose(), (float) p1.x, (float) p1.y, (float) p1.z)
                    .setColor(color)
                    .setUv2(255, 255)
                    .setNormal(0, 0, 1);
        }
    }

    public ResourceLocation getTextureLocation(BloodSpirit entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}

