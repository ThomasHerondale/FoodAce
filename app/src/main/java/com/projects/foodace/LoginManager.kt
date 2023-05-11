package com.projects.foodace

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

private const val KEEP_LOGIN_KEY = "keepLogin"
private const val USERNAME_KEY = "username"

private const val LOGIN_TAG = "LOGIN"

class LoginManager(application: LoggedApplication) {
    private val sharedPrefs: SharedPreferences

    private var keepLogin: Boolean

    var loggedUsername: String?
        private set

    init {
        sharedPrefs = application.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        keepLogin = sharedPrefs.getBoolean(KEEP_LOGIN_KEY, false)
        Log.v(LOGIN_TAG, "Kept login: $keepLogin")
        if (keepLogin) {
            val username = sharedPrefs.getString(USERNAME_KEY, null)
                ?: throw IllegalStateException("Could not find logged user to keep login.")
            loggedUsername = username
            Log.v(LOGIN_TAG, "Login found: $username")
        } else
            loggedUsername = null
    }

    fun setKeepLogin(value: Boolean) {
        keepLogin = value
        sharedPrefs.edit().putBoolean(KEEP_LOGIN_KEY, value).apply()
        Log.v(LOGIN_TAG, "Will keep login: $keepLogin")
    }

    fun loginUsername(username: String) {
        if (loggedUsername != null)
            throw IllegalStateException("Login is already registered.")
        else {
            loggedUsername = username
            if (keepLogin) {
                sharedPrefs.edit().putString(USERNAME_KEY, username).apply()
            }
        }
    }

    fun logout() {
        if (loggedUsername == null)
            throw IllegalStateException("Login has not been registered.")
        else {
            loggedUsername = null
            val editor = sharedPrefs.edit()
            editor.remove(USERNAME_KEY)
            if (keepLogin) {
                editor.remove(USERNAME_KEY)
                setKeepLogin(false)
            }
            editor.apply()
        }
    }
}