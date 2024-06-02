package com.vismiokt.landing_diary


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequiredPermission() {
    val state = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    Scaffold {
        when {
            state.status.isGranted -> CameraScreen()
            else -> {
                LaunchedEffect(Unit) {
                    state.launchPermissionRequest()
                }
                Box(
                    Modifier
                    .fillMaxSize()
                ) {
                    Column(Modifier.padding(vertical = 120.dp, horizontal = 16.dp)) {
                        Icon(
                            Icons.Rounded.ThumbUp,
                            contentDescription = null)
                        Spacer(Modifier.height(8.dp))
                        Text("Camera permission required")
                        Spacer(Modifier.height(4.dp))
                        Text("This is required in order for the app to take pictures")
                    }
                    val context = LocalContext.current
                    Button(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(16.dp),
                        onClick = {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }

                            context.startActivity(intent)
                        }) {
                        Text("Go to settings")
                    }
                }
            }
        }
    }
}

@Composable
fun CameraScreen() {
    Text(text = "Start")
}
