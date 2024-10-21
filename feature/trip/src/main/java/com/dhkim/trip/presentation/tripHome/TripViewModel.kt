package com.dhkim.trip.presentation.tripHome

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.common.onetimeRestartableStateIn
import com.dhkim.trip.domain.TripRepository
import com.dhkim.trip.domain.model.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    val uiState = tripRepository.getAllTrip()
        .flowOn(Dispatchers.IO)
        .map { it.toUiState() }
        .onetimeRestartableStateIn(
            scope = viewModelScope,
            initialValue = TripUiState(),
            isOnetime = false
        )

    fun onAction(action: TripAction) {
        when (action) {
            is TripAction.DeleteTrip -> {
                deleteTrip(tripId = action.tripId)
            }
        }
    }

    private fun deleteTrip(tripId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tripRepository.deleteTrip(id = tripId)
        }
    }
}

@Stable
data class TripItem(
    val id: String,
    val data: Any
)

fun List<Trip>.toUiState(): TripUiState {
    val partition = partition { it.isNextTrip }
    val nextTrips = partition.first
    val prevTrips = partition.second
    val items = mutableListOf<TripItem>()
    if (nextTrips.isNotEmpty()) {
        items.add(TripItem(id = "${UUID.randomUUID()}", data = "다음 여행"))
        items.addAll(nextTrips.map { TripItem(id = it.id, data = it) })
    }
    if (prevTrips.isNotEmpty()) {
        items.add(TripItem(id = "${UUID.randomUUID()}", data = "지난 여행"))
        items.addAll(prevTrips.map { TripItem(id = it.id, data = it) })
    }

    return TripUiState(
        isLoading = false,
        trips = items.toImmutableList()
    )
}