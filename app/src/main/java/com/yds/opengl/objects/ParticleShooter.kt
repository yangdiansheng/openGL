package com.yds.opengl.objects

import android.icu.text.Transliterator
import com.yds.opengl.util.Point
import com.yds.opengl.util.Vector

class ParticleShooter(val position:Point,val direction:Vector,val color:Int) {

    fun addParticles(particleSystem: ParticleSystem,currentTime:Float,count:Int){
        for (i in 0..count) {
            particleSystem.addParticles(position,color,direction,currentTime)
        }
    }


}