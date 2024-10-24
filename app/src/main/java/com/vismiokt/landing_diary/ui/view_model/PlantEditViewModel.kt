package com.vismiokt.landing_diary.ui.view_model

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.ImageUri
import com.vismiokt.landing_diary.data.PlantsRepository
import com.vismiokt.landing_diary.domain.ExternalStorageUseCase
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import com.vismiokt.landing_diary.domain.PlantDetails
import com.vismiokt.landing_diary.domain.toPlant
import com.vismiokt.landing_diary.domain.toPlantDetails
import com.vismiokt.landing_diary.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class PlantEditViewModel @Inject constructor(
    private val repository: PlantsRepository,
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
                plantDetails = repository.getPlantStream(plantId).filterNotNull()
                    .map { it.toPlantDetails() }.first(),
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

    suspend fun imageUriListToUriList(imageUriList: Flow<List<ImageUri>>): List<Uri> {
        return imageUriList.map {
            it.map { uri ->
                uri.uriImg
            }
        }.stateIn(viewModelScope).value
    }

//    fun imageUriListToUriList(imageUriList: List<ImageUri>): List<Uri> {
//        return imageUriList.map { it.uriImg }
//    }


    private fun validatePlant(plantDetails: PlantDetails = plantUiState.plantDetails): Boolean {
        return plantDetails.nameVariety.isNotBlank()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    private fun filterForImgList(element: Uri, list: List<Uri>): Boolean {
        list.forEach {
            if (element == it) return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun savePlant(plantDetails: PlantDetails, context: Context) {
        val contentResolver = context.contentResolver
        val resultAdd =
            _uriImgList.value.filterNot { filterForImgList(it, uriImgListInDatabase.value) }
        val resultDelete =
            uriImgListInDatabase.value.filterNot { filterForImgList(it, _uriImgList.value) }

        if (validatePlant()) {
            viewModelScope.launch {
                repository.updatePlant(plantUiState.plantDetails.toPlant())
                if (resultAdd.isNotEmpty()) {
                    val resultAddNew = resultAdd.toMutableList().apply {
                        replaceAll { uri ->
                            saveImageToExternalStorage(context, uri)
                        }
                    }
                    //    resultAdd.forEach { uri: Uri? -> uri?.let {  } }

                    repository.addImageUriList(resultAddNew.map {
                        ImageUri(
                            id = 0,
                            plantId = plantDetails.id,
                            uriImg = it
                        )
                    })
                }
                if (resultDelete.isNotEmpty()) {
                    resultDelete.forEach { repository.deleteImageUri(it, plantDetails.id) }
                }
            }
            if (resultDelete.isNotEmpty()) {
                resultDelete.forEach {
                    try {
                        //                      contentResolver.delete(it, null, null)
                        it.toFile().delete()
                    } catch (ex: Exception) {
//                    } catch (ex: SecurityException) {

                    }
                }
            }

        }
    }


    private fun saveImageToExternalStorage(context: Context, uri: Uri): Uri {
        val name = FormatDateUseCase().getDateNowForName() + ".jpg"
        val inputStream = context.contentResolver.openInputStream(uri)
        var uriNew: Uri = Uri.EMPTY
        val file = if (ExternalStorageUseCase().isExternalStorageWritable()) {
            File(context.getExternalFilesDir(DIRECTORY_PICTURES), name)
        } else {
            File(context.filesDir, name)
        }
        try {
//            val outputStream = context.openFileOutput(name, Context.MODE_PRIVATE)
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                    uriNew = Uri.fromFile(file)
                }

            }
        } catch (_: IOException) {
            plantUiState = plantUiState.copy(imageSaveError = true)
        }
        return uriNew
    }


    fun closeDatePickerDialog() {
        plantUiState = plantUiState.copy(openDialogCalendar = false)

    }

    fun openDatePickerDialog() {
        plantUiState = plantUiState.copy(openDialogCalendar = true)
    }

    fun closeCamera() {
        plantUiState = plantUiState.copy(showCamera = false)

    }

    fun openCamera() {
        plantUiState = plantUiState.copy(showCamera = true)
    }

    fun resetImageSaveError() {
        plantUiState = plantUiState.copy(imageSaveError = false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(plantDetails: PlantDetails): String {
        return FormatDateUseCase().getDateSet(plantUiState.plantDetails)
    }


}

data class PlantEditUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val plantDetails: PlantDetails = PlantDetails(),
    val isEntryValid: Boolean = true,
    val openDialogCalendar: Boolean = false,
    val openCameraLd: Boolean = false,
    val showCamera: Boolean = false,
    val imageSaveError: Boolean = false
)

