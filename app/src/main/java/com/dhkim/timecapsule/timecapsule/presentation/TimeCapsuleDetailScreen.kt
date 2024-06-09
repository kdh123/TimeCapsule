package com.dhkim.timecapsule.timecapsule.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TimeCapsuleDetailScreen(
    id: String,
    isReceived: Boolean
) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Detail Screen! $id, $isReceived")
    }
}