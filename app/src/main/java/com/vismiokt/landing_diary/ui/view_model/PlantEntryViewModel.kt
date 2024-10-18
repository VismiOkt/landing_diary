package com.vismiokt.landing_diary.ui.view_model

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment.DIRECTORY_PICTURES
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.ImageUri
import com.vismiokt.landing_diary.data.PlantsRepository
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import com.vismiokt.landing_diary.domain.PlantDetails
import com.vismiokt.landing_diary.domain.toPlant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class PlantEntryViewModel(private val repository: PlantsRepository) : ViewModel() {

    var plantUiState by mutableStateOf(PlantEntryUiState())
        private set

    private val _uriImgList = MutableStateFlow<List<Uri>>(listOf())
    val uriImgList: Flow<List<Uri>> = _uriImgList


    private fun validatePlant(plantDetails: PlantDetails = plantUiState.plantDetails): Boolean {
        return plantDetails.nameVariety != ""

    }

    fun updateUiState(plantDetails: PlantDetails) {
        plantUiState = PlantEntryUiState(
            plantDetails = plantDetails,
            isEntryValid = validatePlant(plantDetails),
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun savePlant(context: Context) {
        var id = 0L
        if (plantUiState.isEntryValid) {
            val resultAddNew = _uriImgList.value.toMutableList().apply { replaceAll { uri ->
                saveImageToExternalStorage(context, uri)
            }  }
            viewModelScope.launch {
                id = repository.insertPlant(plantUiState.plantDetails.toPlant())
                repository.addImageUriList(resultAddNew.map {
                    ImageUri(
                        id = 0,
                        plantId = id.toInt(),
                        uriImg = it
                    )
                })
            }
        }
    }

    private fun saveImageToExternalStorage(context: Context, uri: Uri) : Uri {
        val name = FormatDateUseCase().getDateNowForName() + ".jpg"
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.getExternalFilesDir(DIRECTORY_PICTURES), name)
        var uriNew: Uri = Uri.EMPTY
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
                uriNew = Uri.fromFile(file)
            }
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(plantDetails: PlantDetails): String {
        return FormatDateUseCase().getDateSet(plantUiState.plantDetails)

    }


}

data class PlantEntryUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val plantDetails: PlantDetails = PlantDetails(),
    val isEntryValid: Boolean = false,
    val openDialogCalendar: Boolean = false,
    val openCameraLd: Boolean = false,
    val showCamera: Boolean = false
)



