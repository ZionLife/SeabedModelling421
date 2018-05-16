#version 300 es
uniform mat4 MVPMat; 		//总变换矩阵
in vec3 aPos;  		//顶点位置
in vec2 aTCoord;    		//顶点纹理坐标
out vec2 vTCoord;  		//用于传递给片元着色器的纹理坐标
out float currY;				//用于传递给片元着色器的Y坐标
void main(){
   gl_Position = MVPMat * vec4(aPos,1); 	//与矩阵相乘进行变换
   vTCoord = aTCoord;					//将纹理坐标传递给片元着色器
   currY=aPos.y;						//将顶点的Y坐标传递给片元着色器
}