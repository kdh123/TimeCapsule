package com.dhkim.timecapsule.timecapsule.presentation

import androidx.compose.runtime.Stable

@Stable
data class TimeCapsuleUiState(
    val isLoading: Boolean = true,
    val isNothing: Boolean = true,
    val timeCapsules: List<TimeCapsuleItem> = listOf()
)

data class TimeCapsuleItem(
    val id: Int = 0,
    val type: TimeCapsuleType = TimeCapsuleType.NoneTimeCapsule,
    val data: Any? = null
)

enum class TimeCapsuleType {
    Title,
    SubTitle,
    NoneTimeCapsule,
    OpenableTimeCapsule,
    UnopenedMyTimeCapsule,
    UnopenedReceivedTimeCapsule,
    OpenedTimeCapsule,
    InviteFriend,
    Line,
}
