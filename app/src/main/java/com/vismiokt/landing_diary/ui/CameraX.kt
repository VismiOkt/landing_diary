package com.vismiokt.landing_diary.ui

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment.DIRECTORY_PICTURES
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraPreviewScreen(
    navigateBack: () -> Unit,
    saveImg: (Uri) -> Unit

) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    Dialog(
        onDismissRequest = { navigateBack() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface (
            modifier = Modifier.fillMaxSize()
        ){
            LaunchedEffect(lensFacing) {
                val cameraProvider = context.getCameraProvider()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
                preview.setSurfaceProvider(previewView.surfaceProvider)
            }

            Card(modifier = Modifier.fillMaxSize()) {
                Box {
                    AndroidView(
                        { previewView },
                            modifier = Modifier.fillMaxSize()
                    )
                    Button(
                        onClick = {
                            captureImage(
                                imageCapture,
                                context,
                                navigateBack,
                                saveImg = saveImg
                            )

                        },
                        modifier = Modifier
                            .align(
                                Alignment.BottomCenter
                            )
                            .padding(30.dp)
                    ) {
                        Text(text = stringResource(R.string.entry_plant_photograph))
                    }
                }
        }


//        TopBar(title = "", alpha = 0.3f) {
//            navigateBack()
//        }

            }


    }
}


private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
private fun captureImage(
    imageCapture: ImageCapture,
    context: Context,
    navigateBack: () -> Unit,
    saveImg: (Uri) -> Unit
) {
//    val name = FormatDateUseCase().getDateNowForName()
//    val contentValues = ContentValues().apply {
//        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Landing_diary-Image")
//        }
//    }

    val photoFile = File(getOutputDirectory(context), FormatDateUseCase().getDateNowForName() + ".jpg")
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(photoFile)
        .build()
//    val outputOptions = ImageCapture.OutputFileOptions
//        .Builder(
//            context.contentResolver,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            contentValues
//        )
//        .build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                println("Successs")
//                saveImg(outputFileResults.savedUri ?: Uri.EMPTY)
                Uri.fromFile(photoFile).let(saveImg)
                navigateBack()
            }

            override fun onError(exception: ImageCaptureException) {
                println("Failed $exception")
            }

        })

}

@RequiresApi(Build.VERSION_CODES.O)
private fun getOutputDirectory(context: Context): File {
    val mediaDir = context.getExternalFilesDir(DIRECTORY_PICTURES)
    return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
}

