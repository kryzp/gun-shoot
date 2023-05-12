#version 330 core

in vec4 a_position;
in vec4 a_color;
in vec2 a_texCoord0;

out vec4 i_color;
out vec2 i_texCoords;

uniform mat4 u_projTrans;

void main() {
    i_color = a_color;
    i_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}
