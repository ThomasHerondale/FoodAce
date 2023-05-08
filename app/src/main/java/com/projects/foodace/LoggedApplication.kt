package com.projects.foodace

import android.app.Application
import com.projects.foodace.database.User

class LoggedApplication(
) : Application() {
    var username: String? = null
        set(value) =
            if (username != null)
                throw IllegalStateException("Login is already registered.")
            else
                field = value
}