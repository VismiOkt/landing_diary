package com.vismiokt.landing_diary.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.Plant

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  //  plants: List<Plant>,
    //   viewModel: LandingDiaryViewModel,
    drawerState: DrawerState,
    navigateToPlantEntry: () -> Unit,
    navigateToPlantDetails: (Int) -> Unit

) {
    val viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState by viewModel.homeUiState.collectAsState()


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
        if (uiState.plants.isEmpty()) {
            Text(
                text = stringResource(R.string.app_no_plant_description),
                textAlign = TextAlign.Center,
              //  style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(padding)
                    .padding(start = 16.dp, end = 16.dp),
            )
        } else {
            LazyColumn(modifier = Modifier
                .padding(
                    start = padding.calculateStartPadding(LocalLayoutDirection.current),
                    end = padding.calculateEndPadding(LocalLayoutDirection.current),
                    top = padding.calculateTopPadding()
                )
                .fillMaxWidth()
                .padding(8.dp)) {
                items(uiState.plants, key = { it.id }) { plant ->
                    PlantCardMin(
                        plant = plant,
                        onPlantClick = { navigateToPlantDetails(it) }
                    )
                }
            }
        }

    }


}

