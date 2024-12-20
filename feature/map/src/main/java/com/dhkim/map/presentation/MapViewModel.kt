package com.dhkim.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dhkim.location.domain.Category
import com.dhkim.location.domain.LocationRepository
import com.dhkim.location.domain.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<MapSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onAction(action: MapAction) {
        when (action) {
            is MapAction.CloseSearch -> {
                closeSearch(action.isPlaceSelected)
            }

            is MapAction.SearchPlacesByCategory -> {
                searchPlacesByCategory(action.category, action.lat, action.lng)
            }

            is MapAction.SearchPlacesByKeyword -> {
                searchPlacesByKeyword(action.query, action.lat, action.lng)
            }

            is MapAction.SelectPlace -> {
                selectPlace(action.place)
            }
        }
    }

    private fun searchPlacesByCategory(category: Category, lat: String, lng: String) {
        viewModelScope.launch {
            locationRepository.getPlaceByCategory(
                category = category,
                lat = lat,
                lng = lng
            ).cachedIn(viewModelScope)
                .collect {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        query = category.type,
                        category = category,
                        places = flowOf(it).stateIn(viewModelScope),
                        selectedPlace = null
                    )
                    _sideEffect.send(MapSideEffect.BottomSheet(isHide = false))
                }
        }
    }

    private fun searchPlacesByKeyword(query: String, lat: String, lng: String) {
        viewModelScope.launch {
            locationRepository.getNearPlaceByKeyword(
                query = query,
                lat = lat,
                lng = lng
            ).cachedIn(viewModelScope)
                .collect {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        query = query,
                        category = Category.entries.first { category -> category.type == query },
                        places = flowOf(it).stateIn(viewModelScope),
                        selectedPlace = null
                    )
                    _sideEffect.send(MapSideEffect.BottomSheet(isHide = false))
                }
        }
    }

    private fun selectPlace(place: Place) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                query = place.name,
                category = Category.None,
                selectedPlace = place,
                places = MutableStateFlow(PagingData.empty())
            )

            _sideEffect.send(MapSideEffect.BottomSheet(isHide = true))
        }
    }

    private fun closeSearch(isPlaceSlected: Boolean) {
        _uiState.value = if (isPlaceSlected) {
            _uiState.value.copy(
                places = MutableStateFlow(PagingData.empty()),
                category = Category.None
            )
        } else {
            _uiState.value.copy(
                query = "",
                places = MutableStateFlow(PagingData.empty()),
                category = Category.None,
                selectedPlace = null
            )
        }

        viewModelScope.launch {
            _sideEffect.send(MapSideEffect.BottomSheet(isHide = true))
        }
    }
}