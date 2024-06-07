package com.vismiokt.landing_diary

//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.vismiokt.landing_diary.ui.LandingDiaryApp
import com.vismiokt.landing_diary.ui.theme.Landing_diaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val fs = Firebase.firestore
//        fs.collection("plants").document().set(Plant(1, "tutu"))
        enableEdgeToEdge()
        setContent {
            Landing_diaryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LandingDiaryApp()
//                    RequiredPermission()
                }
            }
        }
    }
}



