package com.vismiokt.landing_diary.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.ResultPlant
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import com.vismiokt.landing_diary.domain.PlantDetails
import com.vismiokt.landing_diary.ui.view_model.AppViewModelProvider
import com.vismiokt.landing_diary.ui.view_model.PlantCardViewModel
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlantCard(
    navigateToEditPlant: (Int) -> Unit,
    viewModel: PlantCardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit
) {
    val uiState = viewModel.plantUiState.collectAsState()
    val plantDetails = uiState.value.plantDetails.collectAsState(PlantDetails())
    val imageUriList =  uiState.value.imageUriList.collectAsState(listOf())

    Scaffold(
        topBar = {
            TopBar(
                plantDetails.value.nameVariety,
                actionIcon = Icons.Outlined.Delete,
                onActionIcon = {
                    viewModel.openDeleteDialog()
                },
                alpha = 0f,
                onBackButton = navigateBack
            )

        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToEditPlant(plantDetails.value.id) }) {
                Icon(Icons.Outlined.Create, contentDescription = null)
            }


        }
    ) { padding ->
        if (uiState.value.openDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.closeDeleteDialog() },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deletePlant(plantDetails.value)
                        navigateBack()
                    }) {
                        Text(text = stringResource(R.string.plant_card_delete_dialog_ok))
                    }

                },
                text = {
                    Text(text = stringResource(R.string.plant_card_delete_dialog_text))
                },
                dismissButton = {
                    Button(onClick = { viewModel.closeDeleteDialog() }) {
                        Text(text = stringResource(R.string.plant_card_delete_dialog_cancel))
                    }
                }
            )
            
        }
        val state = rememberScrollState()
        Card(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
                .fillMaxWidth()
                .verticalScroll(state)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(plantDetails.value.category.icon),
                            contentDescription = null,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .clip(
                                    CircleShape
                                )
                                .size(70.dp)
                        )
                        Text(
                            text = stringResource(plantDetails.value.category.title),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                //        .size(100.dp)
                                .padding(top = 8.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(R.string.plant_card_time_plant_seeds_input))
                        Text(
                            text = FormatDateUseCase().getDateSet(plantDetails.value),
                            textAlign = TextAlign.Center,
                            //  style = MaterialTheme.typography.titleLarge,
//                        modifier = Modifier
//                            .padding(start = 16.dp, end = 16.dp),
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row {
                            Text(text = stringResource(R.string.entry_plant_price_input))
                            Text(text = ": ")
                        }
                        Row {
                            Text(text = plantDetails.value.price)
                            Text(
                                text = " " + Currency.getInstance(Locale.getDefault()).symbol,
                                //  textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                HorizontalDivider()
                Row {
                    Text(text = stringResource(R.string.entry_plant_place_of_purchase_input) + ": ")
                    Text(text = plantDetails.value.placeOfPurchase)
                }

                Text(text = stringResource(R.string.entry_plant_note_input) + ": ")
                Text(text = plantDetails.value.note)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor =
                        when (plantDetails.value.result) {
                            ResultPlant.positive -> MaterialTheme.colorScheme.tertiaryContainer
                            ResultPlant.negative -> MaterialTheme.colorScheme.error
                            ResultPlant.neutral -> MaterialTheme.colorScheme.outline
                            ResultPlant.unknown -> MaterialTheme.colorScheme.primaryContainer
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        Text(text = stringResource(R.string.entry_plant_result_input) + ": ")
                        Text(text = stringResource(id = plantDetails.value.result.text))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                ImagePreview(
                    selectedImages = imageUriList.value,
                    onImage = { viewModel.openImageDialog(it) }
                    )
                if (uiState.value.openImageDialog) {
                    Dialog(onDismissRequest = { viewModel.closeImageDialog() }) {
                        TransformableImage(uriImage = uiState.value.openImageUri)

                    }
                }

            }
        }
    }
}

@Composable
fun ImagePreview(
    selectedImages: List<Uri>,
    onImage: (Uri) -> Unit
) {
    LazyRow {
        items (selectedImages) { uri ->

                AsyncImage(
                    modifier = Modifier
                        .size(250.dp)
                        .padding(end = 8.dp)
                        .clickable { onImage(uri) },
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uri)
                        .crossfade(enable = true)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
        }
    }
}

@Composable
private fun TransformableImage(
    uriImage: Uri
) {
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val coroutineScope = rememberCoroutineScope()
    BoxWithConstraints (modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.6f)
    //    .aspectRatio(16f, true)
    ) {
        
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            scale =  (scale * zoomChange).coerceIn(1f, 5f)
            rotation += rotationChange
            val width = (scale - 1) * constraints.maxWidth
            val height = (scale - 1) * constraints.maxHeight
            val maxX = width / 2
            val maxY = height / 2
            offset = Offset(
                x = (offset.x + scale * offsetChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + scale * offsetChange.y).coerceIn(-maxY, maxY)
            )
        }
        AsyncImage(
            modifier = Modifier
                //   .aspectRatio(1f, true)
                //   .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }

                //     .fillMaxWidth()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    //           rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            coroutineScope.launch { state.animateZoomBy(4f) }
                        }
                    )
                }
                .transformable(state = state)
                ,
            //      .weight(0.8f),
            //   .size(550.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(uriImage)
                .crossfade(enable = true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Fit,
        )

    }

//    Box(
//        Modifier
//            .graphicsLayer(
//                scaleX = scale,
//                scaleY = scale,
//     //           rotationZ = rotation,
//                translationX = offset.x,
//                translationY = offset.y
//            )
//            .transformable(state = state)
////            .background(Color.Blue)
//     //       .fillMaxSize()
//    ) {

//    }
//    Box(
//        Modifier
//            .size(200.dp)
//            .clipToBounds()
//            .background(Color.LightGray)
//    ) {
//        // set up all transformation states
//        var scale by remember { mutableStateOf(1f) }
//        var rotation by remember { mutableStateOf(0f) }
//        var offset by remember { mutableStateOf(Offset.Zero) }
//        val coroutineScope = rememberCoroutineScope()
//        // let's create a modifier state to specify how to update our UI state defined above
//        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
//            // note: scale goes by factor, not an absolute difference, so we need to multiply it
//            // for this example, we don't allow downscaling, so cap it to 1f
//            scale = max(scale * zoomChange, 1f)
//            rotation += rotationChange
//            offset += offsetChange
//        }
//        Box(
//            Modifier
//                // apply pan offset state as a layout transformation before other modifiers
//                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
//                // add transformable to listen to multitouch transformation events after offset
//                .transformable(state = state)
//                // optional for example: add double click to zoom
//                .pointerInput(Unit) {
//                    detectTapGestures(
//                        onDoubleTap = {
//                            coroutineScope.launch { state.animateZoomBy(4f) }
//                        }
//                    )
//                }
//                .fillMaxSize()
//                .border(1.dp, Color.Green),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                "\uD83C\uDF55",
//                fontSize = 32.sp,
//                // apply other transformations like rotation and zoom on the pizza slice emoji
//                modifier = Modifier.graphicsLayer {
//                    scaleX = scale
//                    scaleY = scale
//                    rotationZ = rotation
//                }
//            )
//        }
//    }
}



