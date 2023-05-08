package com.projects.foodace

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.projects.foodace.database.FoodAceRepository
import com.projects.foodace.databinding.ActivityStartBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val repository by lazy { FoodAceRepository(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPrefs = getSharedPreferences(
            "loginPrefs", MODE_PRIVATE
        )

        val loggedUsername: String? =
            if (sharedPrefs.getBoolean("keepLogin", false))
                sharedPrefs.getString("username", null)
            else
                (application as LoggedApplication).username

        if (loggedUsername != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("username", loggedUsername)

            startActivity(intent)
            finish()
        }

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener {
            val username = "${binding.usernameField.text}"
            val password = "${binding.passwordField.text}"

            val isLoginOkay = MutableLiveData<Boolean>()
            coroutineScope.launch {
                isLoginOkay.postValue(repository.checkCredentials(username, password))
            }

            isLoginOkay.observe(this) {
                if (isLoginOkay.value!!) { // login succeeded
                    Log.i("LOGIN", "Logging user $username")

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.putExtra("username", username)

                    // Set the session for the user
                    (application as LoggedApplication).username = username

                    // Manage the user wanting to be kept logged in
                    val prefsEditor = getSharedPreferences(
                        "loginPrefs", MODE_PRIVATE
                    ).edit()
                    prefsEditor.putBoolean("keepLogin", binding.keepLoginToggle.isChecked)
                    if (binding.keepLoginToggle.isChecked)
                        prefsEditor.putString("username", username)
                    else
                        prefsEditor.remove("username")
                    prefsEditor.apply()

                    startActivity(intent)
                    finish()
                } else { // login failed
                    Log.i("LOGIN", "Login failed for $username - $password")

                    Toast.makeText(
                        applicationContext,
                        R.string.login_failed_msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}