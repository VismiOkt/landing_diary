package com.vismiokt.landing_diary.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.vismiokt.landing_diary.R

@Composable
fun AboutApp(
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                stringResource(R.string.about_app_title),
                alpha = 0f,
                onBackButton = navigateBack
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Row {
            Image(painter = painterResource(R.drawable.landing_diary), contentDescription = null)
            Column (modifier = Modifier.padding(it)) {
                Text(text = stringResource(R.string.about_app_name))

            }
        }


    }
}