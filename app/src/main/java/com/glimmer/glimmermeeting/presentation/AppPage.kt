package com.glimmer.glimmermeeting.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glimmer.glimmermeeting.R
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme
import kotlinx.coroutines.launch

@Composable
fun AppPage(
    onPageStateChanged: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var pageState by remember { mutableStateOf("MainPage") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideDrawer(onPageStateChanged = onPageStateChanged)
        }
    ) {
        when(pageState) {
            "MainPage" -> MainPage(
                onDrawerStageChanged = {
                    scope.launch {
                        drawerState.apply { if (isClosed) open() else close() }
                    }
                },
                onPageStateChanged = { pageState = it }
            )
            "MeetingDetailedPage" -> MeetingDetailedPage(onPageStateChanged = { pageState = it })
            "MeetingBookPage" -> MeetingBookPage(onPageStateChanged = { pageState = it })
        }
    }
}

@Composable
fun SideDrawer(
    onPageStateChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width((LocalConfiguration.current.screenWidthDp * 0.6).dp)
            .background(Color(0xFFFFFFFF))
    ) {
        UserInfo()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            NavigationList()
            VersionInfo(onPageStateChanged = onPageStateChanged)
        }
    }
}

@Composable
fun VersionInfo(
    onPageStateChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            modifier = Modifier
                .size(50.dp),
            onClick = { onPageStateChanged("AccessPage") },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(Icons.Outlined.ExitToApp, contentDescription = "Logout")
        }
        Text(text = "Glimmer Meeting Version 0.0.1", fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SideDrawerPreview() {
    GlimmerMeetingTheme {
        SideDrawer({})
    }
}

@Composable
fun UserInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .paint(
                painter = painterResource(id = R.drawable.person_background),
                contentScale = ContentScale.FillBounds,
                alpha = 0.2f
            )
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
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

@Composable
fun NavigationList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
        AppPage({})
    }
}