package com.yds.opengl.data

import android.opengl.GLES20
import com.yds.opengl.Constants.Companion.BYTES_PRE_FLOAT
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * 封装存储顶点的矩阵
 */
class VertexArray(private val vertexData:FloatArray) {

    private var floatBuffer:FloatBuffer = ByteBuffer
        .allocateDirect(vertexData.size * BYTES_PRE_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(vertexData)

    fun setVertexAttribPointer(dataOffset:Int,attributeLocation:Int,componentCount:Int,stride:Int){
        floatBuffer.position(dataOffset)
        GLES20.glVertexAttribPointer(attributeLocation,componentCount,GLES20.GL_FLOAT,false,stride,floatBuffer)
        GLES20.glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)
    }
}