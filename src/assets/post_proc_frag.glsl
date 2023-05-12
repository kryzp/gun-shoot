#version 330 core

in vec4 i_color;
in vec2 i_texCoords;

uniform sampler2D u_texture;

out vec4 o_fragColour;

void main() {
    vec4 texColor = texture(u_texture, i_texCoords);
    o_fragColour = u_color * texColor;
}
