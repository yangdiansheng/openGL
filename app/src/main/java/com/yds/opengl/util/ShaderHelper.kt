package com.yds.opengl.util

import android.opengl.GLES20
import android.telephony.gsm.GsmCellLocation
import android.util.Log
import com.yds.opengl.activity.AirHockeyRender
import javax.microedition.khronos.opengles.GL

object ShaderHelper {
    const val TAG = "ShaderHelper"

    //处理顶点着色器
    fun compileVertexShader(shaderCode: String):Int {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode)
    }

    //处理片段着色器
    fun compileFragmentShader(shaderCode: String):Int  {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)
    }

    private fun compileShader(type: Int, shaderCode: String):Int  {
        //创建一个新着色器对象  shaderObjectId OpenGL的引用
        val shaderObjectId = GLES20.glCreateShader(type)
        if(shaderObjectId == 0) {
            if(LogConfig.ON){
                Log.w(TAG,"could not create new shader")
            }
            return 0;
        }
        //将着色器代码上传到着色器对象里 告诉OpenGL读入字符串shaderCode定义的源码，并把它与shaderObjectId关联起来
        GLES20.glShaderSource(shaderObjectId,shaderCode)
        //编译着色器 告诉OpenGL编译上传到shaderObjectId的源代码
        GLES20.glCompileShader(shaderObjectId)
        //检测OpenGL是否能成功地编译着色器 读取编译状态
        val compileStatus:IntArray = intArrayOf(1)
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus,0)
        if(LogConfig.ON){
            Log.v(TAG,"results of compiling source : \n $shaderCode \n ${GLES20.glGetShaderInfoLog(shaderObjectId)}")
        }
        if(compileStatus[0] == 0){
            GLES20.glDeleteShader(shaderObjectId)
            if(LogConfig.ON){
                Log.w(TAG,"compilation of shader failed")
            }
            return 0
        }
        return shaderObjectId
    }

    //连接着色器
    fun compileShader(vertexShaderId:Int, fragmentShaderId:Int):Int{
        //新建程序对象并附上着色器
        val programObjectId = GLES20.glCreateProgram()
        if(programObjectId == 0) {
            if(LogConfig.ON){
                Log.w(TAG,"could not create new program")
            }
            return 0
        }
        //附上着色器
        GLES20.glAttachShader(programObjectId,vertexShaderId)
        GLES20.glAttachShader(programObjectId,fragmentShaderId)
        //连接程序
        GLES20.glLinkProgram(programObjectId)
        //检测是否连接成功
        val linkStatus = intArrayOf(1)
        GLES20.glGetProgramiv(programObjectId,GLES20.GL_LINK_STATUS,linkStatus, 0)
        if(LogConfig.ON){
            Log.v(TAG,"results of linked program :  \n ${GLES20.glGetProgramInfoLog(programObjectId)}")
        }
        //验证状态返回程序对象id
        if(linkStatus[0] == 0){
            GLES20.glDeleteProgram(programObjectId)
            if(LogConfig.ON){
                Log.w(TAG,"linked of program failed")
            }
            return 0
        }
        return programObjectId
    }

    //检测环境
    fun validateProgram(programObjectId:Int) :Boolean {
        GLES20.glValidateProgram(programObjectId)
        val validateStatus = intArrayOf(1)
        GLES20.glGetProgramiv(programObjectId,GLES20.GL_VALIDATE_STATUS,validateStatus,0)
        Log.v(TAG,"results of validate program : ${validateStatus[0]}  \n ${GLES20.glGetProgramInfoLog(programObjectId)}")
        return validateStatus[0] != 0
    }
}