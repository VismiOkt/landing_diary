package com.vismiokt.landing_diary.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate

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
//            _uriImgList.value.forEach {
//                saveImageToInternalStorage(context, it)
//            }
            viewModelScope.launch {
                id = repository.insertPlant(plantUiState.plantDetails.toPlant())
                repository.addImageUriList(_uriImgList.value.map {
                    ImageUri(
                        id = 0,
                        plantId = id.toInt(),
                        uriImg = it
                    )
                })
            }
        }
    }

//    fun copyImgInFile() {
//        val name = FormatDateUseCase().getDateNowForName()
//        val imgFile = File("Pictures/Landing_diary-Image/$name")
//        var imgBitmap: Bitmap? = null
//        if (imgFile.exists()) {
//            imgBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//        }
//    }

//    fun saveImageToInternalStorage(context: Context, uri: Uri) {
//        val name = FormatDateUseCase().getDateNowForName()
//        val inputStream = context.contentResolver.openInputStream(uri)
//        val outputStream = context.openFileOutput(name, Context.MODE_PRIVATE)
//        inputStream?.use { input ->
//            outputStream.use { output ->
//                input.copyTo(output)
//            }
//        }
//    }

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

data class PlantDetails @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: Int = 0,
    val nameVariety: String = "",
    val category: CategoryPlant = CategoryPlant.other,
    val timePlantSeeds: LocalDate = LocalDate.now(),
    val dateLanding: Long = 0,
    val price: String = "",
    val placeOfPurchase: String = "",
    val result: ResultPlant = ResultPlant.unknown,
    val note: String = "",
)

@RequiresApi(Build.VERSION_CODES.O)
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
)

@RequiresApi(Build.VERSION_CODES.O)
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

)

