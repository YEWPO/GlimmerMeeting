package com.glimmer.glimmermeeting.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme

@Composable
fun MainPage(onDrawerStageChanged: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MainPageTopBar(onDrawerStageChanged = onDrawerStageChanged)
        FunctionCard()
        MyMeetingInfo()
    }
}

@Composable
fun MainPageTopBar(onDrawerStageChanged: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Button(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                onClick = { onDrawerStageChanged() }
            ) { }
            Column {
                Text(text = "Name", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = "Apartment", color = Color(0xFF808080))
            }
        }
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
        MainPage({})
    }
}