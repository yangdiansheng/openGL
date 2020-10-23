package com.yds.opengl.programs

import android.content.Context
import android.opengl.GLES20
import com.yds.opengl.Constants.Companion.BYTES_PRE_FLOAT
import com.yds.opengl.data.VertexArray
import com.yds.opengl.util.ShaderHelper
import com.yds.opengl.util.TextResourceReader
import com.yds.opengl.util.TextureHelper
import javax.microedition.khronos.opengles.GL
import kotlin.properties.Delegates

//木槌数据
open class ShaderProgram(val context: Context,vertexShaderResourceId:Int,fragmentShaderResourceId:Int) {
    companion object{
        //uniform constants
        const val U_MATRIX = "u_Matrix"
        const val U_TEXTURE_UNIT = "u_TextureUnit"
        const val U_TIME = "u_Time"
        //attribute constants
        const val A_POSITION = "a_Position"
        const val A_COLOR = "a_Color"
        const val A_TEXTURE_CORRDINATES = "a_TextureCoordinates"
        const val A_DIRECTION_VECTOR = "a_DirectionVector"
        const val A_PARTICLESTAR_TTIME = "a_ParticleStartTime"

    }

    var program = ShaderHelper.buildProgram(
        TextResourceReader.readTextFileFromResource(context,vertexShaderResourceId),
        TextResourceReader.readTextFileFromResource(context,fragmentShaderResourceId)
    )

    fun useProgram(){
        GLES20.glUseProgram(program)
    }

}