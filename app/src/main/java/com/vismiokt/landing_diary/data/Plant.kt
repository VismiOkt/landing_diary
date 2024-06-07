package com.vismiokt.landing_diary.data

import android.icu.util.Calendar
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity(tableName = "plants")
data class Plant (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nameVariety: String,
    val category: CategoryPlant = CategoryPlant.other,
    val timePlantSeeds: String = "",
    val dateLanding: String = "",
    val price: Float = 0.0f,
    val placeOfPurchase: String = "",
    val result: String = "",
    val note: String = ""

) {
}