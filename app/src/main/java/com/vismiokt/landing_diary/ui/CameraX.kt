package com.vismiokt.landing_diary.ui

import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.vismiokt.landing_diary.R
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
    val toastText = stringResource(R.string.entry_plant_error_photograph)
    Dialog(
        onDismissRequest = { navigateBack() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            LaunchedEffect(lensFacing) {
                val cameraProvider = context.getCameraProvider()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraxSelector,
                    preview,
                    imageCapture
                )
                preview.setSurfaceProvider(previewView.surfaceProvider)
            }

            Card(
                modifier = Modifier.fillMaxSize()
            ) {
                Box {
                    AndroidView(
                        { previewView },
                        modifier = Modifier.fillMaxSize()
                    )
                    Card(
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.alpha(0.5f)
                    ) {
                        IconButton(onClick = { navigateBack() }) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                        }
                    }
                    Button(
                        onClick = {
                            captureImage(
                                imageCapture,
                                context,
                                navigateBack,
                                saveImg = saveImg,
                                errorSaveImg = {
                                    Toast.makeText(
                                        context,
                                        toastText,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )

                        },
                        border = BorderStroke(3.dp, color = Color.Black),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(
                                Alignment.BottomCenter
                            )
                            .padding(30.dp)
                            .size(70.dp)
                    ) {

//                        Text(text = stringResource(R.string.entry_plant_photograph))
                    }
                }
            }
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
    saveImg: (Uri) -> Unit,
    errorSaveImg: () -> Unit
) {
//    val name = FormatDateUseCase().getDateNowForName()
//    val contentValues = ContentValues().apply {
//        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Landing_diary-Image")
//        }
//    }
    try {
        val photoFile = File.createTempFile("captured_image", ".jpg", context.cacheDir)
        //    val photoFile = File(getOutputDirectory(context), FormatDateUseCase().getDateNowForName() + ".jpg")
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
        photoFile.deleteOnExit()
    } catch (ex: Exception) {
        errorSaveImg()
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//private fun getOutputDirectory(context: Context): File {
//    val mediaDir = context.getExternalFilesDir(DIRECTORY_PICTURES)
//    return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
//}



