package com.yds.opengl.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import javax.microedition.khronos.opengles.GL
import kotlin.math.tan

/**
 * 投影矩阵
 */
object TextureHelper {
    const val TAG = "TextureHelper"

    //获取纹理Id
    fun loadtexture(context :Context, resourceId:Int):Int{
        //创建纹理对象
        val textureObjectIds = IntArray(1)
        GLES20.glGenTextures(1,textureObjectIds,0)
        if(textureObjectIds[0] == 0) {
            if(LogConfig.ON){
                Log.w(TAG,"could not generate a new opengl texture object")
            }
            return 0
        }
        //解压图片元数据
        val options = BitmapFactory.Options()
        //使用原始图像数据
        options.inScaled = false
        val bitmap = BitmapFactory.decodeResource(context.resources,resourceId,options)
        if(bitmap == null){
            if(LogConfig.ON){
                Log.w(TAG,"resource ID $resourceId could not be decode.")
            }
            GLES20.glDeleteTextures(1,textureObjectIds,0)
            return 0
        }
        //应用纹理对象 2d纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureObjectIds[0])
        //设置纹理过滤参数
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR_MIPMAP_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
        //加载位图数据到OpenGL中 openGl读入bitmap位图数据，并把它复制到当前绑定的纹理对象
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0)
        bitmap.recycle()
        //生成必要的级别
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
        //接触绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
        return textureObjectIds[0]
    }

    fun loadCubeMap(context: Context,cubResources:IntArray):Int{
        val textureObjectIds = IntArray(1)
        GLES20.glGenTextures(1,textureObjectIds,0)
        if(textureObjectIds[0] == 0){
            if(LogConfig.ON){
                Log.w(TAG, "could not generate a new opengl texture object")
            }
            return 0
        }
        val options = BitmapFactory.Options()
        options.inScaled = false
        val cubeBitmap = arrayOfNulls<Bitmap>(6)
        (0..5).forEach { index ->
            Log.w(TAG, "forEach ${index}")
            cubeBitmap[index] = BitmapFactory.decodeResource(context.resources, cubResources[index],options)
            if(cubeBitmap[index] == null){
                if(LogConfig.ON){
                    Log.w(TAG,"resource id ${cubResources[index]} could not be decoded")
                }
                GLES20.glDeleteTextures(1,textureObjectIds,0)
                return 0
            }
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textureObjectIds[0])
        //设置纹理过滤参数
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
        //立体贴图关联起来
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmap[0], 0)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmap[1], 0)

        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmap[2], 0)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmap[3], 0)

        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmap[4], 0)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmap[5], 0)

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
        cubeBitmap.forEach {
            it?.recycle()
        }
        return textureObjectIds[0]
    }
}