package com.vismiokt.landing_diary.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vismiokt.landing_diary.data.Plant

@Composable
fun PlantCardMin(
    plant: Plant
) {
    Card (modifier = Modifier.fillMaxWidth()) {
        Text(text = plant.nameVariety)
    }

}