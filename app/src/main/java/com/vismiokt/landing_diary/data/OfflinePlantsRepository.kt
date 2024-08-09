package com.vismiokt.landing_diary.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow

class OfflinePlantsRepository(private val plantDao: PlantDao) : PlantsRepository {
    override suspend fun insertPlant(plant: Plant): Long = plantDao.insertPlant(plant)

    override fun getAllPlants(): Flow<List<Plant>> = plantDao.getAllPlants()

    override fun getPlantStream(id: Int): Flow<Plant?> = plantDao.getPlant(id)

    override suspend fun getImageUri(plantId: Int): List<ImageUri> = plantDao.getImageUri(plantId)

    override suspend fun addImageUriList(imageUriList: List<ImageUri>) = plantDao.addImageUriList(imageUriList)
}