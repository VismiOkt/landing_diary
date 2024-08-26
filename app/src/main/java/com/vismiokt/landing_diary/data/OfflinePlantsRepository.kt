package com.vismiokt.landing_diary.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow

class OfflinePlantsRepository(private val plantDao: PlantDao) : PlantsRepository {
    override suspend fun insertPlant(plant: Plant): Long = plantDao.insertPlant(plant)

    override suspend fun updatePlant(plant: Plant) = plantDao.updatePlant(plant)

    override suspend fun deletePlant(plant: Plant) = plantDao.deletePlant(plant)

    override fun getAllPlants(): Flow<List<Plant>> = plantDao.getAllPlants()

    override fun getPlantStream(id: Int): Flow<Plant?> = plantDao.getPlant(id)

    override suspend fun getImageUri(plantId: Int): List<ImageUri> = plantDao.getImageUri(plantId)

    override suspend fun addImageUriList(imageUriList: List<ImageUri>) = plantDao.addImageUriList(imageUriList)

    override suspend fun deleteImageUriList(plantId: Int) = plantDao.deleteImageUriList(plantId)

    override suspend fun deleteImageUri(imageUri: Uri, plantId: Int) = plantDao.deleteImageUri(imageUri, plantId)

    override fun getPlantsByCategory(categoryPlant: CategoryPlant): Flow<List<Plant>> = plantDao.getPlantsByCategory(categoryPlant)

    override fun getPlantsByResult(resultPlant: ResultPlant): Flow<List<Plant>> = plantDao.getPlantsByResult(resultPlant)

    override fun getPlantsByResultAndCategory(
        resultPlant: ResultPlant,
        categoryPlant: CategoryPlant
    ): Flow<List<Plant>> = plantDao.getPlantsByResultAndCategory(resultPlant, categoryPlant)
}