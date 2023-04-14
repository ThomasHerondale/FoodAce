package com.projects.foodace.model

import com.projects.foodace.R

enum class Category(val img: Int, val bgoundColor: Int) {
    BURGER(R.drawable.cat_2, R.color.light_purple),
    PIZZA(R.drawable.cat_1, R.color.light_cream),
    HOTDOG(R.drawable.cat_3, R.color.light_ice),
    DRINK(R.drawable.cat_4, R.color.light_mint),
    DESSERT(R.drawable.cat_5, R.color.light_pink);
}