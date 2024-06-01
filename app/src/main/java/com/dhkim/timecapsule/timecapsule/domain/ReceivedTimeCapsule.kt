package com.dhkim.timecapsule.timecapsule.domain


data class ReceivedTimeCapsule(
    val id: String,
    val date: String,
    val openDate: String,
    val sender: String,
    val lat: String,
    val lng: String,
    val address: List<String>,
    val content: String,
    val checkLocation: Boolean,
    val isOpened: Boolean
)
