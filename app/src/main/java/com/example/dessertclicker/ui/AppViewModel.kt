package com.example.dessertclicker.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.R
import com.example.dessertclicker.data.Datasource.dessertList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel: ViewModel() {
    private var _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private var dessertCount  by mutableStateOf(0)
    private var revenue by mutableStateOf(0)
    private var lastDessert = _uiState.value.dessert

    fun updateState() {
        updateCountAndRevenue()
        updateDessert()
        Log.d("updateState start", "count: $dessertCount, revenue: $revenue, dessert: $lastDessert")
        _uiState.update {
            currentState -> currentState.copy(
                desertSold = dessertCount,
                totalRevenue = revenue,
                dessert = lastDessert
            )
        }
    }

    private fun updateDessert() {
            for (dessert in dessertList) {
                if (dessertCount >= dessert.startProductionAmount) {
                    lastDessert = dessert
                } else {
                    break
                }
            }
    }

    private fun updateCountAndRevenue() {
        dessertCount++
        revenue += _uiState.value.dessert.price
    }

    fun shareSoldDessertsInformation(
        intentContext: Context
    ) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                intentContext.getString(
                    R.string.share_text, dessertCount, revenue
                )
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)

        try {
            ContextCompat.startActivity(intentContext, shareIntent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                intentContext,
                intentContext.getString(R.string.sharing_not_available),
                Toast.LENGTH_LONG
            ).show()
        }
    }

}