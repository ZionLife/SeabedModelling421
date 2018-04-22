#version 300 es
precision mediump float;
in vec4 vColor;
out vec4 fragColor;//输出到的片元颜色
void main() {
   fragColor = vColor;
}