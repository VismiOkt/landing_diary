package com.vismiokt.landing_diary.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.vismiokt.landing_diary.data.CategoryPlant
import com.vismiokt.landing_diary.data.Plant
import com.vismiokt.landing_diary.data.PlantsRepository

class PlantEntryViewModel(private val repository: PlantsRepository) : ViewModel() {

    var plantUiState by mutableStateOf(PlantUiState())
        private set

    private fun validatePlant(plantDetails: PlantDetails = plantUiState.plantDetails): Boolean {
        return plantDetails.nameVariety.isNotBlank()
    }

    fun updateUiState(plantDetails: PlantDetails) {
        plantUiState = PlantUiState(plantDetails = plantDetails, isEntryValid = validatePlant(plantDetails))
    }

    suspend fun savePlant() {
        if(validatePlant()) {
            repository.insertPlant(plantUiState.plantDetails.toPlant())
        }
    }




}

data class PlantUiState(
    val plantDetails: PlantDetails = PlantDetails(),
    val isEntryValid: Boolean = false
)

data class PlantDetails(
    val id: Int = 0,
    val nameVariety: String = "",
    val category: CategoryPlant = CategoryPlant.other,
    val timePlantSeeds: String = "",
    val dateLanding: String = "",
    val price: String = "",
    val placeOfPurchase: String = "",
    val result: String = "",
    val note: String = ""
)

fun PlantDetails.toPlant(): Plant = Plant(
    id = id,
    nameVariety = nameVariety,
    category = category,
    timePlantSeeds = timePlantSeeds,
    dateLanding = dateLanding,
    price = price.toFloatOrNull() ?: 0.0f,
    placeOfPurchase = placeOfPurchase,
    result = result,
    note = note
)