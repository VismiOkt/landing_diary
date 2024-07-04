package com.vismiokt.landing_diary.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.vismiokt.landing_diary.ui.PlantDetails
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class FormatDateUseCase {
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToLocalDate(millis: Long) : LocalDate {
        return Instant
            .ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToLocalDateWithFormatter(date: LocalDate, dateTimeFormatter: DateTimeFormatter) : LocalDate {
        //Convert the date to a long in millis using a dateformmater
        val dateInMillis = LocalDate.parse(date.format(dateTimeFormatter), dateTimeFormatter)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        //Convert the millis to a localDate object
        return Instant
            .ofEpochMilli(dateInMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateToString(date: LocalDate): String {
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
        val dateInMillis = convertMillisToLocalDateWithFormatter(date, dateFormatter)
        return dateFormatter.format(dateInMillis)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertLocalDateToMillis(date: LocalDate) : Long {
        return date
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateNow(): String {
        return FormatDateUseCase().dateToString(LocalDate.now(ZoneId.systemDefault()))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateSet(plantDetails: PlantDetails): String {
        return FormatDateUseCase().dateToString(FormatDateUseCase().convertMillisToLocalDate(plantDetails.timePlantSeeds))
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    operator fun invoke(date: Long): String {
//        return dateToString(convertMillisToLocalDate(date))
//    }

}