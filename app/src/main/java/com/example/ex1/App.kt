package com.example.ex1

import android.app.Application
import com.example.ex1.utilities.SignalManager

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
    }

}