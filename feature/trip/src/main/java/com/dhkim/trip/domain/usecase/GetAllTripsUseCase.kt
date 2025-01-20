package com.dhkim.trip.domain.usecase

import com.dhkim.trip.domain.model.Trip
import com.dhkim.trip.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTripsUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {

    operator fun invoke(): Flow<List<Trip>> {
        return tripRepository.getAllTrip()
    }
}