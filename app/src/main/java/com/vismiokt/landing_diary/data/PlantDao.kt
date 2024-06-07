package com.vismiokt.landing_diary.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlant(plant: Plant)

}