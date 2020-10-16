
package com.yds.opengl.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.yds.opengl.AppContext
import com.yds.opengl.R
import com.yds.opengl.util.LogConfig
import com.yds.opengl.util.ShaderHelper
import com.yds.opengl.util.TextResourceReader
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL
import javax.microedition.khronos.opengles.GL10

class AirHockeyActivity2 : AppCompatActivity() {


    companion object{

        fun start(activity: AppCompatActivity){
            val intent = Intent()
            intent.setClass(activity, AirHockeyActivity2::class.java)
            activity.startActivity(intent)
        }
    }

    private var glSurfaceView:GLSurfaceView? = null
    private var rendereSet = false //是否处于有效状态

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)

        //检测是否支持openGl es 2.0
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportEs2 = configurationInfo.reqGlEsVersion >= 0x20000

        //渲染表面
        if (supportEs2){
            glSurfaceView?.setEGLContextClientVersion(2)
            glSurfaceView?.setRenderer(AirHockeyRender2())
            rendereSet = true
        }else {
            Toast.makeText(this,"this device does not support OpenGL 2.0", Toast.LENGTH_SHORT).show()
            return
        }

        setContentView(glSurfaceView)
    }

    //保证OpenGL正确暂停和开始
    override fun onPause() {
        super.onPause()
        if(rendereSet) glSurfaceView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        if(rendereSet) glSurfaceView?.onResume()
    }
}

//渲染器
class  AirHockeyRender2 : GLSurfaceView.Renderer{

    //顶点数据 矩形
    private val tableVertices:Array<Float> = arrayOf(
        0f,0f,
        0f,14f,
        9f,14f,
        9f,0f
    )

    //顶点数据 两个三角形绘制矩形
    private val tableVerticesWithTriangles:FloatArray = floatArrayOf(
        //triangle fan X,Y,R,G,B
        0f,0f,1f,1f,1f,
        -0.5f,-0.5f,0.7f,0.7f,0.7f,
        0.5f,-0.5f,0.7f,0.7f,0.7f,
        0.5f,0.5f,0.7f,0.7f,0.7f,
        -0.5f,0.5f,0.7f,0.7f,0.7f,
        -0.5f,-0.5f,0.7f,0.7f,0.7f,
        //line 1
        -0.5f, 0f,1f,0f,0f,
        0.5f, 0f,0f,0f,1f,
        //mallets
        0f, -0.25f,0f,0f,1f,
        0f, 0.25f,1f,0f,0f
    )

    companion object{
        const val BYTES_PRE_FLOAT = 4
        const val TAG = "AirHockeyActivity"
        const val A_COLOR = "a_Color"
        const val A_POSITION = "a_Position"
        //顶点有多少个分量
        const val POSITION_COMPONENT_COUNT = 2
        const val COLOR_COMPONENT_COUNT = 3
        //跨距告诉OpenGL每个位置之间有多少个字节
        const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PRE_FLOAT

    }

    //存储程序对象中位置的变量
    private var aColorLocation:Int? = null
    //存储属性位置
    private var aPositionLocation:Int? = null

    //dvk像本地环境传递数据使用 FloatBuffer用来在本地内存中存储数据
    private val vertexData:FloatBuffer = ByteBuffer
        //分配一块本地内存、这块内存不会被垃圾回收器管理
        .allocateDirect(tableVerticesWithTriangles.size * BYTES_PRE_FLOAT)
        //告诉字节缓存区按照本地字节序组织内容
        .order(java.nio.ByteOrder.nativeOrder())
        //得到底层反应字节的FloatBuffer实例
        .asFloatBuffer()

    init {
        //把数据从虚拟机中复制到本地内存，当进程结束时这块内存会被释放掉
        vertexData.put(tableVerticesWithTriangles)
    }

    //Surface被创建的时候回调，发生在应用第一次运行的时候，并且在设备被唤醒或者用于从其他Activity切换回来时，这个
    //方法也可能会被调用，本方法可能会被调用多次
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置清空屏幕用的颜色
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f)
        val vertexShaderSource =
            TextResourceReader.readTextFileFromResource(
                AppContext,
                R.raw.simple_vertex_shader_2
            )
        val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource)
        val fragmentShaderSource =
            TextResourceReader.readTextFileFromResource(
                AppContext,
                R.raw.simple_fragment_shader_2
            )
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)
        //把着色器连接在一起
        val program = ShaderHelper.compileShader(vertexShader,fragmentShader)
        //验证OpenGL程序对象
        if(LogConfig.ON){
            ShaderHelper.validateProgram(program)
        }
        //告诉OpenGL在绘制任何东西到屏幕上的时候要使用这里定义的程序
        GLES20.glUseProgram(program)

        //存储程序对象中位置的变量uniform
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        //属性位置 告诉OpenGL到哪里能找到这个属性对应的数据
        val aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)

        //可以在缓冲区vertexData中找到a_Position对应的数据
        vertexData.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,false,
            STRIDE,vertexData)
        //使能顶点数组
        GLES20.glEnableVertexAttribArray(aPositionLocation)
        //将顶点数据和a_Color关联
        aColorLocation?.let {
            vertexData.position(POSITION_COMPONENT_COUNT)
            GLES20.glVertexAttribPointer(it, COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,false,
                STRIDE,vertexData)
            //使能顶点数组
            GLES20.glEnableVertexAttribArray(it)
        }
    }

    
    //在Surface被创建后，每次Surface尺寸变化时调用（横竖屏切换）
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置视图尺寸
        GLES20.glViewport(0,0,width,height)
    }

    //当绘制第一针时，调用一定要绘制一些东西，即使只是清空屏幕，因为在这个方法返回后，渲染区会被较交换在屏幕上，如果什么
    //都没花，会看到闪烁效果
    /**
     * 参数gl 是OpenGL 1.0遗留 2.0不使用
     */
    override fun onDrawFrame(gl: GL10?) {
        //清空屏幕。会擦除屏幕上的所有颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        aColorLocation?.let {
            //更新着色器代码中u_Color的值
            //画两个三角形
            GLES20.glUniform4f(it,1.0f,1.0f,1.0f,1.0f)
            //绘制三角扇形
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6)
            //画线
            GLES20.glUniform4f(it,1.0f,0.0f,0.0f,1.0f)
            GLES20.glDrawArrays(GLES20.GL_LINES,6,2)
            //画点
            GLES20.glUniform4f(it,0.0f,0.0f,1.0f,1.0f)
            GLES20.glDrawArrays(GLES20.GL_POINTS,8,1)
            //画点
            GLES20.glUniform4f(it,0.0f,0.0f,1.0f,1.0f)
            GLES20.glDrawArrays(GLES20.GL_POINTS,9,1)

        }

    }





}
