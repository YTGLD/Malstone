#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform float GameTime;

uniform vec4 ColorModulator;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;
void main() {
    vec2 center = vec2(0.0, 0.0); // 纹理的中心点
    vec2 diff = texCoord0 - center; // 当前坐标与中心点的差值
    float distance = length(diff); // 当前坐标到中心点的距离

    // 动态扭曲频率变化
    float frequency = 10.0 + sin(GameTime * 3.0) * 5.0; // 动态频率

    // 引入多个波动和噪声纹理的变化
    float twistFactor1 = sin(((GameTime + 1) * 5000) + distance * frequency) * 0.0135;
    float twistFactor2 = cos(((GameTime + 2) * 3000) + distance * (frequency * 1.5)) * 0.01;
    float noiseFactor = texture(Sampler0, texCoord0 * 10.0).r;
    float twistFactor3 = sin(((GameTime + 3) * 1000) + distance * 40.0 + noiseFactor * 10.0) * 0.0075; // 噪声引入的扭曲

    // 综合所有的扭曲因子
    float twistFactor = twistFactor1 + twistFactor2 + twistFactor3;

    // 计算当前坐标的极角
    float angle = atan(diff.y, diff.x);
    // 计算新的扭曲后的极角
    float newAngle = angle + twistFactor;
    // 计算变形后的纹理坐标
    vec2 deformedTexCoord = center + distance * vec2(cos(newAngle), sin(newAngle));

    // 使用变形后的纹理坐标采样纹理
    vec4 color = texture(Sampler0, deformedTexCoord) * vertexColor;

    // 如果alpha为0，则丢弃片段
    if (color.a == 0.0) {
        discard;
    }

    // 给颜色加入动态变化，增强视觉效果
    color.rgb *= 1.0 + 0.5 * sin(GameTime * 3.0); // 颜色波动

    // 输出最终颜色
    fragColor = color * ColorModulator;
}