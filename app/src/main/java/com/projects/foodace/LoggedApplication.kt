package com.projects.foodace

import android.app.Application
import com.projects.foodace.database.User

class LoggedApplication(
) : Application() {
    lateinit var loginManager: LoginManager

    override fun onCreate() {
        super.onCreate()
        loginManager = LoginManager(this)
    }
}