package com.vismiokt.landing_diary.ui

import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.CategoryPlant
import com.vismiokt.landing_diary.data.Plant
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LandingDiaryApp() {
    val viewModel: LandingDiaryViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                ModalNavigationApp()
            }
        },
    ) {
        Scaffold(
            topBar = { LdTopAppBar(R.string.app_name_ru) {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                }


            }
        ) { contentPadding ->
            HomeScreen(viewModel.plants, viewModel)
        }
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    plants: List<Plant>,
    viewModel: LandingDiaryViewModel,

) {
    val uiState by viewModel.uiState.collectAsState()

    val date = remember {
        mutableStateOf("")
    }
    if (uiState.openDialogCalendar) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                viewModel.closeDatePickerDialog()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.closeDatePickerDialog()
                        val fduc = FormatDateUseCase()
                        date.value = fduc.invoke(datePickerState.selectedDateMillis?: 0)
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.closeDatePickerDialog()
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
Column {
    Button(onClick = { viewModel.openDatePickerDialog() }, modifier = Modifier.padding(top = 100.dp)) {

    }
    Text(text = date.value)
}

    LazyColumn (modifier = Modifier.padding(top = 200.dp)) {
        items(plants, key = { it.id }) {
            PlantCardMin(it)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LdTopAppBar(
    @StringRes title: Int,
    onPressedMenu: () -> Unit

) {
    TopAppBar(
        title = { Text(text = stringResource(id = title)) },
        navigationIcon = { IconButton(onClick = onPressedMenu) {
            Icon(Icons.Outlined.Menu, contentDescription = null)
        } },
        modifier = Modifier.fillMaxWidth()
        )
}

@Composable
fun ModalNavigationApp() {
    Image(painter = painterResource(R.drawable.landing_diary), contentDescription = null, modifier = Modifier.size(150.dp).clip(shape = RoundedCornerShape(50)))
    Divider()
    CategoryPlant.entries.forEach {
        NavigationDrawerItem(
            label = { Text(text = stringResource(it.title)) },
            selected = false,
            onClick = { /*TODO*/ }
        )
    }

}