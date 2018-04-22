#version 300 es
in vec4 vPosition;
uniform mat4 vMatrix;
out  vec4 vColor;
in vec4 aColor;
void main() {
     gl_Position = vMatrix*vPosition;
     vColor=aColor;
}