package com.vismiokt.landing_diary.ui

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import kotlinx.coroutines.flow.update

data class UiState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val openDialogCalendar: Boolean = false,
//    val datePickerState: DatePickerState,
//    val

)

//fun closeDatePickerDialog() {
//    _HomeUiState.update {
//        it.copy(openDialogCalendar = false)
//    }
//}
//
//fun openDatePickerDialog() {
//    _HomeUiState.update {
//        it.copy(openDialogCalendar = true)
//    }
//}


//val date = remember {
//    mutableStateOf("")
//}
//if (uiState.openDialogCalendar) {
//    val datePickerState = rememberDatePickerState()
//    val confirmEnabled = remember {
//        derivedStateOf { datePickerState.selectedDateMillis != null }
//    }
//    DatePickerDialog(
//        onDismissRequest = {
//            viewModel.closeDatePickerDialog()
//        },
//        confirmButton = {
//            TextButton(
//                onClick = {
//                    viewModel.closeDatePickerDialog()
//                    val fduc = FormatDateUseCase()
//                    date.value = fduc.invoke(datePickerState.selectedDateMillis ?: 0)
//                },
//                enabled = confirmEnabled.value
//            ) {
//                Text("OK")
//            }
//        },
//        dismissButton = {
//            TextButton(
//                onClick = {
//                    viewModel.closeDatePickerDialog()
//                }
//            ) {
//                Text("Cancel")
//            }
//        }
//    ) {
//        DatePicker(state = datePickerState)
//    }
//}