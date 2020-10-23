package com.yds.opengl.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yds.opengl.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_one.setOnClickListener {
            FirstOpenGlActivity.start(this)
        }
        bt_airhockey.setOnClickListener {
            AirHockeyActivity.start(this)
        }
        bt_airhockey_2.setOnClickListener {
            AirHockeyActivity2.start(this)
        }
        bt_airhockey_ortho.setOnClickListener {
            AirHockeyOrthoActivity.start(this)
        }
        bt_airhockey_3d.setOnClickListener {
            AirHockey3DActivity.start(this)
        }
        bt_airhockey_texture.setOnClickListener {
            AirHockeyTextureActivity.start(this)
        }
        bt_airhockey_particles.setOnClickListener {
            ParticlesActivity.start(this)
        }
    }
}
