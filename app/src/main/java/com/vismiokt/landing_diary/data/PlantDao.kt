package com.vismiokt.landing_diary.data

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlant(plant: Plant): Long

    @Update
    suspend fun updatePlant(plant: Plant)

    @Delete
    suspend fun deletePlant(plant: Plant)

    @Query("SELECT * from plants ORDER BY nameVariety ASC")
    fun getAllPlants(): Flow<List<Plant>>

    @Query("SELECT * from plants WHERE id = :id")
    fun getPlant(id: Int): Flow<Plant>

    @Query("SELECT * FROM uri_Image WHERE plantId=:plantId")
    suspend fun getImageUri(plantId: Int): List<ImageUri>

    @Insert
    suspend fun addImageUriList(imageUriList: List<ImageUri>)

    @Query("DELETE FROM uri_Image WHERE uriImg=:imageUri AND plantId=:plantId")
    suspend fun deleteImageUri(imageUri: Uri, plantId: Int)

    @Query("DELETE FROM uri_Image WHERE plantId=:plantId")
    suspend fun deleteImageUriList(plantId: Int)

}