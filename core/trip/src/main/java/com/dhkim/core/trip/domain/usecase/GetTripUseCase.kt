package com.dhkim.core.trip.domain.usecase

import com.dhkim.core.trip.domain.model.Trip
import com.dhkim.core.trip.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTripUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {

    operator fun invoke(tripId: String): Flow<Trip?> {
        return tripRepository.getTrip(id = tripId)
    }
}