package com.vismiokt.landing_diary

//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
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
//        val fs = Firebase.firestore
//        fs.collection("plants").document().set(Plant(1, "tutu"))
//        enableEdgeToEdge()

        setContent {
            Landing_diaryTheme {
                LandingDiaryApp()
            }
        }
    }
}



