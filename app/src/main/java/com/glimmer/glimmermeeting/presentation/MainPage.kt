package com.glimmer.glimmermeeting.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
    Column {
        CenterAlignedTopAppBar(
            title = { Text(text = "Glimmer Meeting") }
        )
        FunctionCard()
        MyMeetingInfo()
    }
}

@Composable
fun FunctionCard() {

}

@Composable
fun MyMeetingInfo() {

}

@Composable
fun MeetingInfoDayCard() {

}

@Composable
fun MeetingInfoCard() {

}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    GlimmerMeetingTheme {
        MainPage()
    }
}