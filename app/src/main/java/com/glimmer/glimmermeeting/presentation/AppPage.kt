package com.glimmer.glimmermeeting.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPage() {
    ModalNavigationDrawer(
        drawerContent = {
            SideDrawer()
        }
    ) {
        MainPage()
    }
}

@Composable
fun SideDrawer() {
    Column {
        UserInfo()
        NavigationList()
    }
}

@Composable
fun UserInfo() {

}

@Composable
fun NavigationList() {

}

@Preview(showBackground = true)
@Composable
fun AppPagePreview() {
    GlimmerMeetingTheme {
        AppPage()
    }
}