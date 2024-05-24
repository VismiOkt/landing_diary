package com.vismiokt.landing_diary.data

import android.provider.ContactsContract.Data

class Plant (
    val id: Int,
    val nameVariety: String,
    val category: CategoryPlant,
    val timePlantSeeds: Data,
    val dateLanding: Data,
    val price: Float,
    val placeOfPurchase: String,
    val result: String,
    val note: String

) {
}