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

//java 封装 粒子着色器
class ParticleShaderProgram(context: Context):ShaderProgram(context, R.raw.particle_vertex_shader,R.raw.particle_fragment_shader) {
    private val uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
    private val uTimeLocation = GLES20.glGetUniformLocation(program, U_TIME)

    val aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
    val aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
    val aDirectionVectorLocation = GLES20.glGetAttribLocation(program, A_DIRECTION_VECTOR)
    val aParticleStartTimeLocation = GLES20.glGetAttribLocation(program, A_PARTICLESTAR_TTIME)

    fun setUniforms(matrix:FloatArray,elapsedTime:Float){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0)
        GLES20.glUniform1f(uTimeLocation,elapsedTime)
    }
}