package com.vismiokt.landing_diary.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.CategoryPlant
import com.vismiokt.landing_diary.data.ImageUri
import com.vismiokt.landing_diary.data.Plant
import com.vismiokt.landing_diary.data.PlantsRepository
import com.vismiokt.landing_diary.data.ResultPlant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlantEntryViewModel(private val repository: PlantsRepository) : ViewModel() {

    var plantUiState by mutableStateOf(PlantEntryUiState())
        private set

    private val _uriImgList = MutableStateFlow<List<Uri>>(listOf())
    val uriImgList: Flow<List<Uri>> = _uriImgList


    private fun validatePlant(plantDetails: PlantDetails = plantUiState.plantDetails): Boolean {
        return plantDetails.nameVariety.isNotBlank()
    }

    fun updateUiState(plantDetails: PlantDetails) {
        plantUiState = PlantEntryUiState(
            plantDetails = plantDetails,
            isEntryValid = validatePlant(plantDetails),
   //         uriImgList = _uriImgList.value.map { ImageUri(id = 0, plantId = plantDetails.id, uriImg = it) }
        )
    }
    fun addImageUri(imgUri: Uri) {
        val uIL = _uriImgList.value.toMutableList()
        uIL.add(imgUri)
        _uriImgList.value = uIL
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun savePlant() {
        var id = 0L
        if (validatePlant()) {
            viewModelScope.launch {
                id = repository.insertPlant(plantUiState.plantDetails.toPlant())
                repository.addImageUriList(_uriImgList.value.map { ImageUri(id = 0, plantId = id.toInt(), uriImg = it) })
            }
    //        if (_uriImgList.value != )
//            viewModelScope.launch {
//                repository.addImageUriList(_uriImgList.value.map { ImageUri(id = 0, plantId = id.toInt(), uriImg = it) })
//            }

        }
    }

    fun closeDatePickerDialog() {
        plantUiState = plantUiState.copy(openDialogCalendar = false)

    }

    fun openDatePickerDialog() {
        plantUiState = plantUiState.copy(openDialogCalendar = true)
    }



}

data class PlantEntryUiState(
    val plantDetails: PlantDetails = PlantDetails(),
    val isEntryValid: Boolean = false,
    val openDialogCalendar: Boolean = false,
 //   val uriImgList: List<ImageUri> = listOf()
)

data class PlantDetails(
    val id: Int = 0,
    val nameVariety: String = "",
    val category: CategoryPlant = CategoryPlant.other,
    val timePlantSeeds: Long = 0,
    val dateLanding: Long = 0,
    val price: String = "",
    val placeOfPurchase: String = "",
    val result: ResultPlant = ResultPlant.unknown,
    val note: String = "",
 //   val uriImgList: List<Uri> = listOf()
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
    note = note,
 //   uriImgList = uriImgList
)

fun Plant.toPlantDetails(): PlantDetails = PlantDetails(
    id = id,
    nameVariety = nameVariety,
    category = category,
    timePlantSeeds = timePlantSeeds,
    dateLanding = dateLanding,
    price = price.toString(),
    placeOfPurchase = placeOfPurchase,
    result = result,
    note = note,
//    uriImgList = uriImgList

)

