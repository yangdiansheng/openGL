package com.yds.opengl.programs

import android.content.Context
import android.opengl.GLES20
import com.yds.opengl.Constants.Companion.BYTES_PRE_FLOAT
import com.yds.opengl.R
import com.yds.opengl.data.VertexArray
import com.yds.opengl.util.ShaderHelper
import com.yds.opengl.util.TextResourceReader
import com.yds.opengl.util.TextureHelper
import javax.microedition.khronos.opengles.GL
import kotlin.properties.Delegates

//木槌数据
class ColorShaderProgram(context: Context):ShaderProgram(context, R.raw.texture_vertex_shader,R.raw.texture_fragment_shader) {
    private val uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)

    val aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
    val aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)

    fun setUniforms(matrix:FloatArray){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0)
    }

}