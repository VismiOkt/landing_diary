package com.vismiokt.landing_diary.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "uri_Image",
    foreignKeys = [ForeignKey(
        entity = Plant::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("plantId"),
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
    )
data class ImageUri(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val plantId: Int,
    val uriImg: Uri = Uri.EMPTY
)
