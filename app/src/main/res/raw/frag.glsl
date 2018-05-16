#version 300 es
precision mediump float;							//浮点精度为mediump
in vec2 vTCoord; 						//接收从纹理坐标
in float currY;								//接收Y坐标
uniform sampler2D sTGrass;					//纹理内容数据（草皮）
uniform sampler2D sTRock;					//纹理内容数据（岩石）
uniform float landStartY;							//过程纹理起始Y坐标
uniform float landYSpan;							//过程纹理跨度

out vec4 fragColor;
void main(){
   vec4 gColor=texture(sTGrass, vTCoord); 	//从草皮纹理中采样出颜色
   vec4 rColor=texture(sTRock, vTCoord); 	//从岩石纹理中采样出颜色
   vec4 finalColor;									//最终颜色
   if(currY<landStartY){
	  finalColor=gColor;	//当片元Y坐标小于过程纹理起始Y坐标时采用草皮纹理
   }else if(currY>landStartY+landYSpan){
	  finalColor=rColor;	//当片元Y坐标大于过程纹理起始Y坐标加跨度时采用岩石纹理
   }else{
       float currYRatio=(currY-landStartY)/landYSpan;	//计算岩石纹理所占的百分比
       finalColor= currYRatio*rColor+(1.0- currYRatio)*gColor;//将岩石、草皮纹理颜色按比例混合
   }
	   fragColor = finalColor; //给此片元最终颜色值
}  
