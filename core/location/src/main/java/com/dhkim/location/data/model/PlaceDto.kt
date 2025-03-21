package com.dhkim.location.data.model

import com.dhkim.location.domain.model.Places

internal data class PlaceDto(
    val documents: List<PlaceDocument>,
    val meta: PlaceMeta
) {
    fun toPlaces(): Places {
        return Places(
            isEnd = meta.is_end,
            places = documents.map { it.toPlace() }
        )
    }
}