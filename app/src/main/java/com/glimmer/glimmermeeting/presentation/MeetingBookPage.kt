package com.glimmer.glimmermeeting.presentation

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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

    var durationHours by remember { mutableStateOf(0) }
    var durationMinutes by remember { mutableStateOf(30) }
    var selectedDurationHours by remember { mutableStateOf(0) }
    var selectedDurationMinutes by remember { mutableStateOf(30) }

    var meetingRoom by remember { mutableStateOf("信软楼西306") }
    var selectedMeetingRoom by remember { mutableStateOf("信软楼西306") }

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
            durationHours = durationHours,
            durationMinutes = durationMinutes,
            meetingRoom = meetingRoom,
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
                            selectedDurationHours = durationHours
                            selectedDurationMinutes = durationMinutes
                            selectedMeetingRoom = meetingRoom
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
                        IconButton(onClick = {
                            durationHours = selectedDurationHours
                            durationMinutes = selectedDurationMinutes
                            meetingRoom = selectedMeetingRoom
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }) {
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
                    "Duration" -> MeetingBookInfoDurationPicker(
                        durationHours = durationHours,
                        durationMinutes = durationMinutes,
                        onDurationHoursChanged = { selectedDurationHours = it },
                        onDurationMinutesChanged = { selectedDurationMinutes = it }
                    )
                    "MeetingRoom" -> MeetingBookInfoMeetingRoomPicker(
                        meetingRoom = meetingRoom,
                        onMeetingRoomChanged = { selectedMeetingRoom = it }
                    )
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
fun MeetingBookInfoDurationPicker(
    durationHours: Int,
    durationMinutes: Int,
    onDurationHoursChanged: (Int) -> Unit,
    onDurationMinutesChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        InfiniteCircularList(
            width = 70.dp,
            itemHeight = 40.dp,
            items = (0..7).toMutableList(),
            additionalText = "小时",
            initialItem = durationHours,
            textStyle = TextStyle(fontSize = 14.sp),
            textColor = Color.LightGray,
            selectedTextColor = Color.Black,
            onItemSelected = { _, item ->
                onDurationHoursChanged(item)
            }
        )
        InfiniteCircularList(
            width = 70.dp,
            itemHeight = 40.dp,
            items = (0..59).toMutableList(),
            additionalText = "分钟",
            initialItem = durationMinutes,
            textStyle = TextStyle(fontSize = 14.sp),
            textColor = Color.LightGray,
            selectedTextColor = Color.Black,
            onItemSelected = { _, item ->
                onDurationMinutesChanged(item)
            }
        )
    }
}

@Composable
fun MeetingBookInfoMeetingRoomPicker(
    meetingRoom: String,
    onMeetingRoomChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        InfiniteCircularList(
            width = 150.dp,
            itemHeight = 40.dp,
            items = listOf("信软楼西306", "三教401", "二教110", "一教103"),
            additionalText = "",
            initialItem = meetingRoom,
            textStyle = TextStyle(fontSize = 14.sp),
            textColor = Color.LightGray,
            selectedTextColor = Color.Black,
            onItemSelected = { _, item ->
                onMeetingRoomChanged(item)
            }
        )
    }
}

@Composable
fun MeetingBookInfo(
    durationHours: Int,
    durationMinutes: Int,
    meetingRoom: String,
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
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
            ),
            value = meetingTitle,
            onValueChange = { meetingTitle = it }
        )
        MeetingBookInfoPicker(
            durationHours = durationHours,
            durationMinutes = durationMinutes,
            meetingRoom = meetingRoom,
            showBottomSheet = showBottomSheet,
        )
    }
}

@Composable
fun MeetingBookInfoPicker(
    durationHours: Int,
    durationMinutes: Int,
    meetingRoom: String,
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
                Text(text = "12:00")
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
                Text(text = "${durationHours}小时${durationMinutes}分钟")
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
                Text(text = meetingRoom)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> InfiniteCircularList(
    width: Dp,
    itemHeight: Dp,
    numberOfDisplayedItems: Int = 3,
    items: List<T>,
    additionalText: String,
    initialItem: T,
    itemScaleFact: Float = 1.5f,
    textStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    onItemSelected: (index: Int, item: T) -> Unit = { _, _ -> }
) {
    val itemHalfHeight = LocalDensity.current.run { itemHeight.toPx() / 2f }
    val scrollState = rememberLazyListState(0)
    var lastSelectedIndex by remember {
        mutableStateOf(0)
    }
    var itemsState by remember {
        mutableStateOf(items)
    }
    LaunchedEffect(items) {
        var targetIndex = items.indexOf(initialItem) - 1
        targetIndex += ((Int.MAX_VALUE / 2) / items.size) * items.size
        itemsState = items
        lastSelectedIndex = targetIndex
        scrollState.scrollToItem(targetIndex)
    }
    LazyColumn(
        modifier = Modifier
            .width(width)
            .height(itemHeight * numberOfDisplayedItems),
        state = scrollState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = scrollState
        )
    ) {
        items(
            count = Int.MAX_VALUE,
            itemContent = { i ->
                val item = itemsState[i % itemsState.size]
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            val y = coordinates.positionInParent().y - itemHalfHeight
                            val parentHalfHeight =
                                (coordinates.parentCoordinates?.size?.height ?: 0) / 2f
                            val isSelected =
                                (y > parentHalfHeight - itemHalfHeight && y < parentHalfHeight + itemHalfHeight)
                            if (isSelected && lastSelectedIndex != i) {
                                onItemSelected(i % itemsState.size, item)
                                lastSelectedIndex = i
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.toString() + additionalText,
                        style = textStyle,
                        color = if (lastSelectedIndex == i) {
                            selectedTextColor
                        } else {
                            textColor
                        },
                        fontSize = if (lastSelectedIndex == i) {
                            textStyle.fontSize * itemScaleFact
                        } else {
                            textStyle.fontSize
                        }
                    )
                }
            }
        )
    }
}