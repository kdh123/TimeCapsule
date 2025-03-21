package com.dhkim.location.data.model

import com.dhkim.location.domain.model.Place
import java.math.RoundingMode
import java.text.DecimalFormat

internal data class PlaceDocument(
    val address_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val category_name: String,
    val distance: String,
    val id: String,
    val phone: String,
    val place_name: String,
    val place_url: String,
    val road_address_name: String,
    val x: String,
    val y: String
) {
    fun toPlace(): Place {
        val df = DecimalFormat("#.#").apply {
            roundingMode = RoundingMode.UP
        }
        val distanceResult = if (distance.isNotEmpty()) {
            if (distance.toDouble() < 1000) {
                "${distance}m"
            } else {
                "${df.format(distance.toDouble() / 1000.0)}km"
            }
        } else {
            "알 수 없음"
        }

        return Place(
            id = id,
            name = place_name,
            lat = y,
            lng = x,
            category = category_name,
            distance = distanceResult,
            phone = phone,
            url = place_url,
            address = road_address_name,
        )
    }
}