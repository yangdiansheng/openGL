/**
顶点着色器
**/

/**
我们定义的每一个单一顶点，顶点着色器都会被调用一次，当它被调用的时候会在a_Position属性里接收到当前顶点的位置，这个属性被定义成vec4类型
vec4类型包含4个分量，在位置的上下文中，x、y、z、对应一个三维位置，w特殊坐标，默认前三个都是0、最后一个是1
**/
uniform mat4 u_Matrix; //uniform 代表一个4 x 4的矩阵
uniform float u_Time;

attribute vec3 a_Position;
attribute vec3 a_Color;
attribute vec3 a_DirectionVector;//方向向量
attribute float a_ParticleStartTime;

//给片段着色器使用
varying vec3 v_Color; //颜色
varying float v_ElapsedTime; //运行时间



/**
创建顶点着色器，着色器入口点，前面定义过的位置复制到指定的输出变量gl_Position中，OpenGl会把gl_Position中存储的值作为当前顶点的最终位置，并把
这些顶点组装成点、直线、和三角形
**/
void main(){
    v_Color = a_Color; //把颜色发给片段着色器
    v_ElapsedTime = u_Time - a_ParticleStartTime; //计算粒子从被创建后运行了多少时间，然后传给片段着色器
    vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime); //计算粒子当前位置 运行时间越长粒子走的越远
    float gravityFactor = v_ElapsedTime * v_ElapsedTime / 8.0; //加速重力因子
    currentPosition.y -= gravityFactor;
    gl_Position = u_Matrix * vec4(currentPosition, 1.0);
    gl_PointSize = 20.0; //把点大小设置成10个像素
}