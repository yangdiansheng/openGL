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
precision mediump float;
uniform samplerCube u_TextureUnit; //接收实际的纹理数据
varying vec3 v_Position; //纹理坐标

/**
着色器入口 gl_FragColor OpenGL会使用这个颜色作为当前片段的最终颜色
**/
void main(){
    //文件坐标和文件数据被传递给着色器函数
    gl_FragColor = textureCube(u_TextureUnit, v_Position);
}