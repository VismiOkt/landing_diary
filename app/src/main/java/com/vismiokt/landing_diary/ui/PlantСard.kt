package com.vismiokt.landing_diary.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
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
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.ResultPlant
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import kotlinx.coroutines.flow.asFlow
import java.util.Currency
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlantCard(
    navigateToEditPlant: (Int) -> Unit,
    viewModel: PlantCardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit
) {
    val uiState = viewModel.plantUiState

    Scaffold(
        topBar = {
            TopBar(
                uiState.plantDetails.nameVariety,
                actionIcon = Icons.Outlined.Delete,
                onActionIcon = {
                    viewModel.openDeleteDialog()
                },
                alpha = 0f,
                onBackButton = navigateBack
            )

        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToEditPlant(uiState.plantDetails.id) }) {
                Icon(Icons.Outlined.Create, contentDescription = null)
            }


        }
    ) { padding ->
        if (uiState.openDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.closeDeleteDialog() },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deletePlant(uiState.plantDetails)
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
        Card(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(uiState.plantDetails.category.icon),
                            contentDescription = null,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .clip(
                                    CircleShape
                                )
                                .size(70.dp)
                        )
                        Text(
                            text = stringResource(uiState.plantDetails.category.title),
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
                            text = FormatDateUseCase().getDateSet(uiState.plantDetails),
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
                            Text(text = uiState.plantDetails.price)
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
                    Text(text = uiState.plantDetails.placeOfPurchase)
                }

                Text(text = stringResource(R.string.entry_plant_note_input) + ": ")
                Text(text = uiState.plantDetails.note)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor =
                        when (uiState.plantDetails.result) {
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
                        Text(text = stringResource(id = uiState.plantDetails.result.text))
                    }

                }
                ImagePreview(selectedImages = uiState.imageUriList)
                //Use Coil to display the selected image

//                if (uiState.value.imageUriList != Uri.EMPTY) {
//                    val painter = rememberAsyncImagePainter(
//                        ImageRequest
//                            .Builder(LocalContext.current)
//                            .data(data = uiState.value.plantDetails.uriImgList)
//                            .build()
//                    )
//                    AsyncImage(model = uiState.value.plantDetails.uriImgList, contentDescription = null,)
//                    Image(
//                        painter = painter,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .padding(5.dp)
//                            .fillMaxWidth()
//                            .border(6.0.dp, Color.Gray),
//                        contentScale = ContentScale.Crop
//                    )
//                }
//


            }


        }


    }


}

@Composable
fun ImagePreview(
    selectedImages: List<Uri>
) {
    LazyRow {
        items (selectedImages) { uri ->

                AsyncImage(
                    modifier = Modifier
                        .size(250.dp),
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

