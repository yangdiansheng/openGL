
package com.yds.opengl.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class FirstOpenGlActivity : AppCompatActivity() {

    companion object{
        fun start(activity: AppCompatActivity){
            val intent = Intent()
            intent.setClass(activity,
                FirstOpenGlActivity::class.java)
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
            glSurfaceView?.setRenderer(FirstOpenGLProjectRender())
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
class  FirstOpenGLProjectRender : GLSurfaceView.Renderer{

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
