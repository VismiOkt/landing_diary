package com.vismiokt.landing_diary.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.CategoryPlant
import com.vismiokt.landing_diary.data.FilterPlant
import com.vismiokt.landing_diary.data.Plant
import com.vismiokt.landing_diary.data.ResultPlant
import com.vismiokt.landing_diary.ui.view_model.AppViewModelProvider
import com.vismiokt.landing_diary.ui.view_model.HomeUiState
import com.vismiokt.landing_diary.ui.view_model.HomeViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    drawerState: DrawerState,
    navigateToPlantEntry: () -> Unit,
    navigateToPlantDetails: (Int) -> Unit,


) {
    val viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState = viewModel.homeUiState.collectAsState()
    val plants = viewModel.resPlants.collectAsState(initial = listOf<Plant>())
    val searchText = viewModel.searchText.collectAsState("")
    val isSearching = viewModel.isSearching.collectAsState()


    Scaffold(
        topBar = {
            LdTopAppBar(
                R.string.app_name_ru,
                onSearch = { viewModel.onSearchBar() },
                drawerState
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToPlantEntry) {
                Icon(Icons.Outlined.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column (
            modifier = Modifier
                .padding(
                    start = padding.calculateStartPadding(LocalLayoutDirection.current),
                    end = padding.calculateEndPadding(LocalLayoutDirection.current),
                    top = padding.calculateTopPadding()
                )
        ) {
            if (uiState.value.showBarSearch) {
                TopAppBarSearch(
                    searchText = searchText,
                    onSearchTextChange = viewModel::onSearchTextChange,
                    onClearSearchText = viewModel::onClearSearchText,

                )
            }
            FilterAppBar(
                uiState,
                plantsYear = viewModel.plantsYear.collectAsState(listOf()),
                onClickValueResult = { viewModel.onClickValueResult(it) },
                onClickValueCategory = { viewModel.onClickValueCategory(it) },
                deleteFilterCategory = { viewModel.deleteFilterCategory() },
                deleteFilterResult = { viewModel.deleteFilterResult() },
                deleteFilterYear = { viewModel.deleteFilterYear() },
                onClickValueYear = { viewModel.onClickValueYear(it) }
            )
            if (plants.value.isEmpty() && !uiState.value.onFilterYear && !uiState.value.onFilterResult && !uiState.value.onFilterCategory && searchText.value.isBlank()) {
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
    uiState: State<HomeUiState>,
    plantsYear: State<List<String>>,
    onClickValueCategory: (CategoryPlant) -> Unit,
    onClickValueResult: (ResultPlant) -> Unit,
    onClickValueYear: (String) -> Unit,
    deleteFilterCategory: () -> Unit,
    deleteFilterResult: () -> Unit,
    deleteFilterYear: () -> Unit
) {

    val state = rememberScrollState()
    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 2.dp)
            .horizontalScroll(state),
    ) {
        FilterPlant.entries.forEach {
            var expanded by remember { mutableStateOf(false) }
            when (it) {
                FilterPlant.category -> {
                    Box {
                        FilterChip(
                            selected = uiState.value.onFilterCategory,
                            onClick = {
                                expanded = true
                            },
                            label = {
                                Text(text = stringResource(uiState.value.filterCategory?.title ?: it.title))
                            },
                            trailingIcon = {
                                Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                            },
                            leadingIcon = {
                                if (uiState.value.onFilterCategory) {
                                    IconButton(
                                        onClick = { deleteFilterCategory() },
                                        modifier = Modifier.size(16.dp)
                                    ) {
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
                    Box {
                        FilterChip(
                            selected = uiState.value.onFilterResult,
                            onClick = {
                                expanded = true
                            },
                            label = {
                                Text(text = stringResource(uiState.value.filterResult?.text ?: it.title))
                            },
                            trailingIcon = {
                                Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                            },
                            leadingIcon = {
                                if (uiState.value.onFilterResult) {
                                    IconButton(
                                        onClick = { deleteFilterResult() },
                                        modifier = Modifier.size(16.dp)
                                    ) {
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
                    Box {
                        FilterChip(
                            selected = uiState.value.onFilterYear,
                            onClick = {
                                expanded = true
                            },
                            label = {
                                Text(text = uiState.value.filterYear ?: stringResource(it.title))
                            },
                            trailingIcon = {
                                Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                            },
                            leadingIcon = {
                                if (uiState.value.onFilterYear) {
                                    IconButton(
                                        onClick = { deleteFilterYear() },
                                        modifier = Modifier.size(16.dp)
                                    ) {
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
                            plantsYear.value.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it) },
                                    onClick = {
                                        expanded = false
                                        onClickValueYear(it)
                                    })
                            }
                        }
                    }

                }

            }


        }

    }
}

@Composable
fun TopAppBarSearch(
    searchText: State<String>,
    onSearchTextChange: (String) -> Unit,
    onClearSearchText: () -> Unit,
) {


    Column(modifier = Modifier

    ) {
        OutlinedTextField(
            value = searchText.value,
            onValueChange = onSearchTextChange,
            placeholder = {
                Text(text = stringResource(R.string.search_bar_search))
            },
            trailingIcon = {
                IconButton(onClick = { onClearSearchText() }) {
                    Icon(Icons.Outlined.Clear, contentDescription = null)
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
        )
    }
}


