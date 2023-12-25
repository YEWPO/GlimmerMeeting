package com.glimmer.glimmermeeting.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glimmer.glimmermeeting.ui.theme.BlueDeep
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingBookPage(
    onPageStateChanged: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetInfo by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val context = LocalContext.current

        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            ),
            title = { Text(text = "预定会议") },
            navigationIcon = {
                IconButton(onClick = { onPageStateChanged("MainPage") }) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Arrow Back")
                }
            },
            actions = {
                TextButton(
                    onClick = {
                        Toast.makeText(context, "预定成功", Toast.LENGTH_SHORT).show()
                        onPageStateChanged("MainPage")
                    }
                ) {
                    Text(text = "完成", fontSize = 18.sp)
                }
            }
        )
        MeetingBookInfo(
            showBottomSheet = {
                bottomSheetInfo = it
                showBottomSheet = true
            }
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                dragHandle = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }) {
                            Icon(Icons.Outlined.Close, contentDescription = "Date Close")
                        }
                        Text(
                            fontSize = 20.sp,
                            text = when(bottomSheetInfo) {
                                "Date" -> "开始时间"
                                "Duration" -> "会议时长"
                                "MeetingRoom" -> "会议室"
                                else -> ""
                            }
                        )
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Outlined.Check,
                                contentDescription = "Date Check",
                                tint = BlueDeep
                            )
                        }
                    }
                }
            ) {
                when(bottomSheetInfo) {
                    "Date" -> MeetingBookInfoDatePicker()
                    "Duration" -> MeetingBookInfoDurationPicker()
                    "MeetingRoom" -> MeetingBookInfoMeetingRoomPicker()
                }
            }
        }
    }
}

@Composable
fun MeetingBookInfoDatePicker() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "11111")
    }
}

@Composable
fun MeetingBookInfoDurationPicker() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "22222")
    }
}

@Composable
fun MeetingBookInfoMeetingRoomPicker() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "33333")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingBookInfo(
    showBottomSheet: (String) -> Unit
) {
    var meetingTitle by remember { mutableStateOf("陈佳华预定的会议") }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White
            ),
            value = meetingTitle,
            onValueChange = { meetingTitle = it }
        )
        MeetingBookInfoPicker(showBottomSheet = showBottomSheet)
    }
}

@Composable
fun MeetingBookInfoPicker(
    showBottomSheet: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showBottomSheet("Date") }
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "开始时间")
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "11:00")
                Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = "Date Picker")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showBottomSheet("Duration") }
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "会议时长")
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "2小时")
                Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = "Date Picker")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showBottomSheet("MeetingRoom") }
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "会议室")
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "信软楼西306")
                Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = "Date Picker")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeetingBookPagePreview() {
    GlimmerMeetingTheme {
        MeetingBookPage({})
    }
}