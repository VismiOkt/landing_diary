package com.vismiokt.landing_diary.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.vismiokt.landing_diary.data.CategoryPlant
import com.vismiokt.landing_diary.data.Plant
import com.vismiokt.landing_diary.data.ResultPlant
import java.time.LocalDate

data class PlantDetails @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: Int = 0,
    val nameVariety: String = "",
    val category: CategoryPlant = CategoryPlant.other,
    val timePlantSeeds: LocalDate = LocalDate.now(),
    val dateLanding: Long = 0,
    val price: String = "",
    val placeOfPurchase: String = "",
    val result: ResultPlant = ResultPlant.unknown,
    val note: String = "",
)

@RequiresApi(Build.VERSION_CODES.O)
fun PlantDetails.toPlant(): Plant = Plant(
    id = id,
    nameVariety = nameVariety,
    category = category,
    timePlantSeeds = timePlantSeeds,
    dateLanding = dateLanding,
    price = price.toFloatOrNull() ?: 0.0f,
    placeOfPurchase = placeOfPurchase,
    result = result,
    note = note,
)

@RequiresApi(Build.VERSION_CODES.O)
fun Plant.toPlantDetails(): PlantDetails = PlantDetails(
    id = id,
    nameVariety = nameVariety,
    category = category,
    timePlantSeeds = timePlantSeeds,
    dateLanding = dateLanding,
    price = price.toString(),
    placeOfPurchase = placeOfPurchase,
    result = result,
    note = note,

    )

fun Plant.doesMatchSearchQuery (query: String): Boolean {
    return nameVariety.contains(query, ignoreCase = true)
}