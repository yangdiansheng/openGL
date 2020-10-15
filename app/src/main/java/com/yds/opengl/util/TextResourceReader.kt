package com.yds.opengl.util

import android.content.Context
import android.content.res.Resources
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.lang.StringBuilder

class TextResourceReader {
    companion object{
        //从资源中读取文本
        fun readTextFileFromResource(context: Context,resourceId:Int):String{
            val body = StringBuilder()
            try {
                val inputStream = context.resources.openRawResource(resourceId)
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var nextLine = bufferedReader.readLine()
                while(nextLine != null){
                    body.append(nextLine)
                    body.append('\n')
                    nextLine = bufferedReader.readLine()
                }
            }catch (o : IOException){
                throw RuntimeException("could not open resource : $resourceId",o)
            }catch (o : Resources.NotFoundException){
                throw RuntimeException("resource not found : $resourceId",o)
            }
            return body.toString()
        }
    }
}