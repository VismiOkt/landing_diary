package com.vismiokt.landing_diary.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vismiokt.landing_diary.data.Plant

@Composable
fun PlantCardMin(
    plant: Plant,
    onPlantClick: (Int) -> Unit
) {
    Card (
        shape = RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onPlantClick(plant.id) }
    ) {

        Row {
            Text(
                text = plant.nameVariety,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            )
            Image(painter = painterResource(plant.category.icon),
                contentDescription = null,
                alignment = Alignment.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .clip(
                        CircleShape
                    )
                    .size(35.dp)

            )
        }

    }

}