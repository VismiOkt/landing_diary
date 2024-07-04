package com.vismiokt.landing_diary.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlant(plant: Plant)

    @Query("SELECT * from plants ORDER BY nameVariety ASC")
    fun getAllPlants(): Flow<List<Plant>>

    @Query("SELECT * from plants WHERE id = :id")
    fun getPlant(id: Int): Flow<Plant>

}