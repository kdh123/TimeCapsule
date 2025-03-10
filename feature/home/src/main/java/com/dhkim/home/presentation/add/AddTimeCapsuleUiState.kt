package com.dhkim.home.presentation.add

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.dhkim.location.domain.model.Place
import com.dhkim.story.domain.model.SharedFriend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
data class AddTimeCapsuleUiState(
    val isLoading: Boolean = false,
    val isCompleted: Boolean = false,
    val date: String = "",
    val content: String = "",
    val imageUrls: List<String> = listOf(),
    val openDate: String = "",
    val lat: String = "",
    val lng: String = "",
    val placeName: String = "",
    val address: String = "",
    val checkLocation: Boolean = false,
    val isShare: Boolean = false,
    val sharedFriends: List<SharedFriend> = listOf(),
    val placeQuery: String = "",
    val placeResult: StateFlow<PagingData<Place>> = MutableStateFlow(PagingData.empty()),
)