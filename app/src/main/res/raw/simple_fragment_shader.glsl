/**
片段着色器
**/

/**
精度限定符
定义了所有浮点数类型默认精度lowp mediump highp
**/

/**
生成片段颜色
让每个顶点都使用同一个值 四个分量  红 绿 蓝 阿尔法
**/
uniform vec4 u_Color;

/**
着色器入口 gl_FragColor OpenGL会使用这个颜色作为当前片段的最终颜色
**/
void main(){
    gl_FragColor = u_Color;
}