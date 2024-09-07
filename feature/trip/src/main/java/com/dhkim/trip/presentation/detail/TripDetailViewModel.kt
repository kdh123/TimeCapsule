package com.dhkim.trip.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhkim.trip.domain.TripRepository
import com.dhkim.trip.domain.model.Trip
import com.dhkim.trip.domain.model.TripImage
import com.dhkim.trip.domain.model.toTripType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripDetailViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<TripDetailSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    var tripAllImages = MutableStateFlow<List<TripImage>>(listOf())
        private set

    fun onAction(action: TripDetailAction) {
        when (action) {
            is TripDetailAction.InitTrip -> {
                if (uiState.value.tripDates.isEmpty()) {
                    initTrip(tripId = action.tripId)
                }
            }

            is TripDetailAction.LoadImages -> {
                loadImages(tripId = action.tripId, images = action.images)
            }

            is TripDetailAction.UpdateTrip -> {
                updateTrip(trip = action.trip)
            }

            is TripDetailAction.DeleteTrip -> {
                deleteTrip(tripId = action.tripId)
            }

            is TripDetailAction.SelectDate -> {
                selectDate(selectedIndex = action.selectedIndex)
            }

            is TripDetailAction.DeleteImage -> {
                deleteImage(tripId = action.tripId, imageId = action.imageId)
            }
        }
    }

    private fun deleteImage(tripId: String, imageId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tripAllImages.value = tripAllImages.value.filter { it.id != imageId }
            val updateTrip =
                tripRepository.getTrip(id = tripId).first()?.copy(images = tripAllImages.value)
                    ?: return@launch
            tripRepository.updateTrip(updateTrip)
            val updateImages = _uiState.value.images.filter { it.id != imageId }.toImmutableList()
            _uiState.value = _uiState.value.copy(images = updateImages)
        }
    }

    private fun updateTrip(trip: Trip) {
        viewModelScope.launch(Dispatchers.IO) {
            tripRepository.updateTrip(trip = trip.copy(images = listOf(), videos = listOf()))
            _sideEffect.send(
                TripDetailSideEffect.LoadImages(
                    startDate = trip.startDate,
                    endDate = trip.endDate
                )
            )
        }
    }

    private fun deleteTrip(tripId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tripRepository.deleteTrip(id = tripId)
        }
    }

    private fun selectDate(selectedIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val date = _uiState.value.tripDates[selectedIndex].date
            val strDate = "${date.first}-${date.second}-${date.third}"
            val images = tripAllImages.value.filter { it.date == strDate }.toImmutableList()
            _uiState.value = _uiState.value.copy(selectedIndex = selectedIndex, images = images)
        }
    }

    private fun initTrip(tripId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tripRepository.getTrip(id = tripId)
                .catch { }
                .collect { currentTrip ->
                    currentTrip ?: return@collect

                    val title = StringBuilder()
                    currentTrip.places.forEachIndexed { index, place ->
                        title.append(place)
                        if (index < currentTrip.places.size - 1) {
                            title.append(", ")
                        }
                    }
                    title.append(" 여행")

                    if (currentTrip.images.isEmpty()) {
                        with(currentTrip) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                title = "$title",
                                startDate = startDate,
                                endDate = endDate,
                                type = type.toTripType().desc
                            )
                        }
                        _sideEffect.send(
                            TripDetailSideEffect.LoadImages(
                                startDate = currentTrip.startDate,
                                endDate = currentTrip.endDate
                            )
                        )
                    } else {
                        with(currentTrip) {
                            tripAllImages.value = currentTrip.images
                            val strDate: String = if (_uiState.value.selectedIndex <= 0) {
                                currentTrip.startDate
                            } else {
                                val date =
                                    _uiState.value.tripDates[_uiState.value.selectedIndex].date
                                "${date.first}-${date.second}-${date.third}"
                            }
                            val images = tripAllImages.value.filter { it.date == strDate }
                                .toImmutableList()

                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                title = "$title",
                                type = currentTrip.type.toTripType().desc,
                                startDate = startDate,
                                endDate = endDate,
                                images = images
                            )
                        }
                    }
                }
        }
    }

    private fun loadImages(tripId: String, images: List<TripImage>) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTrip = tripRepository.getTrip(id = tripId).firstOrNull() ?: return@launch

            tripAllImages.value = images
            val title = StringBuilder()
            currentTrip.places.forEachIndexed { index, place ->
                title.append(place)
                if (index < currentTrip.places.size - 1) {
                    title.append(", ")
                }
            }
            title.append(" 여행")

            with(currentTrip) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    title = "$title",
                    type = currentTrip.type.toTripType().desc,
                    startDate = startDate,
                    endDate = endDate,
                    selectedIndex = 0,
                )
            }
            selectDate(0)
            if (tripAllImages.value.isNotEmpty()) {
                tripRepository.updateTrip(currentTrip.copy(images = tripAllImages.value))
            } else {
                tripRepository.updateTrip(currentTrip.copy(images = listOf(TripImage())))
            }
        }
    }
}