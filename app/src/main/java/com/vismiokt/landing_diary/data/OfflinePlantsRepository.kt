package com.vismiokt.landing_diary.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class OfflinePlantsRepository(private val plantDao: PlantDao) : PlantsRepository {
    override suspend fun insertPlant(plant: Plant): Long = plantDao.insertPlant(plant)

    override suspend fun updatePlant(plant: Plant) = plantDao.updatePlant(plant)

    override suspend fun deletePlant(plant: Plant) = plantDao.deletePlant(plant)

    override fun getAllPlants(): Flow<List<Plant>> = plantDao.getAllPlants()

    override fun getPlantStream(id: Int): Flow<Plant?> = plantDao.getPlant(id)

    override fun getImageUri(plantId: Int): Flow<List<ImageUri>> = plantDao.getImageUri(plantId)

    override suspend fun addImageUriList(imageUriList: List<ImageUri>) = plantDao.addImageUriList(imageUriList)

    override suspend fun deleteImageUriList(plantId: Int) = plantDao.deleteImageUriList(plantId)

    override suspend fun deleteImageUri(imageUri: Uri, plantId: Int) = plantDao.deleteImageUri(imageUri, plantId)

    override fun getPlantsByCategory(categoryPlant: CategoryPlant): Flow<List<Plant>> = plantDao.getPlantsByCategory(categoryPlant)

    override fun getPlantsByResult(resultPlant: ResultPlant): Flow<List<Plant>> = plantDao.getPlantsByResult(resultPlant)

    override fun getPlantsByYear(year: String,): Flow<List<Plant>> = plantDao.getPlantsByYear("$year-01-01",
        "$year-12-31")

    override suspend fun getPlantsDate(): Flow<List<String>> = plantDao.getPlantsDate()

    override fun getPlantsByCategoryAndYear(
        categoryPlant: CategoryPlant,
        year: String,
    ): Flow<List<Plant>> = plantDao.getPlantsByCategoryAndYear(
        categoryPlant,
        "$year-01-01",
        "$year-12-31"
    )

    override fun getPlantsByResultAndCategoryAndYear(
        resultPlant: ResultPlant,
        categoryPlant: CategoryPlant,
        year: String,
    ): Flow<List<Plant>> = plantDao.getPlantsByResultAndCategoryAndYear(resultPlant, categoryPlant, "$year-01-01",
        "$year-12-31")

    override fun getPlantsByResultAndYear(
        resultPlant: ResultPlant,
        year: String,
    ): Flow<List<Plant>> = plantDao.getPlantsByResultAndYear(resultPlant, "$year-01-01",
        "$year-12-31")

    override fun getPlantsByResultAndCategory(
        resultPlant: ResultPlant,
        categoryPlant: CategoryPlant
    ): Flow<List<Plant>> = plantDao.getPlantsByResultAndCategory(resultPlant, categoryPlant)


}