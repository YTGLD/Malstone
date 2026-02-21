#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform float GameTime;

uniform vec4 ColorModulator;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec2 center = vec2(0.5, 0.5); // 纹理的中心点
    vec2 diff = texCoord0 - center; // 当前坐标与中心点的差值
    float distance = length(diff); // 当前坐标到中心点的距离

    // 计算扭曲因子
    float twistFactor = sin(((GameTime + 1) * 5000) + distance * 30.0 ) * 0.135; // 扭曲因子
    // 计算新的纹理坐标
    float angle = atan(diff.y, diff.x); // 当前坐标的极角
    float newAngle = angle + twistFactor; // 扭曲后的极角
    vec2 deformedTexCoord = center + distance * vec2(cos(newAngle), sin(newAngle));

    // 使用变形后的纹理坐标采样纹理
    vec4 color = texture(Sampler0, deformedTexCoord) * vertexColor;

    // 如果alpha为0，则丢弃片段
    if (color.a == 0.0) {
        discard;
    }

    // 输出最终颜色
    fragColor = color * ColorModulator;
}