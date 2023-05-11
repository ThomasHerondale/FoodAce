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
    private lateinit var loginManager: LoginManager
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val repository by lazy { FoodAceRepository(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginManager = (application as LoggedApplication).loginManager

        val loggedUsername = loginManager.loggedUsername

        if (loggedUsername != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("username", loggedUsername)

            startActivity(intent)
            finish()
        }

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Manage the user wanting to be kept logged in
        binding.keepLoginToggle.setOnCheckedChangeListener { _, isChecked ->
            loginManager.setKeepLogin(isChecked)
        }

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
                    loginManager.loginUsername(username)

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