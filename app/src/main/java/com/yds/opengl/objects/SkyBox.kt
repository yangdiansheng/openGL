package com.yds.opengl.objects

import android.opengl.GLES20
import com.yds.opengl.Constants.Companion.BYTES_PRE_FLOAT
import com.yds.opengl.data.VertexArray
import com.yds.opengl.programs.SkyboxShaderProgram
import com.yds.opengl.programs.TextureShaderProgram
import com.yds.opengl.util.TextureHelper
import java.nio.ByteBuffer

class SkyBox {
    companion object{
        const val POSITION_COMPONENT_COUNT = 3
    }

    //包含桌面的顶点数据和纹理坐标S T
    private val vertextArray = VertexArray(floatArrayOf(
        -1f,  1f,  1f,     // (0) Top-left near
        1f,  1f,  1f,     // (1) Top-right near
        -1f, -1f,  1f,     // (2) Bottom-left near
        1f, -1f,  1f,     // (3) Bottom-right near
        -1f,  1f, -1f,     // (4) Top-left far
        1f,  1f, -1f,     // (5) Top-right far
        -1f, -1f, -1f,     // (6) Bottom-left far
        1f, -1f, -1f      // (7) Bottom-right far
    ))

    private val indexArray =  ByteBuffer.allocateDirect(6 * 6)

    init {
        indexArray.put(
            byteArrayOf(
                // Front
                1, 3, 0,
                0, 3, 2,

                // Back
                4, 6, 5,
                5, 6, 7,

                // Left
                0, 2, 4,
                4, 2, 6,

                // Right
                5, 7, 1,
                1, 7, 3,

                // Top
                5, 1, 4,
                4, 1, 0,

                // Bottom
                6, 2, 7,
                7, 2, 3
            )
        )
        indexArray.position(0)
    }

    //把顶点数组绑定到一个着色器上
    fun bindData(skyboxProgram: SkyboxShaderProgram){
        vertextArray.setVertexAttribPointer(
            0,
            skyboxProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            0
        )
    }

    fun draw(){
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,36,GLES20.GL_UNSIGNED_BYTE, indexArray)
    }
}