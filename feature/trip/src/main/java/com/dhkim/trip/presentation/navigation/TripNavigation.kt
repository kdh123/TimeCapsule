package com.dhkim.trip.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.dhkim.trip.presentation.detail.TripDetailScreen
import com.dhkim.trip.presentation.detail.TripDetailViewModel
import com.dhkim.trip.presentation.imageDetail.TripImageDetailScreen
import com.dhkim.trip.presentation.schedule.TripScheduleScreen
import com.dhkim.trip.presentation.schedule.TripScheduleViewModel
import com.dhkim.trip.presentation.tripHome.TripScreen
import com.dhkim.trip.presentation.tripHome.TripViewModel
import com.dhkim.ui.Popup
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

const val TRIP_MAIN_ROUTE = "tripMain"
const val TRIP_ROUTE = "trip"
const val TRIP_SCHEDULE_ROUTE = "trip_schedule"
const val TRIP_DETAIL_ROUTE = "trip_route"
const val TRIP_IMAGE_DETAIL_ROUTE = "trip_image_detail_route"

fun NavGraphBuilder.tripScreen(
    modifier: Modifier = Modifier,
    onNavigateToSchedule: (String) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToImageDetail: (String) -> Unit,
    showPopup: (Popup) -> Unit,
    onBack: () -> Unit
) {
    navigation(startDestination = TRIP_ROUTE, route = TRIP_MAIN_ROUTE) {
        composable(TRIP_ROUTE) {
            val viewModel = hiltViewModel<TripViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            TripScreen(
                uiState = uiState,
                onAction = remember {
                    viewModel::onAction
                },
                modifier = modifier,
                onNavigateToSchedule = onNavigateToSchedule,
                onNavigateToDetail = onNavigateToDetail,
                showPopup = showPopup
            )
        }

        composable(
            route = "$TRIP_SCHEDULE_ROUTE/{tripId}",
            arguments = listOf(
                navArgument("tripId") {
                    type = StringType
                    defaultValue = ""
                }
            )
        ) {
            var tripId = it.arguments?.getString("tripId") ?: ""
            if (tripId.isBlank()) {
                tripId = ""
            }
            val viewModel = hiltViewModel<TripScheduleViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val sideEffect = remember {
                viewModel.sideEffect
            }

            TripScheduleScreen(
                isEdit = tripId.isNotEmpty(),
                uiState = uiState,
                sideEffect = sideEffect,
                onAction = remember(viewModel) {
                    viewModel::onAction
                },
                onBack = onBack
            )
        }

        composable(
            route = "$TRIP_DETAIL_ROUTE/{tripId}",
            arguments = listOf(
                navArgument("tripId") {
                    type = StringType
                    defaultValue = ""
                }
            )
        ) {
            val tripId = it.arguments?.getString("tripId") ?: ""
            val viewModel = hiltViewModel<TripDetailViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val sideEffect = remember {
                viewModel.sideEffect
            }

            TripDetailScreen(
                tripId = tripId,
                uiState = uiState,
                sideEffect = sideEffect,
                onAction = viewModel::onAction,
                onNavigateToImageDetail = onNavigateToImageDetail,
                onNavigateToSchedule = onNavigateToSchedule,
                onBack = onBack
            )
        }

        composable("${TRIP_IMAGE_DETAIL_ROUTE}/{imageUrl}") {
            val imageUrl = it.arguments?.getString("imageUrl") ?: ""
            val realImageUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8.toString())
            TripImageDetailScreen(imageUrl = realImageUrl)
        }
    }
}

fun NavController.navigateToTripSchedule(tripId: String) {
    navigate("$TRIP_SCHEDULE_ROUTE/$tripId")
}

fun NavController.navigateToTripDetail(tripId: String) {
    navigate("$TRIP_DETAIL_ROUTE/$tripId")
}

fun NavController.navigateToTripImageDetail(imageUrl: String) {
    navigate("$TRIP_IMAGE_DETAIL_ROUTE/$imageUrl")
}