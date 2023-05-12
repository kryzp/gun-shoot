#version 330 core

in vec4 i_color;
in vec2 i_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_bloom;
uniform float u_globalBrightness;

out vec4 o_fragColour;

float intensity(vec4 c) {
	return (c.r + c.g + c.b) / 3.0;
}

void main() {
    vec4 texColor = texture(u_texture, i_texCoords);
    vec4 bloom = texture(u_bloom, i_texCoords) * vec4(2.0 + 2.0 * (1.0 - u_globalBrightness));
    float bloomIntensity = intensity(bloom);
    texColor += bloom;
    o_fragColour = i_color * texColor * max(bloomIntensity, u_globalBrightness);
}
