package com.vismiokt.landing_diary

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.vismiokt.landing_diary.ui.LandingDiaryApp
import com.vismiokt.landing_diary.ui.theme.Landing_diaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Landing_diaryTheme {
                LandingDiaryApp()
            }
        }
    }


}



