package com.vismiokt.landing_diary.data

class OfflinePlantsRepository(private val plantDao: PlantDao) : PlantsRepository {
    override suspend fun insertPlant(plant: Plant) = plantDao.insertPlant(plant)
}