package com.glimmer.glimmermeeting.presentation

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainPage() {
    Text(text = "MainPage")
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    Surface {
        MainPage()
    }
}