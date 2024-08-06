package com.vismiokt.landing_diary.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vismiokt.landing_diary.R

//@Composable
//fun PhotoSelectorView(maxSelectionCount: Int = 1) {
//    var selectedImages by remember {
//        mutableStateOf<List<Uri?>>(emptyList())
//    }
//
//    val buttonText = if (maxSelectionCount > 1) {
//        "Select up to $maxSelectionCount photos"
//    } else {
//        "Select a photo"
//    }
//
//    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = { uri -> selectedImages = listOf(uri) }
//    )
//
//    // I will start this off by saying that I am still learning Android development:
//    // We are tricking the multiple photos picker here which is probably not the best way,
//    // if you know of a better way to implement this feature drop a comment and let me know
//    // how to improve this design
//    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = if (maxSelectionCount > 1) {
//            maxSelectionCount
//        } else {
//            2
//        }),
//        onResult = { uris -> selectedImages = uris }
//    )
//
//    fun launchPhotoPicker() {
//        if (maxSelectionCount > 1) {
//            multiplePhotoPickerLauncher.launch(
//                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//            )
//        } else {
//            singlePhotoPickerLauncher.launch(
//                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//            )
//        }
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(onClick = {
//            launchPhotoPicker()
//        }) {
//            Text(buttonText)
//        }
//
//        ImageLayoutView(selectedImages = selectedImages)
//    }
//}



@Composable
fun PhotoPickerScreen(
    saveImg: (Uri) -> Unit
) {
    //The URI of the photo that the user has picked
    var photoUri: Uri? by remember { mutableStateOf(null) }

    //The launcher we will use for the PickVisualMedia contract.
    //When .launch()ed, this will display the photo picker.
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        //When the user has selected a photo, its URI is returned here
        photoUri = uri
    }


    Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                //On button press, launch the photo picker
                launcher.launch(PickVisualMediaRequest(
                    //Here we request only photos. Change this to .ImageAndVideo if you want videos too.
                    //Or use .VideoOnly if you only want videos.
                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                ))
            },
        ) {
            Text(stringResource(R.string.entry_plant_add_image_from_gallery))
        }

        if (photoUri != null) {
            //Use Coil to display the selected image
            saveImg(photoUri ?: Uri.EMPTY)
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(data = photoUri)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .border(6.0.dp, Color.Gray),
                contentScale = ContentScale.Crop
            )

        }
    }
}
