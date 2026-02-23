package com.ytgld.malstone.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sammy.malum.registry.client.MalumShaders;
import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.attribute.AttReg;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.opengl.GL11;
import team.lodestar.lodestone.registry.client.LodestoneShaders;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

public class ShieldRenderHandler {

    public static ResourceLocation SOUL_WARD = ResourceLocation.fromNamespaceAndPath(Malstone.MODID
            ,"textures/gui/hud/decay_shield.png");
    public static ResourceLocation GLOW = ResourceLocation.fromNamespaceAndPath(Malstone.MODID
            ,"textures/gui/hud/decay_shield_glow.png");
    public static ResourceLocation EMPTY = ResourceLocation.fromNamespaceAndPath(Malstone.MODID
            ,"textures/gui/hud/decay_shield_empty.png");
    public static ResourceLocation DISSOLVEMENT = ResourceLocation.fromNamespaceAndPath(Malstone.MODID
            ,"textures/gui/hud/decay_shield_dissolvement.png");

    public static int glow;
    public static int fadeout;

    public static double lastShield;
    public static float displayedShield;


    public static float sizeHeartBeat;

    public static void tick(ClientTickEvent event) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            double max = player.getAttributeValue(AttReg.MaxDecayShield);
            double now = player.getData(AttReg.DecayShield);
            if (now >= max) {
                if (glow < 40) {
                    glow++;
                }
            } else {
                if (glow > 0) {
                    glow--;
                }
            }
            if (lastShield != now) {
                glow = 15;
            }
            lastShield = now;
            displayedShield = Mth.lerp(0.2f, displayedShield, (float) now);
            if (now > 0 && now < max) {
                if (fadeout > 0) {
                    fadeout = Math.max(0, fadeout - 10);
                }
            } else {
                if (fadeout < 80) {
                    fadeout++;
                }
            }
            float sin = (float) Math.sin((player.tickCount / 3f) / Math.max(Math.sqrt(now),1));
            if (sin < 0) {
                sin = -sin;
            }
            if (sin > 0.8) {
                sizeHeartBeat = 1.05f;
                if (sin > 0.85) {
                    sizeHeartBeat = 1.1f;
                    if (sin > 0.9f) {
                        sizeHeartBeat = 1.15f;
                    }
                }
            }else {
                sizeHeartBeat = 1f;
            }

        }
    }

    public static void renderShield(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        var minecraft = Minecraft.getInstance();
        var poseStack = guiGraphics.pose();
        if (!minecraft.options.hideGui) {
            var player = minecraft.player;
            if (player != null && minecraft.level != null) {
                if (!player.isCreative() && !player.isSpectator()) {
                    double maxShield = player.getAttributeValue(AttReg.MaxDecayShield);
                    poseStack.pushPose();
                    int left = guiGraphics.guiWidth() / 2;
                    int top = guiGraphics.guiHeight() - 47;

                    // 缩放因子

                    // 如果有显示的护盾并且最大护盾大于0
                    if (displayedShield > 0 && maxShield > 0) {
                        float delta = (float) (displayedShield / maxShield);
                        float dissolvement = Easing.QUAD_OUT.ease(delta, 0, 1f);
                        float alpha = (1 - fadeout / 80f) * 0.75f;
                        poseStack.pushPose();

                        RenderSystem.depthMask(true);
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        var distorted = LodestoneShaders.SCREEN_DISTORTED_TEXTURE.getShaderInstance();
                        distorted.safeGetUniform("YFrequency").set(24f);
                        distorted.safeGetUniform("XFrequency").set(16f);
                        distorted.safeGetUniform("Speed").set(1000f);
                        distorted.safeGetUniform("Intensity").set(160f);
                        distorted.safeGetUniform("Width").set(64f);
                        distorted.safeGetUniform("Height").set(64f);

                        var builder = VFXBuilders.createScreen().setShader(distorted);
                        builder.setPositionWithWidth(
                                left - (32 * delta * sizeHeartBeat) /2,
                                top -  (32 * delta * sizeHeartBeat) /2,
                                32 * delta * sizeHeartBeat,
                                32 * delta * sizeHeartBeat
                        );
                        builder.setAlpha(alpha).setTexture(EMPTY).blit(poseStack);

                        var hud = MalumShaders.DISSOLVING_HUD_ELEMENT.getShaderInstance();
                        RenderSystem.setShaderTexture(1, DISSOLVEMENT);
                        hud.safeGetUniform("YFrequency").set(24f);
                        hud.safeGetUniform("XFrequency").set(16f);
                        hud.safeGetUniform("Speed").set(1000f);
                        hud.safeGetUniform("Intensity").set(160f);
                        hud.safeGetUniform("Dissolvement").set(dissolvement);
                        hud.safeGetUniform("Width").set(64f);
                        hud.safeGetUniform("Height").set(64f);

                        builder.setShader(hud).setTexture(SOUL_WARD).blit(poseStack);
                        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                        builder.setAlpha(0.2f * alpha).blit(poseStack);

                        if (glow > 0 && glow < 40) {
                            float time = minecraft.level.getGameTime() + deltaTracker.getGameTimeDeltaPartialTick(true);
                            float glowAlpha = (20 - Math.abs(20 - glow)) / 20f;
                            int angle = Mth.floor((time * 20) % 360);
                            float range = Easing.SINE_IN_OUT.ease(glowAlpha, 0, 320f);
                            var light = LodestoneShaders.RADIAL_DISTORTED_SCREEN_LIGHT.getShaderInstance();
                            light.safeGetUniform("YFrequency").set(24f);
                            light.safeGetUniform("XFrequency").set(16f);
                            light.safeGetUniform("Speed").set(1000f);
                            light.safeGetUniform("Intensity").set(160f);
                            light.safeGetUniform("Width").set(64f);
                            light.safeGetUniform("Height").set(64f);
                            light.safeGetUniform("Angle").set(angle);
                            light.safeGetUniform("LightAngleRange").set(range);
                            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                            builder.setShader(light).setAlpha(alpha);

                            if (displayedShield >= maxShield) {
                                builder.setTexture(SOUL_WARD).blit(poseStack);
                            }
                            builder.setTexture(GLOW).blit(poseStack);
                            light.setUniformDefaults();
                        }

                        distorted.setUniformDefaults();
                        hud.setUniformDefaults();
                        RenderSystem.defaultBlendFunc();
                        RenderSystem.disableBlend();
                        poseStack.popPose();
                    }
                    poseStack.popPose();
                }
            }
        }
    }
}
