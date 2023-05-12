#version 330 core

in vec4 i_color;
in vec2 i_texCoords;

out vec4 o_fragColour;

uniform sampler2D u_texture;
uniform vec2 u_texSize;
uniform float u_blurAmount;

const int KERNEL_SIZE = 15;
const float KERNEL_RADIUS = float(KERNEL_SIZE - 1) / 2.0;

const float WEIGHTS[KERNEL_SIZE] = float[](
	0.004429, 0.008957, 0.021596, 0.044368, 0.077847,
	0.115228, 0.147261, 0.159576, 0.147261, 0.115228,
	0.077847, 0.044368, 0.021596, 0.008957, 0.004429
);

void main() {
    vec4 texColor = texture(u_texture, i_texCoords);

    vec2 uv = i_texCoords;
    vec4 cc = vec4(0.0);

    for (int i = 0; i < KERNEL_SIZE; i++) {
    	float offset = float(i) - KERNEL_RADIUS;
    	vec2 blurUV = uv + vec2(offset / u_texSize.x * u_blurAmount, 0.0);
    	cc += texture(u_texture, blurUV) * WEIGHTS[i];
    }

    o_fragColour = i_color * cc;
}
