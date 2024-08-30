package com.vismiokt.landing_diary.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.ImageUri
import com.vismiokt.landing_diary.data.PlantsRepository
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import com.vismiokt.landing_diary.navigation.Screen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlantCardViewModel(
    private val repository: PlantsRepository,
    savedStateHandle: SavedStateHandle
    ) : ViewModel() {

    private val plantId: Int = checkNotNull(savedStateHandle[Screen.PlantCardDestination.plantId])
//    val uiState: StateFlow<PlantCardUiState> =
//        repository.getPlantStream(plantId)
//            .filterNotNull()
//            .map {
//                PlantCardUiState(plantDetails = it.toPlantDetails(), imageUriList = repository.getImageUri(it.id), openDeleteDialog = openDialogDeleteState.value)
//            }.stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000L),
//                initialValue = PlantCardUiState()
//            )

    var plantUiState by mutableStateOf(PlantCardUiState())
        private set

    init {
        viewModelScope.launch {
            plantUiState = PlantCardUiState(
                plantDetails = repository.getPlantStream(plantId).filterNotNull().map { it.toPlantDetails() }.first(),
                imageUriList = imageUriListToUriList(repository.getImageUri(plantId))
            )
        }
    }


    fun imageUriListToUriList(imageUriList: List<ImageUri>): List<Uri> {
        return imageUriList.map { it.uriImg }
    }

    fun deletePlant(plantDetails: PlantDetails) {
        viewModelScope.launch {
            repository.deletePlant(plantDetails.toPlant())
            repository.deleteImageUriList(plantDetails.id)
        }

    }

    fun openDeleteDialog() {
        plantUiState = plantUiState.copy(openDeleteDialog = true)
    }

    fun closeDeleteDialog() {
        plantUiState = plantUiState.copy(openDeleteDialog = false)
    }

    fun openImageDialog(uri: Uri) {
        plantUiState = plantUiState.copy(openImageDialog = true, openImageUri = uri)
    }

    fun closeImageDialog() {
        plantUiState = plantUiState.copy(openImageDialog = false)
    }


}

data class PlantCardUiState (
    val plantDetails: PlantDetails = PlantDetails(),
    val imageUriList: List<Uri> = listOf(),
    val openDeleteDialog: Boolean = false,
    val openImageDialog: Boolean = false,
    val openImageUri: Uri = Uri.EMPTY
)