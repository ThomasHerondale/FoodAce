package com.projects.foodace

import android.app.Activity
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

operator fun Lazy<FoodAceApplication>.getValue(ref: Any?, property: KProperty<*>) =
    value.cartViewModel


fun Fragment.applicationViewModels() = (this.requireActivity().application as FoodAceApplication)

fun Activity.applicationViewModels() = (application as FoodAceApplication)
