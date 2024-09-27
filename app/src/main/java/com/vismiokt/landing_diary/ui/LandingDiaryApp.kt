package com.vismiokt.landing_diary.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.vismiokt.landing_diary.R
import com.vismiokt.landing_diary.navigation.LdNavigation
import com.vismiokt.landing_diary.navigation.Screen
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LandingDiaryApp() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                ModalNavigationApp(

                    navigateToAboutApp = {
                        navController.navigate(Screen.AboutAppDestination.route) {
                            launchSingleTop = true
                        }
                        coroutineScope.launch {
                            drawerState.close()
                        }

                    },
                    navigateToHomeScreenAllPlants = {
                        navController.navigate(Screen.HomeDestination.route) {
                            launchSingleTop = true
                        }
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    },
                    navigateToSettings = {
                        navController.navigate(Screen.SettingsDestination.route) {
                            launchSingleTop = true
                        }
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        },
    ) {
        LdNavigation(
            drawerState = drawerState,
            navController = navController
        )
    }


}


@Composable
fun ModalNavigationApp(
    navigateToAboutApp: () -> Unit,
    navigateToHomeScreenAllPlants: () -> Unit,
    navigateToSettings: () -> Unit
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Card(
            shape = RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp, topStart = 20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.landing_diary),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(50))
                )
                Text(
                    text = stringResource(R.string.app_name_ru),
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        HorizontalDivider()
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.menu_all_plants))},
            selected = false,
            onClick = { navigateToHomeScreenAllPlants() }
        )
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.menu_settings))},
            selected = false,
            onClick = { navigateToSettings() }
        )
        NavigationDrawerItem(
            label = {
                Row(modifier = Modifier) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = stringResource(R.string.menu_about_app)
                    )
                    Text(text = stringResource(R.string.menu_about_app),
                        modifier = Modifier.padding(start = 4.dp))
                }
            },
            selected = false,
            onClick = { navigateToAboutApp() }
        )


    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LdTopAppBar(
    @StringRes title: Int,
    onSearch: () -> Unit,
    drawerState: DrawerState

) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(text = stringResource(id = title)) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }) {
                Icon(Icons.Outlined.Menu, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = { onSearch() }) {
                Icon(Icons.Outlined.Search, contentDescription = null)

            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    alpha: Float,
    onBackButton: () -> Unit
) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = onBackButton) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent.copy(alpha = alpha)
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    alpha: Float,
    actionIcon: ImageVector,
    onActionIcon: () -> Unit,
    onBackButton: () -> Unit
) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = onBackButton) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onActionIcon) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent.copy(alpha = alpha)
        ),
    )
}

