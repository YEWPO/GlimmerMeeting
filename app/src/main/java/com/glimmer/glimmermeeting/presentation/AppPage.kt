package com.glimmer.glimmermeeting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPage() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideDrawer()
        }
    ) {
        MainPage(onDrawerStageChanged = {
            scope.launch {
                drawerState.apply { if (isClosed) open() else close() }
            }
        })
    }
}

@Composable
fun SideDrawer() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(Color(0xFFFFFFFF))
    ) {
        UserInfo()
        NavigationList()
    }
}

@Composable
fun UserInfo() {
    Text(text = "Hello")
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