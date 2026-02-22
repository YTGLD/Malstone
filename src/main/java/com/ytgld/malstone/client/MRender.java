package com.ytgld.malstone.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;

import static org.lwjgl.opengl.GL11C.GL_LEQUAL;
import static org.lwjgl.opengl.GL11C.GL_LESS;

public class MRender extends RenderType{
    public static final TransparencyStateShard UNIFIED_TRANSPARENCY_STATE = new TransparencyStateShard("unified_transparency", () -> {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        RenderSystem.depthFunc(GL_LESS);
        RenderSystem.depthMask(false);

    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(GL_LEQUAL);
        RenderSystem.disableDepthTest();
    });


    public MRender(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }
    public static RenderType light = create("light",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS,
            1536, false,
            true, RenderType.CompositeState.builder().
                    setShaderState(RENDERTYPE_LIGHTNING_SHADER)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setTransparencyState(UNIFIED_TRANSPARENCY_STATE).createCompositeState(false));

    private static ShaderInstance liveShaderInstance;
    public static ShaderInstance getLiveShaderInstance() {
        return liveShaderInstance;
    }

    public static void set_liveShaderInstance(ShaderInstance liveShaderInstance) {
        MRender.liveShaderInstance = liveShaderInstance;
    }
}
