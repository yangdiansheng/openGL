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
class TextureShaderProgram(context: Context):ShaderProgram(context, R.raw.texture_vertex_shader,R.raw.texture_fragment_shader) {
    private val uMatrixLocation = GLES20.glGetAttribLocation(program, U_MATRIX)
    private val uTextureUnitLocation = GLES20.glGetAttribLocation(program, U_TEXTURE_UNIT)

    val aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
    val aTextCoordinatesLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_CORRDINATES)

    fun setUniforms(matrix:FloatArray,textureId:Int){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId)//绑定纹理
        GLES20.glUniform1i(uTextureUnitLocation,0)//把选定的纹理单元传递给片段着色器
    }

}