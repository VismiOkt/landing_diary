package com.vismiokt.landing_diary.ui.view_model

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
import com.vismiokt.landing_diary.domain.PlantDetails
import com.vismiokt.landing_diary.domain.toPlant
import com.vismiokt.landing_diary.domain.toPlantDetails
import com.vismiokt.landing_diary.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class PlantCardViewModel @Inject constructor (
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

    private val _plantUiState = MutableStateFlow(PlantCardUiState())
    val plantUiState: StateFlow<PlantCardUiState> = _plantUiState.asStateFlow()

    init {
        updateUiState()
    }

    private fun updateUiState() {
        viewModelScope.launch {
            _plantUiState.update {
                it.copy(
                    plantDetails = repository.getPlantStream(plantId).filterNotNull().map { plant -> plant.toPlantDetails() },
                    imageUriList = imageUriListToUriList(repository.getImageUri(plantId))
                )
            }
        }
    }


    fun imageUriListToUriList(imageUriList: Flow<List<ImageUri>>): Flow<List<Uri>> {
        return imageUriList.map {
            it.map { uri ->
                uri.uriImg }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deletePlant(plantDetails: PlantDetails) {
        viewModelScope.launch {
            repository.deletePlant(plantDetails.toPlant())
            repository.deleteImageUriList(plantDetails.id)
        }

    }

    fun openDeleteDialog() {
        _plantUiState.update {
            it.copy(openDeleteDialog = true)
        }
    }

    fun closeDeleteDialog() {
        _plantUiState.update {
            it.copy(openDeleteDialog = false)
        }
    }

    fun openImageDialog(uri: Uri) {
        _plantUiState.update {
            it.copy(openImageDialog = true, openImageUri = uri)
        }
    }

    fun closeImageDialog() {
        _plantUiState.update {
            it.copy(openImageDialog = false)
        }
    }
}

data class PlantCardUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val plantDetails: Flow<PlantDetails> = MutableStateFlow(PlantDetails()),
    val imageUriList: Flow<List<Uri>> = MutableStateFlow(listOf()),
    val openDeleteDialog: Boolean = false,
    val openImageDialog: Boolean = false,
    val openImageUri: Uri = Uri.EMPTY
)