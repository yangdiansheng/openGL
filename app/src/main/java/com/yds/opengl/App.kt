package com.yds.opengl

import android.app.Activity
import android.app.Application
import android.content.ContextWrapper
import android.os.Bundle

private lateinit var INSTANCE: Application

object AppContext : ContextWrapper(INSTANCE)

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}

