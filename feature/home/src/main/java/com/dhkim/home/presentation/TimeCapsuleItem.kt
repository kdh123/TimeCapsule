package com.dhkim.home.presentation

import androidx.compose.runtime.Stable

@Stable
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
    UnopenedTimeCapsule,
    OpenedTimeCapsule,
    InviteFriend,
    Line,
}