package com.vismiokt.landing_diary.data

interface PlantsRepository {
    suspend fun insertPlant(plant: Plant)

}