package com.example.dessertclicker.ui

import com.example.dessertclicker.R
import com.example.dessertclicker.model.Dessert

data class AppUiState(
    val desertSold: Int = 0,
    val totalRevenue: Int = 0,
    val isLastDessert: Boolean = false,
    val dessert: Dessert = Dessert(R.drawable.cupcake, 5, 0)
)