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
class SkyboxShaderProgram(context: Context):ShaderProgram(context, R.raw.skybox_vertex_shader,R.raw.skybox_fragment_shader) {
    private val uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
    private val uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT)

    val aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)

    fun setUniforms(matrix:FloatArray,textureId:Int){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0)

        //设置纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureId)
        GLES20.glUniform1i(uTextureUnitLocation,0)
    }
}