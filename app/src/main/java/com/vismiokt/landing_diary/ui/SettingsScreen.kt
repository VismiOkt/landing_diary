package com.vismiokt.landing_diary.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.Key.Companion.Tab
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.ui.view_model.SettingsViewModel

@Composable
fun SettingsScreen(
    navigateBack: () -> Unit
) {
    val viewModel: SettingsViewModel = viewModel()
  //  val uiState = viewModel.settingsUiState


    Scaffold(
        topBar = {
            TopBar(
                stringResource(R.string.menu_settings),
                alpha = 0f,
                onBackButton = navigateBack
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues = it)) {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = stringResource(R.string.settings_theme))
                    TabRow(selectedTabIndex = 0) {
                        
                    }
                }

            }

        }


    }
}

