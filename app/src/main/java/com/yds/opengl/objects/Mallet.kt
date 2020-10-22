package com.yds.opengl.objects

import android.opengl.GLES20
import com.yds.opengl.Constants.Companion.BYTES_PRE_FLOAT
import com.yds.opengl.data.VertexArray
import com.yds.opengl.programs.ColorShaderProgram
import com.yds.opengl.util.TextureHelper

//木槌数据
class Mallet {
    companion object{
        const val POSITION_COMPONENT_COUNT = 2
        const val COLOR_COMPONENT_COUNT = 3
        const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PRE_FLOAT
    }

    //包含桌面的顶点数据和纹理坐标S T
    private val VERTEX_DATA:FloatArray = floatArrayOf(
        //triangle fan X,Y,R,G,B
        0f,-0.4f,0f,0f,1f,
        0f,0.4f,1f,0f,0f
    )

    private var vertextArray:VertexArray

    init {
        vertextArray = VertexArray(VERTEX_DATA)
    }

    //把顶点数组绑定到一个着色器上
    fun bindData(colorProgram: ColorShaderProgram){
        vertextArray.setVertexAttribPointer(
            0,
            colorProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        vertextArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            colorProgram.aColorLocation,
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw(){
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,2)
    }
}