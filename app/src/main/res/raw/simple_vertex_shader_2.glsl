/**
顶点着色器
**/

/**
我们定义的每一个单一顶点，顶点着色器都会被调用一次，当它被调用的时候会在a_Position属性里接收到当前顶点的位置，这个属性被定义成vec4类型
vec4类型包含4个分量，在位置的上下文中，x、y、z、对应一个三维位置，w特殊坐标，默认前三个都是0、最后一个是1
**/
attribute vec4 a_Position;
attribute vec4 a_Color;

//直线或三角形上的每个片段混合后的颜色可以用varying生成 使用线程差值linear interpolation实现
varying vec4 v_Color; //变量类型 把混合后的值发给片段着色器 一个点想两一个点混合颜色

/**
创建顶点着色器，着色器入口点，前面定义过的位置复制到指定的输出变量gl_Position中，OpenGl会把gl_Position中存储的值作为当前顶点的最终位置，并把
这些顶点组装成点、直线、和三角形
**/
void main(){
    v_Color = a_Color;

    gl_Position = a_Position;
    gl_PointSize = 10.0; //指定点大小
}