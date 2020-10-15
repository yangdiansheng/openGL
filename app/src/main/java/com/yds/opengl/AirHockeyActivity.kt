
package com.yds.opengl

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyActivity : AppCompatActivity() {

    companion object{
        fun start(activity: AppCompatActivity){
            val intent = Intent()
            intent.setClass(activity,AirHockeyActivity::class.java)
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
            glSurfaceView?.setRenderer(AirHockeyRender())
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
class  AirHockeyRender : GLSurfaceView.Renderer{

    //顶点数据 矩形
    private val tableVertices:Array<Float> = arrayOf(
        0f,0f,
        0f,14f,
        9f,14f,
        9f,0f
    )

    //顶点数据 两个三角形绘制矩形
    private val tableVerticesWithTriangles:FloatArray = floatArrayOf(
        //triangle 1
        0f,0f,
        9f,14f,
        0f,14f,
        //triangle 2
        0f,0f,
        9f,0f,
        9f,14f
    )

    companion object{
        const val BYTES_PRE_FLOAT = 4
    }
    //dvk像本地环境传递数据使用 FloatBuffer用来在本地内存中存储数据
    private val vertexData:FloatBuffer = ByteBuffer
        //分配一块本地内存、这块内存不会被垃圾回收器管理
        .allocate(tableVerticesWithTriangles.size * BYTES_PRE_FLOAT)
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
        GLES20.glClearColor(0.0f,1.0f,0.0f,0.0f)
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
    }





}
