package com.dhkim.splash

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dhkim.main.MainActivity
import com.dhkim.onboarding.R
import com.dhkim.ui.DefaultBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    val context = LocalContext.current

    LaunchedEffect(true) {
        delay(500L)
        Intent(context, MainActivity::class.java).run {
            context.startActivity(this)
        }
        (context as? Activity)?.finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DefaultBackground(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_send_time_white),
                contentDescription = null,
                modifier = Modifier
                    .padding(70.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}