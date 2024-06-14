package com.vismiokt.landing_diary.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.data.CategoryPlant
import com.vismiokt.landing_diary.navigation.LdNavigation
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LandingDiaryApp() {
  //  val viewModel: LandingDiaryViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                ModalNavigationApp()
            }
        },
    ) {
        LdNavigation(drawerState = drawerState)
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LdTopAppBar(
    @StringRes title: Int,
    drawerState: DrawerState

) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(text = stringResource(id = title)) },
        navigationIcon = { IconButton(onClick = {
            scope.launch {
                drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
        }) {
            Icon(Icons.Outlined.Menu, contentDescription = null)
        } },
        modifier = Modifier.fillMaxWidth()
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: Int,
    onBackButton: () -> Unit
) {
    TopAppBar(
        title = {
            Text(stringResource(title))
        },
        navigationIcon = {
            IconButton(onClick = onBackButton) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }

    )
}

@Composable
fun ModalNavigationApp() {
    Image(painter = painterResource(R.drawable.landing_diary), contentDescription = null, modifier = Modifier
        .size(150.dp)
        .clip(shape = RoundedCornerShape(50)))
    Divider()
    CategoryPlant.entries.forEach {
        NavigationDrawerItem(
            label = { Text(text = stringResource(it.title)) },
            selected = false,
            onClick = { /*TODO*/ }
        )
    }

}