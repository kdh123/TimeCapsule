package com.dhkim.timecapsule.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dhkim.camera.navigation.cameraNavigation
import com.dhkim.timecapsule.R
import com.dhkim.timecapsule.home.BottomPlace
import com.dhkim.timecapsule.home.presentation.navigation.homeNavigation
import com.dhkim.timecapsule.profile.presentation.navigation.profileNavigation
import com.dhkim.timecapsule.search.domain.Place
import com.dhkim.timecapsule.search.presentation.navigation.searchNavigation
import com.dhkim.timecapsule.timecapsule.presentation.navigation.addTimeCapsuleNavigation
import com.dhkim.timecapsule.timecapsule.presentation.navigation.timeCapsuleDetailNavigation
import com.dhkim.timecapsule.timecapsule.presentation.navigation.timeCapsuleNavigation
import com.dhkim.timecapsule.timecapsule.presentation.navigation.timeCapsuleOpenNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val state = rememberStandardBottomSheetState(
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(state)
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.AddTimeCapsule, Screen.TimeCapsule, Screen.Profile)
    val isBottomNavShow = navController
        .currentBackStackEntryAsState()
        .value?.destination?.route in listOf(Screen.Home.route, Screen.TimeCapsule.route, Screen.Profile.route)
    var selectedPlace: Place? by remember {
        mutableStateOf(null)
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomNavShow,
                enter = fadeIn() + slideIn { IntOffset(0, it.height) },
                exit = fadeOut() + slideOut { IntOffset(0, it.height) }
            ) {
                if (selectedPlace == null) {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.Bottom),
                        containerColor = colorResource(id = R.color.white),
                    ) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        items.forEach { screen ->
                            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                            NavigationBarItem(
                                icon = {
                                    if (isSelected) {
                                        Icon(painterResource(id = screen.selected), contentDescription = null, tint = Color.Unspecified)
                                    } else {
                                        Icon(painterResource(id = screen.unSelected), contentDescription = null, tint = Color.Unspecified)
                                    }
                                },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPdding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize(),
            navController = navController,
            startDestination = "home"
        ) {
            homeNavigation(
                scaffoldState = scaffoldState,
                onNavigateToSearch = { lat, lng ->
                    navController.navigate("search/$lat/$lng")
                },
                onSelectPlace = { place ->
                    selectedPlace = place
                },
                onInitSavedState = {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("place", null)
                },
                onNavigateToAdd = { place ->
                    navController.navigate("addTimeCapsule")
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("place", place)
                }
            )
            timeCapsuleDetailNavigation()
            timeCapsuleOpenNavigation()
            timeCapsuleNavigation(
                onNavigateToOpen = { id, isReceived ->
                    navController.navigate("timeCapsuleOpen/$id/${isReceived}")
                },
                onNavigateToDetail = { id, isReceived ->
                    navController.navigate("timeCapsuleDetail/$id/${isReceived}")
                },
                modifier = Modifier
                    .padding(
                        bottom = innerPdding.calculateBottomPadding()
                    )
            )
            addTimeCapsuleNavigation(
                onNavigateToCamera = {
                    navController.navigate("camera")
                },
                onBack = {
                    navController.navigateUp()
                }
            )
            cameraNavigation(
                folderName = context.getString(R.string.app_name),
                onNext = { imageUrl ->
                    navController.navigateUp()
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("imageUrl", imageUrl)
                }
            )
            searchNavigation {
                navController.navigateUp()
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("place", it)
            }
            profileNavigation {
                navController.navigateUp()
            }
        }
    }
}

sealed class Screen(
    val title: String, val selected: Int, val unSelected: Int, val route: String
) {
    data object Home : Screen("홈", R.drawable.ic_home_primary, R.drawable.ic_home_black, "home")
    data object AddTimeCapsule : Screen("추가", R.drawable.ic_add_primary, R.drawable.ic_add_black, "addTimeCapsule")
    data object TimeCapsule : Screen("타임캡슐", R.drawable.ic_time_primary, R.drawable.ic_time_black, "timeCapsule")
    data object Profile : Screen("프로필", R.drawable.ic_profile_primary, R.drawable.ic_profile_black, "profile")
}