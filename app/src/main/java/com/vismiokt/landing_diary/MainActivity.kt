package com.vismiokt.landing_diary

//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vismiokt.landing_diary.ui.LandingDiaryApp
import com.vismiokt.landing_diary.ui.theme.Landing_diaryTheme
import com.vismiokt.landing_diary.ui.view_model.MainViewModel
import com.vismiokt.landing_diary.ui.view_model.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  //  private val viewModel: MainViewModel = hiltViewModel()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val fs = Firebase.firestore
//        fs.collection("plants").document().set(Plant(1, "tutu"))
//        enableEdgeToEdge()
  //      val uiState by viewModel.

        setContent {
            Landing_diaryTheme {
                LandingDiaryApp()
            }
        }
    }
}



