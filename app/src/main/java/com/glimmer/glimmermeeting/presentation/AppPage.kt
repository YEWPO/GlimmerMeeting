package com.glimmer.glimmermeeting.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glimmer.glimmermeeting.R
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
            .width((LocalConfiguration.current.screenWidthDp * 0.6).dp)
            .background(Color(0xFFFFFFFF))
    ) {
        UserInfo()
        NavigationList()
    }
}

@Preview(showBackground = true)
@Composable
fun SideDrawerPreview() {
    GlimmerMeetingTheme {
        SideDrawer()
    }
}

@Composable
fun UserInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ) {
        Image(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            painter = painterResource(id = R.drawable.jiahua),
            contentDescription = "Drawer User Avatar"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Text(text = "陈佳华", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(text = "电子科技大学", fontSize = 14.sp, color = Color(0xFF808080))
                }
            }
            Image(
                modifier = Modifier
                    .clickable {},
                painter = painterResource(id = R.drawable.qr),
                contentDescription = "Drawer QR"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        NavigationDrawerItem(
            label = { Text(text = "我的个人信息", fontSize = 14.sp) },
            icon = { Image(painter = painterResource(id = R.drawable.person), contentDescription = "Drawer Person")},
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.White
            ),
            selected = false,
            onClick = {}
        )
        NavigationDrawerItem(
            label = { Text(text = "日历", fontSize = 14.sp) },
            icon = { Image(painter = painterResource(id = R.drawable.calendar), contentDescription = "Drawer Calendar") },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.White
            ),
            selected = false,
            onClick = {}
        )
        NavigationDrawerItem(
            label = { Text(text = "会议室", fontSize = 14.sp) },
            icon = { Image(painter = painterResource(id = R.drawable.room), contentDescription = "Drawer Room") },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.White
            ),
            selected = false,
            onClick = {}
        )
        NavigationDrawerItem(
            label = { Text(text = "设置", fontSize = 14.sp) },
            icon = { Image(painter = painterResource(id = R.drawable.setting), contentDescription = "Drawer Setting") },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.White
            ),
            selected = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppPagePreview() {
    GlimmerMeetingTheme {
        AppPage()
    }
}