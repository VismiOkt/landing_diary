package com.vismiokt.landing_diary.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.AppTheme
import com.vismiokt.landing_diary.ui.view_model.SettingsViewModel


@Composable
fun SettingsScreen(
    navigateBack: () -> Unit
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val uiState = viewModel.settingsUiState.collectAsState()


    Scaffold(
        topBar = {
            TopBar(
                stringResource(R.string.menu_settings),
                onBackButton = navigateBack
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = paddingValues)
        ) {
            if(uiState.value.supportDynamicTheme) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row {
                            Text(
                                text = stringResource(R.string.settings_dynamic_theme),
                                modifier = Modifier.weight(0.8f)
                            )
                            Switch(
                                checked = uiState.value.isDynamicTheme,
                                onCheckedChange = { viewModel.onDynamicTheme(it) },
                                modifier = Modifier.weight(0.2f)
                            )
                        }


                    }

                }
            }

            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = stringResource(R.string.settings_theme))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppTheme.entries.forEach { appTheme ->
                            ThemeItem(
                                image = appTheme.image,
                                text = appTheme.text,
                                onClick = { viewModel.selectAppTheme(appTheme) },
                                selected = appTheme == uiState.value.appTheme
                            )
                        }
                    }
                }
            }

        }


    }
}

@Composable
fun ThemeItem(
    @DrawableRes image: Int,
    @StringRes text: Int,
    onClick: () -> Unit,
    selected: Boolean
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {
        Image(painter = painterResource(image), contentDescription = null)
        Text(text = stringResource(text))
        RadioButton(selected = selected, onClick = { onClick() })
    }
}

