package com.vismiokt.landing_diary.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.vismiokt.landing_diary.R

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequiredPermission(
    navigateBack: () -> Unit,
 //   saveImg: (Uri) -> Unit
) {
    val state = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    Scaffold {
        when {
            state.status.isGranted -> CameraPreviewScreen(navigateBack)
            else -> {
                TopBar(title = "", alpha = 0f) {
                    navigateBack()
                }
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
                        Text(stringResource(R.string.required_permission_camera_permission_required))
                        Spacer(Modifier.height(4.dp))
                        Text(stringResource(R.string.required_permission_take_pictures))
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
                        Text(stringResource(R.string.required_permission_go_to_settings))
                    }
                }
            }
        }
    }
}

