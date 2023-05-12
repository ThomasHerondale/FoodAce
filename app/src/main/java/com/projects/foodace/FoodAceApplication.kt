package com.projects.foodace

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.projects.foodace.model.CartViewModel
import kotlin.reflect.KProperty

class FoodAceApplication(
) : Application() {
    val loginManager by lazy { LoginManager(this) }
    private lateinit var _cartViewModel: CartViewModel
    val cartViewModel by lazy { _cartViewModel }

    override fun onCreate() {
        super.onCreate()
        _cartViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
            .create(CartViewModel::class.java)
    }

    // TODO: controllo e provider dinamici se mai ci saranno più viewModel
    operator fun getValue(ref: Any?, property: KProperty<*>) = cartViewModel
}
