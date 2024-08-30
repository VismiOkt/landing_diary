package com.vismiokt.landing_diary.data

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

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

    @Query("SELECT * from plants WHERE category=:categoryPlant ORDER BY nameVariety ASC")
    fun getPlantsByCategory(categoryPlant: CategoryPlant): Flow<List<Plant>>

    @Query("SELECT * from plants WHERE result=:resultPlant ORDER BY nameVariety ASC")
    fun getPlantsByResult(resultPlant: ResultPlant): Flow<List<Plant>>

    @Query("SELECT * from plants WHERE result=:resultPlant AND category=:categoryPlant ORDER BY nameVariety ASC")
    fun getPlantsByResultAndCategory(resultPlant: ResultPlant, categoryPlant: CategoryPlant): Flow<List<Plant>>

    @Query("SELECT * from plants WHERE result=:resultPlant AND category=:categoryPlant AND (date(timePlantSeeds) BETWEEN :date1 AND :date2) ORDER BY nameVariety ASC")
    fun getPlantsByResultAndCategoryAndYear(resultPlant: ResultPlant, categoryPlant: CategoryPlant, date1: String, date2: String): Flow<List<Plant>>

    @Query("SELECT * from plants WHERE (date(timePlantSeeds) BETWEEN :date1 AND :date2)")
    fun getPlantsByYear(date1: String, date2: String): Flow<List<Plant>>

    @Query("SELECT * from plants WHERE result=:resultPlant AND (date(timePlantSeeds) BETWEEN :date1 AND :date2) ORDER BY nameVariety ASC")
    fun getPlantsByResultAndYear(resultPlant: ResultPlant, date1: String, date2: String): Flow<List<Plant>>

    @Query("SELECT * from plants WHERE category=:categoryPlant AND (date(timePlantSeeds) BETWEEN :date1 AND :date2) ORDER BY nameVariety ASC")
    fun getPlantsByCategoryAndYear(categoryPlant: CategoryPlant, date1: String, date2: String): Flow<List<Plant>>

    @Query("SELECT date(timePlantSeeds) from plants")
    fun getPlantsDate(): Flow<List<String>>

}