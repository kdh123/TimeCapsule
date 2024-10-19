package com.dhkim.notification

import com.dhkim.home.domain.model.ReceivedTimeCapsule

data class NotificationUiState(
    val isLoading: Boolean = false,
    val timeCapsules: List<ReceivedTimeCapsule> = listOf()
)