package com.vismiokt.landing_diary.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.ImageUri
import com.vismiokt.landing_diary.data.Plant
import com.vismiokt.landing_diary.data.PlantsRepository
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import com.vismiokt.landing_diary.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlantEditViewModel (private val repository: PlantsRepository,
                          savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val plantId: Int = checkNotNull(savedStateHandle[Screen.PlantCardDestination.plantId])

//    val uiState: StateFlow<PlantEditUiState> =
//        repository.getPlantStream(plantId)
//            .filterNotNull()
//            .map {
//                PlantEditUiState(plantDetails = it.toPlantDetails(), uriImgList = repository.getImageUri(it.id))
//            }.stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000L),
//                initialValue = PlantEditUiState()
//            )
    var plantUiState by mutableStateOf(PlantEditUiState())
        private set

    private val _uriImgList = MutableStateFlow<List<Uri>>(listOf())
    val uriImgList: Flow<List<Uri>> = _uriImgList

    val uriImgListInDatabase = MutableStateFlow<List<Uri>>(listOf())


    init {
        viewModelScope.launch {
            plantUiState = PlantEditUiState(
                plantDetails = repository.getPlantStream(plantId).filterNotNull().map { it.toPlantDetails() }.first(),
        //        uriImgList = repository.getImageUri(plantId)
            )
        }
        initialList(plantId)
    }

    private fun initialList(plantId: Int) {
        viewModelScope.launch {
            _uriImgList.value = imageUriListToUriList(repository.getImageUri(plantId))
            uriImgListInDatabase.value = _uriImgList.value
        }
    }

    fun imageUriListToUriList(imageUriList: List<ImageUri>): List<Uri> {
        return imageUriList.map { it.uriImg }
    }



    private fun validatePlant(plantDetails: PlantDetails = plantUiState.plantDetails): Boolean {
        return plantDetails.nameVariety.isNotBlank()
    }

    fun updateUiState(plantDetails: PlantDetails) {
        plantUiState = PlantEditUiState(
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

    fun deleteImageUri(imgUri: Uri) {
        val uIL = _uriImgList.value.toMutableList()
        uIL.remove(imgUri)
        _uriImgList.value = uIL
    }

    fun okkk(element: Uri, list: List<Uri>): Boolean {
        list.forEach {
            if(element == it) return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun savePlant(plantDetails: PlantDetails) {
        val resultAdd = _uriImgList.value.filterNot { okkk(it, uriImgListInDatabase.value) }
        val resultDelete = uriImgListInDatabase.value.filterNot { okkk(it, _uriImgList.value) }

        if (validatePlant()) {
            viewModelScope.launch {
                repository.updatePlant(plantUiState.plantDetails.toPlant())

                repository.addImageUriList(resultAdd.map {
                    ImageUri(
                        id = 0,
                        plantId = plantDetails.id,
                        uriImg = it
                    )
                })
                if(resultDelete.isNotEmpty()) {
                    resultDelete.forEach { repository.deleteImageUri(it, plantDetails.id) }

                }

            }


        }
     }


    fun closeDatePickerDialog() {
        plantUiState = plantUiState.copy(openDialogCalendar = false)

    }

    fun openDatePickerDialog() {
        plantUiState = plantUiState.copy(openDialogCalendar = true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(plantDetails: PlantDetails): String {
        return if (plantUiState.plantDetails.timePlantSeeds == 0L) {
            updateUiState(plantDetails.copy(timePlantSeeds = FormatDateUseCase().getDateNowMillis()))
            FormatDateUseCase().getDateNowString()
        } else {
            FormatDateUseCase().getDateSet(plantUiState.plantDetails)
        }

    }


}

data class PlantEditUiState(
    val plantDetails: PlantDetails = PlantDetails(),
    val isEntryValid: Boolean = true,
    val openDialogCalendar: Boolean = false,
    val openCameraLd: Boolean = false,
//    val uriImgList: List<ImageUri> = listOf()
)

