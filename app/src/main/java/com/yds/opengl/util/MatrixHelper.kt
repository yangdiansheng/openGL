package com.yds.opengl.util

import kotlin.math.tan

/**
 * 投影矩阵
 */
object MatrixHelper {
    fun perspectiveM(m:FloatArray, yFovInDegrees:Float,aspect:Float,n:Float,f:Float){
        //计算焦距
        val angleInRadians = (yFovInDegrees * Math.PI / 180.0f).toFloat()
        val a = 1.0f / tan(angleInRadians / 2.0f)
        //写出矩阵值
        m[0] =  a / aspect
        m[1] =  0f
        m[2] =  0f
        m[3] =  0f

        m[4] =  0f
        m[5] =  a
        m[6] =  0f
        m[7] =  0f

        m[8] =  0f
        m[9] =  0f
        m[10] = -((f + n) / (f - n))
        m[11] = -1f

        m[12] =  0f
        m[13] =  0f
        m[14] =  -((2f * f * n) / (f - n))
        m[15] =  0f

    }
}