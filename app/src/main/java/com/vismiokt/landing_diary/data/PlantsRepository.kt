package com.vismiokt.landing_diary.data

import kotlinx.coroutines.flow.Flow

interface PlantsRepository {
    suspend fun insertPlant(plant: Plant)

    fun getAllPlants(): Flow<List<Plant>>

    fun getPlantStream(id: Int): Flow<Plant?>

}