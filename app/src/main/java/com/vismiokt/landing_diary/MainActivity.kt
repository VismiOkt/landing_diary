package com.vismiokt.landing_diary

//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vismiokt.landing_diary.data.AppTheme
import com.vismiokt.landing_diary.ui.LandingDiaryApp
import com.vismiokt.landing_diary.ui.theme.Landing_diaryTheme
import com.vismiokt.landing_diary.ui.view_model.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val fs = Firebase.firestore
//        fs.collection("plants").document().set(Plant(1, "tutu"))
//        enableEdgeToEdge()


        setContent {
//            val appTheme = viewModel.settingsUiState.collectAsStateWithLifecycle()
            val appTheme = viewModel.settingsUiState.collectAsState()
            when (appTheme.value.appTheme) {
                AppTheme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                AppTheme.DARK  -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                AppTheme.LIGHT  -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
       //     delegate.applyDayNight()
            Landing_diaryTheme {
                LandingDiaryApp()
            }
        }
    }
}



