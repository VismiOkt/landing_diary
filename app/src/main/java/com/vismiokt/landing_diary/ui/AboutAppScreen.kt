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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vismiokt.landing_diary.R

@Composable
fun AboutAppScreen(
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                stringResource(R.string.about_app_title),
                onBackButton = navigateBack
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues = it)) {
            Image(
                painter = painterResource(R.drawable.landing_diary),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(
                        RoundedCornerShape(150.dp)
                    )
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.about_app_name))
                Text(text = stringResource(R.string.about_app_version))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.about_app_author))

            }
        }
    }
}