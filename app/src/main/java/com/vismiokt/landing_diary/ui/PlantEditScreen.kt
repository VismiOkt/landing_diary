package com.vismiokt.landing_diary.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.CategoryPlant
import com.vismiokt.landing_diary.data.ResultPlant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Currency
import java.util.Locale

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlantEditScreen(
    navigateBack: () -> Unit,
    navigateCamera: () -> Unit,
    navigateToPlantDetails: (Int) -> Unit
) {
    val viewModel: PlantEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState = viewModel.plantUiState
    Scaffold(
        topBar = {
            TopBar(
                uiState.plantDetails.nameVariety,
                alpha = 0f,
                onBackButton = navigateBack
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        PlantEditBody(
            uiState,
            onValueChange = viewModel::updateUiState,
            closeDatePickerDialog = viewModel::closeDatePickerDialog,
            openDatePickerDialog = viewModel::openDatePickerDialog,
            onSave = {
                viewModel.savePlant(uiState.plantDetails)
                navigateToPlantDetails(uiState.plantDetails.id)
         //       navigateBack()
            },
            setDate = viewModel::setDate,
            requiredPermission = navigateCamera,
            saveImgUri = viewModel::addImageUri,
            imageUriList = viewModel.uriImgList,
            onDeleteUri = { viewModel.deleteImageUri(it) },
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(
                    start = padding.calculateStartPadding(LocalLayoutDirection.current),
                    end = padding.calculateEndPadding(LocalLayoutDirection.current),
                    top = padding.calculateTopPadding()
                )
                .fillMaxWidth()
        )
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantEditBody(
    plantUiState: PlantEditUiState,
    onValueChange: (PlantDetails) -> Unit = {},
    closeDatePickerDialog: () -> Unit,
    openDatePickerDialog: () -> Unit,
    onSave: () -> Unit,
    setDate: (PlantDetails) -> String,
    imageUriList: Flow<List<Uri>>,
    requiredPermission: () -> Unit,
    saveImgUri: (Uri) -> Unit,
    onDeleteUri: (Uri) -> Unit,
    modifier: Modifier
) {

    val plantDetails = plantUiState.plantDetails

    if (plantUiState.openDialogCalendar) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = {
                closeDatePickerDialog()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onValueChange(
                            plantDetails.copy(
                                timePlantSeeds = datePickerState.selectedDateMillis ?: 0
                            )
                        )
                        closeDatePickerDialog()
                    },
                ) {
                    Text(stringResource(R.string.app_ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        closeDatePickerDialog()
                    }
                ) {
                    Text(stringResource(R.string.app_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    Column(modifier = modifier.imePadding()) {
        OutlinedTextField(
            value = plantDetails.nameVariety,
            onValueChange = { onValueChange(plantDetails.copy(nameVariety = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(stringResource(R.string.entry_plant_name_input)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            singleLine = true
        )

        var expanded by remember { mutableStateOf(false) }
        var expandedResult by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                value = stringResource(plantDetails.category.title),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                label = { Text(stringResource(R.string.entry_plant_category_input)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                CategoryPlant.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(stringResource(option.title)) },
                        onClick = {
                            onValueChange(plantDetails.copy(category = option))
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )

                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        ) {
            OutlinedTextField(
                readOnly = true,
                value = setDate(plantDetails),
                label = { Text(stringResource(R.string.entry_plant_time_plant_seeds_input)) },
                onValueChange = {
                    onValueChange(plantDetails.copy(timePlantSeeds = it.toLongOrNull() ?: 0L))
                })
            IconButton(
                onClick = {
                    openDatePickerDialog()
                }
            ) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar")
            }
        }

        OutlinedTextField(
            value = plantDetails.price,
            onValueChange = { onValueChange(plantDetails.copy(price = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(R.string.entry_plant_price_input)) },
            leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = plantDetails.placeOfPurchase,
            onValueChange = { onValueChange(plantDetails.copy(placeOfPurchase = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(stringResource(R.string.entry_plant_place_of_purchase_input)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            singleLine = true
        )
        ExposedDropdownMenuBox(
            expanded = expandedResult,
            onExpandedChange = { expandedResult = it },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                value = stringResource(plantDetails.result.text),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                label = { Text(stringResource(R.string.entry_plant_result_input)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedResult) }
            )

            ExposedDropdownMenu(
                expanded = expandedResult,
                onDismissRequest = { expandedResult = false },
                //    modifier = Modifier.padding(20.dp)
            ) {
                ResultPlant.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(stringResource(option.text)) },
                        onClick = {
                            onValueChange(plantDetails.copy(result = option))
                            expandedResult = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )

                }
            }
        }
//        OutlinedTextField(
//            value = plantDetails.result,
//            onValueChange = { onValueChange(plantDetails.copy(result = it)) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//            label = { Text(stringResource(R.string.entry_plant_result_input)) },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
//            singleLine = true
//        )
        OutlinedTextField(
            value = plantDetails.note,
            onValueChange = { onValueChange(plantDetails.copy(note = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(stringResource(R.string.entry_plant_note_input)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        )
        ImagePreviewEdit(
            imageUriList.collectAsState(initial = listOf()).value,
            onDelete = {
                onDeleteUri(it)
            }
        )
        PhotoPickerScreen(
            saveImg = {
                saveImgUri(it)
            }
        )

        Button(
            onClick = {
                requiredPermission()

            },
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.entry_plant_make_photo))
        }


        Button(
            onClick = { onSave() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            Text(text = stringResource(R.string.plant_edit_save))
        }


    }
}

@Composable
fun ImagePreviewEdit(
    selectedImages: List<Uri>,
    onDelete: (Uri) -> Unit
) {
    LazyRow {
        items(selectedImages) { uri ->
            Box(modifier = Modifier) {
                AsyncImage(
                    modifier = Modifier
                        .size(250.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uri)
                        .crossfade(enable = true)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
                SmallFloatingActionButton(
                    onClick = { onDelete(uri) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Outlined.Clear, contentDescription = "")
                }
            }

        }
    }
}