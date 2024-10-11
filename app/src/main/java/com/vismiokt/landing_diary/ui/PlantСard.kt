package com.vismiokt.landing_diary.ui

import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.ResultPlant
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import com.vismiokt.landing_diary.domain.PlantDetails
import com.vismiokt.landing_diary.ui.view_model.AppViewModelProvider
import com.vismiokt.landing_diary.ui.view_model.PlantCardViewModel
import kotlinx.coroutines.launch
import java.io.InputStream
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
    val imageUriList = uiState.value.imageUriList.collectAsState(listOf())

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
//                        MyZoomableBox {
//                            AsyncImage(
//                                modifier = Modifier
//                                    .graphicsLayer(
//                                        scaleX = scale,
//                                        scaleY = scale,
//                                        translationX = offsetX,
//                                        translationY = offsetY
//                                    ),
//                                model = ImageRequest.Builder(LocalContext.current)
//                                    .data(uiState.value.openImageUri)
//                                    .crossfade(enable = true)
//                                    .build(),
//                                contentDescription = "",
//                                //      contentScale = ContentScale.Fit,
//                            )
//                            IconButton(
//                                onClick = { viewModel.closeImageDialog() }
//                            ) {
//
//                                Card(
//                                    colors = CardDefaults.cardColors(
//                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
//                                    ),
//                                    modifier = Modifier
//                                        .size(width = 25.dp, height = 25.dp)
//                                        .alpha(0.6f)
//                                )
//                                {
//                                    Icon(
//                                        Icons.Outlined.Clear,
//                                        contentDescription = null
//                                    )
//                                }
//
//                            }
//                        }
                        TransformableImage(uriImage = uiState.value.openImageUri) {

                        }


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
        items(selectedImages) { uri ->

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
    uriImage: Uri,
    onBackHandler: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    //   var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val painter =
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(uriImage)
                .apply(block = fun ImageRequest.Builder.() {
                    //    placeholder(R.drawable.placeholder)
                    crossfade(true)
                    error(R.drawable.error)
                })
                .size(1)
                .build()
        )
//    val inputStream = context.contentResolver.openInputStream(uriImage)
//    //  val exif = ExifInterface(inputStream)
//    val ei: ExifInterface? = inputStream?.let { ExifInterface(it) }
//    val orientation: Int =
//        ei?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//            ?: ExifInterface.ORIENTATION_NORMAL

    BoxWithConstraints(
        contentAlignment = Alignment.Center,

        modifier = Modifier
            .clip(RectangleShape)
            .aspectRatio((painter.intrinsicSize.width / painter.intrinsicSize.height))
//            .aspectRatio(if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270 || orientation == ExifInterface.ORIENTATION_TRANSVERSE) (painter.intrinsicSize.width / painter.intrinsicSize.height) else (painter.intrinsicSize.height / painter.intrinsicSize.width))
    ) {

        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)
            //       rotation += rotationChange
            val width = (scale - 1) * constraints.maxWidth
            val height = (scale - 1) * constraints.maxHeight
            val maxX = width / 2
            val maxY = height / 2
            offset = Offset(
                x = (offset.x + scale * offsetChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + scale * offsetChange.y).coerceIn(-maxY, maxY)
            )
        }
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
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
        )
//        AsyncImage(
//            modifier = Modifier
//                .fillMaxWidth()
//                .graphicsLayer(
//                    scaleX = scale,
//                    scaleY = scale,
//                    translationX = offset.x,
//                    translationY = offset.y
//                )
//                .pointerInput(Unit) {
//                    detectTapGestures(
//                        onDoubleTap = {
//                            coroutineScope.launch { state.animateZoomBy(4f) }
//                        }
//                    )
//                }
//                .transformable(state = state)
//                ,
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(uriImage)
//                .crossfade(enable = true)
//                .build(),
//            contentDescription = "",
//            contentScale = ContentScale.Fit,
//        )

    }
}

@Composable
fun ZoomableBox(
    modifier: Modifier = Modifier,
    minScale: Float = 1f,
    maxScale: Float = 5f,
    content: @Composable ZoomableBoxScope.() -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = modifier
            //        .clip(RectangleShape)
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = maxOf(minScale, minOf(scale * zoom, maxScale))
                    val maxX = (size.width * (scale - 1)) / 2
                    val minX = -maxX
                    offsetX = maxOf(minX, minOf(maxX, offsetX + pan.x))
                    val maxY = (size.height * (scale - 1)) / 2
                    val minY = -maxY
                    offsetY = maxOf(minY, minOf(maxY, offsetY + pan.y))
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scale = if (scale > 1f) 1f
                        else 3f
                    }
                )
            }
    ) {
        val scope = ZoomableBoxScopeImpl(scale, offsetX, offsetY)
        scope.content()
    }
}

interface ZoomableBoxScope {
    val scale: Float
    val offsetX: Float
    val offsetY: Float
}

private data class ZoomableBoxScopeImpl(
    override val scale: Float,
    override val offsetX: Float,
    override val offsetY: Float
) : ZoomableBoxScope



