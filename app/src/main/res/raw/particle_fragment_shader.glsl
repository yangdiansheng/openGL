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
varying vec3 v_Color;
varying float v_ElapsedTime;


/**
着色器入口 gl_FragColor OpenGL会使用这个颜色作为当前片段的最终颜色
**/
void main(){
    float xDistance = 0.5 - gl_PointCoord.x;
    float yDistance = 0.5 - gl_PointCoord.y;
    float distanceFromCenter = sqrt(xDistance * xDistance + yDistance * yDistance);
    if(distanceFromCenter > 0.5){
        discard;
    }else {
        gl_FragColor = vec4(v_Color / v_ElapsedTime, 1.0); //颜色除以运行时间，这个着色器会使年轻粒子明亮，年老粒子暗淡
    }

}