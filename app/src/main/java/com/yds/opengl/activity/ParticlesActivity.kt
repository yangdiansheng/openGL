
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
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.yds.opengl.App
import com.yds.opengl.AppContext
import com.yds.opengl.R
import com.yds.opengl.objects.ParticleShooter
import com.yds.opengl.objects.ParticleSystem
import com.yds.opengl.programs.ParticleShaderProgram
import com.yds.opengl.util.*
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL
import javax.microedition.khronos.opengles.GL10
import kotlin.properties.Delegates

class ParticlesActivity : AppCompatActivity() {


    companion object{

        fun start(activity: AppCompatActivity){
            val intent = Intent()
            intent.setClass(activity, ParticlesActivity::class.java)
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
            glSurfaceView?.setRenderer(ParticlesRender())
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
class  ParticlesRender : GLSurfaceView.Renderer{

    val projectionMatrix = FloatArray(16)
    val viewMatrix = FloatArray(16)
    val viewProjectionMatrix = FloatArray(16)

    lateinit var particleProgram:ParticleShaderProgram
    lateinit var particleSystem: ParticleSystem
    lateinit var redParticleShooter: ParticleShooter
    lateinit var greenParticleShooter: ParticleShooter
    lateinit var blueParticleShooter: ParticleShooter
    var globalStartTime = 0L


    //Surface被创建的时候回调，发生在应用第一次运行的时候，并且在设备被唤醒或者用于从其他Activity切换回来时，这个
    //方法也可能会被调用，本方法可能会被调用多次
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置清空屏幕用的颜色
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f)

        particleProgram = ParticleShaderProgram(AppContext)
        particleSystem = ParticleSystem(10000)
        globalStartTime = System.nanoTime()

        val particleDirection = Vector(0f,0.5f,0f)

        redParticleShooter = ParticleShooter(Point(-1f,0f,0f),particleDirection,Color.rgb(255,50,5))
        greenParticleShooter = ParticleShooter(Point(0f,0f,0f),particleDirection,Color.rgb(25,255,25))
        blueParticleShooter = ParticleShooter(Point(1f,0f,0f),particleDirection,Color.rgb(5,50,255))
    }


    //在Surface被创建后，每次Surface尺寸变化时调用（横竖屏切换）
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置视图尺寸
        GLES20.glViewport(0,0,width,height)

        MatrixHelper.perspectiveM(projectionMatrix,45f,width.toFloat() / height.toFloat(), 1f,10f)
        Matrix.setIdentityM(viewMatrix,0)
        Matrix.translateM(viewMatrix,0,0f,-1.5f,-5f)
        Matrix.multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0)
    }

    //当绘制第一针时，调用一定要绘制一些东西，即使只是清空屏幕，因为在这个方法返回后，渲染区会被较交换在屏幕上，如果什么
    //都没花，会看到闪烁效果
    /**
     * 参数gl 是OpenGL 1.0遗留 2.0不使用
     */
    override fun onDrawFrame(gl: GL10?) {
        //清空屏幕。会擦除屏幕上的所有颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f

        redParticleShooter.addParticles(particleSystem,currentTime,5)
        greenParticleShooter.addParticles(particleSystem,currentTime,5)
        blueParticleShooter.addParticles(particleSystem,currentTime,5)

        particleProgram.useProgram()
        particleProgram.setUniforms(viewProjectionMatrix,currentTime)
        particleSystem.bindData(particleProgram)
        particleSystem.draw()

    }





}