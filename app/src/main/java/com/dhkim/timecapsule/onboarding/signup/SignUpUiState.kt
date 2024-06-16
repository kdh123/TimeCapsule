package com.dhkim.timecapsule.onboarding.signup

data class SignUpUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val profileImage: Int = 0,
    val errorMessage: String = ""
)