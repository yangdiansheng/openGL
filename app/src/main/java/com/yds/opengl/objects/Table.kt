package com.yds.opengl.objects

import android.opengl.GLES20
import com.yds.opengl.Constants.Companion.BYTES_PRE_FLOAT
import com.yds.opengl.data.VertexArray
import com.yds.opengl.programs.TextureShaderProgram
import com.yds.opengl.util.TextureHelper

class Table {
    companion object{
        const val POSITION_COMPONENT_COUNT = 2
        const val TEXTTURE_COORPDINATES_COMPONENT_COUNT = 2
        const val STRIDE = (POSITION_COMPONENT_COUNT + TEXTTURE_COORPDINATES_COMPONENT_COUNT) * BYTES_PRE_FLOAT
    }

    //包含桌面的顶点数据和纹理坐标S T
    private val VERTEX_DATA:FloatArray = floatArrayOf(
        //triangle fan X,Y,S,T
        0f,0f,0.5f,0.5f
        -0.5f,-0.8f,0f,0.9f,
        0.5f,-0.8f,1f,0.9f,
        0.5f,0.8f,1f,0.1f,
        -0.5f,0.8f,0f,0.1f,
        -0.5f,-0.8f,0f,0.9f
    )

    private var vertextArray:VertexArray

    init {
        vertextArray = VertexArray(VERTEX_DATA)
    }

    //把顶点数组绑定到一个着色器上
    fun bindData(textureProgram: TextureShaderProgram){
        vertextArray.setVertexAttribPointer(
            0,
            textureProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        vertextArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            textureProgram.aTextCoordinatesLocation,
            TEXTTURE_COORPDINATES_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw(){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6)
    }
}