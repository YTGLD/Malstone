package com.ytgld.malstone.client;

import net.minecraft.client.renderer.ShaderInstance;

public class MRender {
    private static ShaderInstance liveShaderInstance;

    public static ShaderInstance getLiveShaderInstance() {
        return liveShaderInstance;
    }

    public static void set_liveShaderInstance(ShaderInstance liveShaderInstance) {
        MRender.liveShaderInstance = liveShaderInstance;
    }
}
