
package com.yds.opengl.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.yds.opengl.App
import com.yds.opengl.AppContext
import com.yds.opengl.R
import com.yds.opengl.objects.Mallet
import com.yds.opengl.objects.Table
import com.yds.opengl.programs.ColorShaderProgram
import com.yds.opengl.programs.TextureShaderProgram
import com.yds.opengl.util.*
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.properties.Delegates

/**
 * 纹理
 *

 *
 */
class AirHockeyTextureActivity : AppCompatActivity() {


    companion object{

        fun start(activity: AppCompatActivity){
            val intent = Intent()
            intent.setClass(activity, AirHockeyTextureActivity::class.java)
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
            glSurfaceView?.setRenderer(AirHockeyTextureRender())
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
class  AirHockeyTextureRender : GLSurfaceView.Renderer{

    //顶点数组存储的矩阵
    private val projectMatrix = FloatArray(16)
    //模型矩阵
    private val modelMatrix = FloatArray(16)

    private lateinit var  table:Table
    private lateinit var mallet:Mallet
    private lateinit var  textureProgram:TextureShaderProgram
    private lateinit var  colorProgram :ColorShaderProgram
    private var texture by Delegates.notNull<Int>()


    //Surface被创建的时候回调，发生在应用第一次运行的时候，并且在设备被唤醒或者用于从其他Activity切换回来时，这个
    //方法也可能会被调用，本方法可能会被调用多次
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置清空屏幕用的颜色
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f)
        table = Table()
        mallet = Mallet()

        textureProgram = TextureShaderProgram(AppContext)
        colorProgram = ColorShaderProgram(AppContext)

        texture = TextureHelper.loadtexture(AppContext,R.drawable.faguang)
    }

    
    //在Surface被创建后，每次Surface尺寸变化时调用（横竖屏切换）
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置视图尺寸
        GLES20.glViewport(0,0,width,height)

        Matrix.perspectiveM(projectMatrix,0,45f,width.toFloat() / height.toFloat(), 1f, 10f)
        MatrixHelper.perspectiveM(projectMatrix,45f,width.toFloat() / height.toFloat(), 1f, 10f)
        //移动桌面
        Matrix.setIdentityM(modelMatrix,0)
//        Matrix.translateM(modelMatrix,0,0f,0f,-2f)
        Matrix.translateM(modelMatrix,0,0f,0f,-3f)
        Matrix.rotateM(modelMatrix, 0,-60f,1f,0f,0f)
        //矩阵相乘
        val temp = FloatArray(16)
        Matrix.multiplyMM(temp,0,projectMatrix,0,modelMatrix,0)
        System.arraycopy(temp,0,projectMatrix,0,temp.size)

    }

    //当绘制第一针时，调用一定要绘制一些东西，即使只是清空屏幕，因为在这个方法返回后，渲染区会被较交换在屏幕上，如果什么
    //都没花，会看到闪烁效果
    /**
     * 参数gl 是OpenGL 1.0遗留 2.0不使用
     */
    override fun onDrawFrame(gl: GL10?) {
        //清空屏幕。会擦除屏幕上的所有颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        textureProgram.useProgram()
        textureProgram.setUniforms(projectMatrix,texture)
        table.bindData(textureProgram)
        table.draw()

        colorProgram.useProgram()
        colorProgram.setUniforms(projectMatrix)
        mallet.bindData(colorProgram)
        mallet.draw()
    }





}
