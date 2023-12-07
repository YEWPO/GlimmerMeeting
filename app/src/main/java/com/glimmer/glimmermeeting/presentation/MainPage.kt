package com.glimmer.glimmermeeting.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
    ModalNavigationDrawer(
        drawerContent = {
            SideDrawer()
        }
    ) {
        /* Main Screen */
    }
}

@Composable
fun UserInfo() {

}

@Composable
fun NavigationList() {

}

@Composable
fun SideDrawer() {
    Column {
        UserInfo()
        NavigationList()
    }
}

@Composable
fun FunctionCard() {

}

@Composable
fun MeetingInfoCard() {

}

@Composable
fun MeetingInfoDayCard() {

}

@Composable
fun MyMeetingInfo() {

}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    GlimmerMeetingTheme {
        MainPage()
    }
}