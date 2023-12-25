package com.glimmer.glimmermeeting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.glimmer.glimmermeeting.presentation.AccessPage
import com.glimmer.glimmermeeting.presentation.AppPage
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var pageState by remember { mutableStateOf("AccessPage") }

            GlimmerMeetingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (pageState) {
                        "AccessPage" -> AccessPage(onPageStateChanged = { pageState = it })
                        "AppPage" -> AppPage(onPageStateChanged = { pageState = it })
                    }
                }
            }
        }
    }
}