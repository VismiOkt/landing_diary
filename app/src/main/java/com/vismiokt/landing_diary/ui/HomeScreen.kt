package com.vismiokt.landing_diary.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.CategoryPlant
import com.vismiokt.landing_diary.data.FilterPlant
import com.vismiokt.landing_diary.data.Plant
import com.vismiokt.landing_diary.data.ResultPlant

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    drawerState: DrawerState,
    navigateToPlantEntry: () -> Unit,
    navigateToPlantDetails: (Int) -> Unit

) {
    val viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState = viewModel.homeUiState
    val plants = uiState.plants.collectAsState(initial = listOf())
    Scaffold(
        topBar = {
            LdTopAppBar(R.string.app_name_ru, drawerState)
//             {
//                scope.launch {
//                    drawerState.apply {
//                        if (isClosed) open() else close()
//                    }
//                }
//            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToPlantEntry) {
                Icon(Icons.Outlined.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column {
            FilterAppBar(
                uiState,
                padding,
                onClickValueResult = { viewModel.onClickValueResult(it) },
                onClickValueCategory = { viewModel.onClickValueCategory(it) },
                deleteFilterCategory = { viewModel.deleteFilterCategory() },
                deleteFilterResult = { viewModel.deleteFilterResult() }
            )
            if (plants.value.isEmpty()) {
                Text(
                    text = stringResource(R.string.app_no_plant_description),
                    textAlign = TextAlign.Center,
                    //  style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        //    .padding(padding)
                        .padding(start = 16.dp, end = 16.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
//                    .padding(
//                        start = padding.calculateStartPadding(LocalLayoutDirection.current),
//                        end = padding.calculateEndPadding(LocalLayoutDirection.current),
//                        top = padding.calculateTopPadding()
//                    )
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(plants.value, key = { it.id }) { plant ->
                        PlantCardMin(
                            plant = plant,
                            onPlantClick = { navigateToPlantDetails(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterAppBar(
    uiState: HomeUiState,
    padding: PaddingValues,
    onClickValueCategory: (CategoryPlant) -> Unit,
    onClickValueResult: (ResultPlant) -> Unit,
    deleteFilterCategory: () -> Unit,
    deleteFilterResult: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(
                start = padding.calculateStartPadding(LocalLayoutDirection.current),
                end = padding.calculateEndPadding(LocalLayoutDirection.current),
                top = padding.calculateTopPadding()
            )
            .padding(start = 10.dp, end = 2.dp),
    ) {
        FilterPlant.entries.forEach {
            var expanded by remember { mutableStateOf(false) }
            when (it) {
                FilterPlant.category -> {
                    Box(modifier = Modifier.weight(1f)) {
                        FilterChip(
                            selected = uiState.onFilterCategory,
                            onClick = {
                                expanded = true
                            },
                            label = {
                                if (uiState.filterCategory == null) {
                                    Text(stringResource(it.title))
                                } else {
                                    Text(text = stringResource(uiState.filterCategory.title))
                                }
                            },
                            trailingIcon = {
                                Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                            },
                            leadingIcon = {
                                if (uiState.onFilterCategory) {
                                    IconButton(onClick = { deleteFilterCategory() }, modifier = Modifier.size(16.dp)) {
                                        Icon(
                                            Icons.Outlined.Clear,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .fillMaxWidth()
                        )
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            CategoryPlant.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(it.title)) },
                                    onClick = {
                                        expanded = false
                                        onClickValueCategory(it)

                                    })
                            }
                        }
                    }


                }

                FilterPlant.result -> {
                    Box(modifier = Modifier.weight(1f)) {
                        FilterChip(
                            selected = uiState.onFilterResult,
                            onClick = {
                                expanded = true
                            },
                            label = {
                                if (uiState.filterResult == null) {
                                    Text(stringResource(it.title))
                                } else {
                                    Text(text = stringResource(uiState.filterResult.text))
                                }

                            },
                            trailingIcon = {
                                Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                            },
                            leadingIcon = {
                                if (uiState.onFilterResult) {
                                    IconButton(onClick = { deleteFilterResult() }, modifier = Modifier.size(16.dp)) {
                                        Icon(
                                            Icons.Outlined.Clear,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .fillMaxWidth()
                        )
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            ResultPlant.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(it.text)) },
                                    onClick = {
                                        expanded = false
                                        onClickValueResult(it)
                                    })
                            }
                        }
                    }

                }

                FilterPlant.year -> {


                }

            }


        }

    }
}

@Composable
fun DropdownItem(
    values: FilterPlant,
    onClickValueCategory: (CategoryPlant) -> Unit,
    onClickValueResult: (ResultPlant) -> Unit
) {
    when (values) {
        FilterPlant.category -> {
            CategoryPlant.entries.forEach {
                DropdownMenuItem(
                    text = { Text(text = stringResource(it.title)) },
                    onClick = { onClickValueCategory(it) })
            }
        }

        FilterPlant.result -> {
            ResultPlant.entries.forEach {
                DropdownMenuItem(
                    text = { Text(text = stringResource(it.text)) },
                    onClick = { onClickValueResult(it) })
            }
        }

        FilterPlant.year -> {


        }

    }

}

