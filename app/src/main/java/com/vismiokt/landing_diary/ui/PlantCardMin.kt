package com.vismiokt.landing_diary.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vismiokt.landing_diary.data.Plant

@Composable
fun PlantCardMin(
    plant: Plant,
    onPlantClick: (Int) -> Unit
) {
    Card (modifier = Modifier.fillMaxWidth().padding(4.dp).clickable { onPlantClick(plant.id) }) {
        Text(
            text = plant.nameVariety,
            modifier = Modifier.padding(8.dp)
            )
    }

}