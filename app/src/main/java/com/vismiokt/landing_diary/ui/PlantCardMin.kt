package com.vismiokt.landing_diary.ui

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vismiokt.landing_diary.data.Plant

@Composable
fun PlantCardMin(
    plant: Plant
) {
    Card {
        Text(text = plant.nameVariety)
    }

}