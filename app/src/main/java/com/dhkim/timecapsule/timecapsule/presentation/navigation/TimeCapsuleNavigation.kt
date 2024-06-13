package com.dhkim.timecapsule.timecapsule.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dhkim.timecapsule.main.Screen
import com.dhkim.timecapsule.search.domain.Place
import com.dhkim.timecapsule.timecapsule.TimeCapsuleScreen
import com.dhkim.timecapsule.timecapsule.presentation.add.AddTimeCapsuleScreen
import com.dhkim.timecapsule.timecapsule.presentation.detail.TimeCapsuleDetailScreen
import com.dhkim.timecapsule.timecapsule.presentation.TimeCapsuleSideEffect
import com.dhkim.timecapsule.timecapsule.presentation.TimeCapsuleViewModel
import com.dhkim.timecapsule.timecapsule.presentation.detail.TimeCapsuleDetailViewModel
import com.dhkim.timecapsule.timecapsule.presentation.detail.TimeCapsuleOpenScreen

fun NavGraphBuilder.timeCapsuleNavigation(
    onNavigateToOpen: (timeCapsuleId: String, isReceived: Boolean) -> Unit,
    onNavigateToDetail: (timeCapsuleId: String, isReceived: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(Screen.TimeCapsule.route) {
        val viewModel = hiltViewModel<TimeCapsuleViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val sideEffect by viewModel.sideEffect.collectAsStateWithLifecycle(TimeCapsuleSideEffect.None)

        TimeCapsuleScreen(
            uiState = uiState,
            sideEffect = sideEffect,
            modifier = modifier,
            shareTimeCapsule = viewModel::shareTimeCapsule,
            openTimeCapsule = viewModel::openTimeCapsule,
            onNavigateToOpen = onNavigateToOpen,
            onNavigateToDetail = onNavigateToDetail
        )
    }
}

fun NavGraphBuilder.timeCapsuleDetailNavigation() {
    composable("timeCapsuleDetail/{id}/{isReceived}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id") ?: ""
        val isReceived = (backStackEntry.arguments?.getString("isReceived") ?: "false").toBoolean()
        val viewModel = hiltViewModel<TimeCapsuleDetailViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        TimeCapsuleDetailScreen(
            timeCapsuleId = id,
            isReceived = isReceived,
            uiState = uiState,
            init = viewModel::init
        )
    }
}

fun NavGraphBuilder.timeCapsuleOpenNavigation() {
    composable("timeCapsuleOpen/{id}/{isReceived}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id") ?: ""
        val isReceived = (backStackEntry.arguments?.getString("isReceived") ?: "false").toBoolean()
        val viewModel = hiltViewModel<TimeCapsuleDetailViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        TimeCapsuleOpenScreen(
            timeCapsuleId = id,
            isReceived = isReceived,
            uiState = uiState,
            init = viewModel::init
        )
    }
}

fun NavGraphBuilder.addTimeCapsuleNavigation(
    onNavigateToCamera: () -> Unit,
    onBack: () -> Unit,
) {
    composable("addTimeCapsule") { backStackEntry ->
        val place = backStackEntry.savedStateHandle.get<Place>("place") ?: Place()
        val imageUrl = backStackEntry.savedStateHandle.get<String>("imageUrl") ?: ""

        AddTimeCapsuleScreen(
            imageUrl = imageUrl,
            place = place,
            onNavigateToCamera = onNavigateToCamera,
            onBack = onBack
        )
    }
}