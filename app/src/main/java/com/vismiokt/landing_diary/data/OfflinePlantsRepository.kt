package com.vismiokt.landing_diary.data

import kotlinx.coroutines.flow.Flow

class OfflinePlantsRepository(private val plantDao: PlantDao) : PlantsRepository {
    override suspend fun insertPlant(plant: Plant) = plantDao.insertPlant(plant)

    override fun getAllPlants(): Flow<List<Plant>> = plantDao.getAllPlants()

    override fun getPlantStream(id: Int): Flow<Plant?> = plantDao.getPlant(id)

    override suspend fun getImageUri(plantId: Int): List<ImageUri> = plantDao.getImageUri(plantId)
}