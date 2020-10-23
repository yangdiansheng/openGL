package com.yds.opengl.objects

import android.icu.text.Transliterator
import android.opengl.Matrix
import androidx.core.graphics.rotationMatrix
import com.yds.opengl.util.Point
import com.yds.opengl.util.Vector
import kotlin.random.Random

class ParticleShooter(val position:Point,val direction:Vector,val color:Int,val angleVariance:Float,val speedVariance:Float) {

    val random = java.util.Random()

    val rotationMatrix = FloatArray(16)
    val directionVector = FloatArray(16)
    val resultVector = FloatArray(16)

    init {
        directionVector[0] = direction.x
        directionVector[1] = direction.y
        directionVector[2] = direction.z
    }

    fun addParticles(particleSystem: ParticleSystem,currentTime:Float,count:Int){
        for (i in 0..count) {
            //创建旋转矩阵
            Matrix.setRotateEulerM(rotationMatrix,0,
                (random.nextFloat() - 0.5f) * angleVariance,
                (random.nextFloat() - 0.5f) * angleVariance,
                (random.nextFloat() - 0.5f) * angleVariance
            )

            Matrix.multiplyMV(resultVector,0,
            rotationMatrix,0,
            directionVector,0)

            val speedAdjustment =  1f + random.nextFloat() * speedVariance

            val thisDirection = Vector(
                resultVector[0] * speedAdjustment,
                resultVector[1] * speedAdjustment,
                resultVector[2] * speedAdjustment
            )

            particleSystem.addParticles(position,color,thisDirection,currentTime)
        }
    }


}