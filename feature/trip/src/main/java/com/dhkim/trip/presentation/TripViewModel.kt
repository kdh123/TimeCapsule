package com.dhkim.trip.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.trip.domain.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            tripRepository.getAllTrip()
                .catch { }
                .collect { trips ->
                    val partition = trips.partition { it.isNextTrip }
                    _uiState.value = _uiState.value.copy(
                        nextTrips = partition.first.toImmutableList(),
                        prevTrips = partition.second.toImmutableList(),
                    )
                }
        }
    }
}