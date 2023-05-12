#version 330 core

in vec4 i_color;
in vec2 i_texCoords;

uniform sampler2D u_texture;
uniform vec2 u_texSize;

out vec4 o_fragColour;

vec4 vignette(vec4 color) {
    float normalizedDistance = length((gl_FragCoord.xy / u_texSize) - vec2(0.5)) * 2.0;
    float darkness = smoothstep(0.5, 1.7, normalizedDistance);
    color.rgb *= 1.0 - darkness;
    return color;
}

void main() {
    o_fragColour = i_color * vignette(texture(u_texture, i_texCoords));
}
