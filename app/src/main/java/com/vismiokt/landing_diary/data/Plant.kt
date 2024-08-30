package com.vismiokt.landing_diary.data

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "plants")
data class Plant @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nameVariety: String,
    val category: CategoryPlant = CategoryPlant.other,
    val timePlantSeeds: LocalDate = LocalDate.now(),
    val dateLanding: Long = 0,
    val price: Float = 0.0f,
    val placeOfPurchase: String = "",
    val result: ResultPlant = ResultPlant.unknown,
    val note: String = "",
//    val uriImgList: List<Uri> = listOf()
) {
    val createdDateFormatted : String
        @RequiresApi(Build.VERSION_CODES.O)
        get() = timePlantSeeds.format(DateTimeFormatter.ofPattern("yyyy-MM-DD"))
}