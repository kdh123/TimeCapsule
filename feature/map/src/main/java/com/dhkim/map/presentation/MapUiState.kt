package com.dhkim.map.presentation

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.dhkim.location.domain.model.Category
import com.dhkim.location.domain.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
data class MapUiState(
    val isLoading: Boolean = false,
    val isCompleted: Boolean = false,
    val query: String = "",
    val places: StateFlow<PagingData<Place>> = MutableStateFlow(PagingData.empty()),
    val category: Category = Category.None,
    val selectedPlace: Place? = null
)