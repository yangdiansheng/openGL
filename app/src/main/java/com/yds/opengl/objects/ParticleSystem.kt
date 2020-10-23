package com.yds.opengl.objects

import android.graphics.Color
import android.graphics.Path
import android.graphics.drawable.VectorDrawable
import android.opengl.GLES20
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.yds.opengl.Constants.Companion.BYTES_PRE_FLOAT
import com.yds.opengl.data.VertexArray
import com.yds.opengl.programs.ParticleShaderProgram
import com.yds.opengl.programs.TextureShaderProgram
import com.yds.opengl.util.Point
import com.yds.opengl.util.TextureHelper
import com.yds.opengl.util.Vector

//粒子系统
class ParticleSystem(private val maxParticleCount: Int) {
    companion object {
        const val POSITION_COMPONENT_COUNT = 3
        const val COLOR_COMPONENT_COUNT = 3
        const val VECTOR_COMPONENT_COUNT = 3
        const val PARTICLE_START_TIME_COMPONENT_COUNT = 1
        const val TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT +
                VECTOR_COMPONENT_COUNT + PARTICLE_START_TIME_COMPONENT_COUNT
        const val STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PRE_FLOAT
    }

    private var particles: FloatArray//存储粒子的浮点数组
    private var vertexArray: VertexArray//发送给OpenGL的数据
    private var currentPaicleCount = 0//持续记录数组中的粒子
    private var nextParticle = 0

    init {
        particles = FloatArray(maxParticleCount * TOTAL_COMPONENT_COUNT)
        vertexArray = VertexArray(particles)

    }

    /**
     * 位置
     * 颜色
     * 方向
     * 粒子创建时间
     */
    fun addParticles(position: Point, color: Int, direction: Vector, particleStartTime: Float) {
        val particleOffset = nextParticle * TOTAL_COMPONENT_COUNT
        //新粒子的每个属性位置
        var currentOffSet = particleOffset
        nextParticle++;
        if (currentPaicleCount < maxParticleCount) currentPaicleCount++
        if (nextParticle == maxParticleCount) nextParticle = 0

        particles[currentOffSet++] = position.x
        particles[currentOffSet++] = position.y
        particles[currentOffSet++] = position.z

        particles[currentOffSet++] = Color.red(color) / 225f
        particles[currentOffSet++] = Color.green(color) / 225f
        particles[currentOffSet++] = Color.blue(color) / 225f

        particles[currentOffSet++] = direction.x
        particles[currentOffSet++] = direction.y
        particles[currentOffSet++] = direction.z

        particles[currentOffSet++] = particleStartTime

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT)
    }

    fun bindData(particleProgram: ParticleShaderProgram) {
        var dataOffet = 0
        vertexArray.setVertexAttribPointer(
            dataOffet,
            particleProgram.aPositionLocation, POSITION_COMPONENT_COUNT, STRIDE)
        dataOffet += POSITION_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            dataOffet,
            particleProgram.aColorLocation, COLOR_COMPONENT_COUNT, STRIDE)
        dataOffet += COLOR_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            dataOffet,
            particleProgram.aDirectionVectorLocation, VECTOR_COMPONENT_COUNT, STRIDE)
        dataOffet += VECTOR_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            dataOffet,
            particleProgram.aParticleStartTimeLocation, PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE)
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, currentPaicleCount)
    }
}