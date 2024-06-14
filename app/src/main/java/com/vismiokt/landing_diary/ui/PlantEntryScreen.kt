package com.vismiokt.landing_diary.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.CategoryPlant
import java.util.Currency
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlantEntryScreen(
    navigateBack: () -> Unit

) {
    val viewModel: PlantEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)

    Scaffold(
        topBar = {
            TopBar(
                R.string.entry_plant_title,
                onBackButton = navigateBack
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        PlantEntryBody(
            viewModel.plantUiState,
            onValueChange = viewModel::updateUiState,
            modifier = Modifier
                .padding(
                    start = padding.calculateStartPadding(LocalLayoutDirection.current),
                    end = padding.calculateEndPadding(LocalLayoutDirection.current),
                    top = padding.calculateTopPadding()
                )
                .fillMaxWidth()
        )


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantEntryBody(
    plantUiState: PlantUiState,
    onValueChange: (PlantDetails) -> Unit = {},
    modifier: Modifier
) {
    val plantDetails = plantUiState.plantDetails
    Column(modifier = modifier) {
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
        OutlinedTextField(
            value = plantDetails.result,
            onValueChange = { onValueChange(plantDetails.copy(result = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(stringResource(R.string.entry_plant_result_input)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = plantDetails.note,
            onValueChange = { onValueChange(plantDetails.copy(note = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(stringResource(R.string.entry_plant_note_input)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        )


    }
}