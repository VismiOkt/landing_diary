package com.vismiokt.landing_diary.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nameVariety: String,
    val category: CategoryPlant = CategoryPlant.other,
    val timePlantSeeds: Long = 0,
    val dateLanding: Long = 0,
    val price: Float = 0.0f,
    val placeOfPurchase: String = "",
    val result: ResultPlant = ResultPlant.unknown,
    val note: String = ""

) {
}