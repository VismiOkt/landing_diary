package com.vismiokt.landing_diary.ui.view_model

import android.content.Context
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
import com.vismiokt.landing_diary.domain.PlantDetails
import com.vismiokt.landing_diary.domain.toPlant
import com.vismiokt.landing_diary.domain.toPlantDetails
import com.vismiokt.landing_diary.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
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
                uri.uriImg }
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
            if(element == it) return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun savePlant(plantDetails: PlantDetails, context: Context) {
        val contentResolver = context.contentResolver
        val resultAdd = _uriImgList.value.filterNot { filterForImgList(it, uriImgListInDatabase.value) }
        val resultDelete = uriImgListInDatabase.value.filterNot { filterForImgList(it, _uriImgList.value) }

        if (validatePlant()) {
            viewModelScope.launch {
                repository.updatePlant(plantUiState.plantDetails.toPlant())
             //   resultAdd.forEach { uri: Uri? -> uri?.let { saveImageToInternalStorage(context, it) } }

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
            if (resultDelete.isNotEmpty()) {
                resultDelete.forEach {
                    try {
                        contentResolver.delete(it, null, null)
                    } catch (ex: SecurityException) {

                    }
                }
            }
        //        resultAdd.forEach { saveImageToInternalStorage(context, it) }
        }
     }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun saveImageToInternalStorage(context: Context, uri: Uri) {
//        val name = FormatDateUseCase().getDateNowForName()
//        val inputStream = context.contentResolver.openInputStream(uri)
//        val outputStream = context.openFileOutput(name, Context.MODE_PRIVATE)
//       // var uriNew: Uri = Uri.EMPTY
//        inputStream?.use { input ->
//            outputStream.use { output ->
//                input.copyTo(output)
//              //  uriNew = Uri.fromFile(output)
//            }
//        }
//
//    }

//    @SuppressLint("RestrictedApi")
//    fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File? {
//        // Get the pictures directory that's inside the app-specific directory on
//        // external storage.
//        val file = File(context.getExternalFilesDir(
//            Environment.DIRECTORY_PICTURES), albumName)
//        if (!file?.mkdirs()) {
//            Log.e(LOG_TAG, "Directory not created")
//        }
//        return file
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

data class PlantEditUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val plantDetails: PlantDetails = PlantDetails(),
    val isEntryValid: Boolean = true,
    val openDialogCalendar: Boolean = false,
    val openCameraLd: Boolean = false,
    val showCamera: Boolean = false
)

