package com.vismiokt.landing_diary.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface PlantsRepository {
    suspend fun insertPlant(plant: Plant): Long

    suspend fun updatePlant(plant: Plant)

    suspend fun deletePlant(plant: Plant)

    fun getAllPlants(): Flow<List<Plant>>

    fun getPlantStream(id: Int): Flow<Plant?>

    suspend fun getImageUri(plantId: Int): List<ImageUri>

    suspend fun addImageUriList(imageUriList: List<ImageUri>)

    suspend fun deleteImageUriList(plantId: Int)

    suspend fun deleteImageUri(imageUri: Uri, plantId: Int)

}