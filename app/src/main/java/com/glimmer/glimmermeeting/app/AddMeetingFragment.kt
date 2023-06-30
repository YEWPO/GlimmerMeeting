package com.glimmer.glimmermeeting.app

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Message.obtain
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.glimmer.glimmermeeting.MainActivity
import com.glimmer.glimmermeeting.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Types
import com.squareup.moshi.adapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate

class AddMeetingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_meeting_layout, container, false).apply {
            findViewById<ComposeView>(R.id.addMeetingComposeView).apply {
                setContent {
                    AddMeetingScreen(
                        onNavigate = {
                            activity?.supportFragmentManager?.findFragmentById(R.id.main_fragment_container)
                            ?.findNavController()?.navigate(it)
                        },
                        token = activity?.getPreferences(Context.MODE_PRIVATE)?.getString("loginToken", null)?:"null",
                        context = context
                    )
                }
            }
        }
    }
}

@JsonClass(generateAdapter = true)
data class MeetingRoomInfo(
    var id: Int,
    var name: String,
    var info: String,
    var location: String,
    var related_booking: String?,
    var stat_id: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMeetingScreen(
    onNavigate: (Int) -> Unit,
    token: String,
    context: Context
) {
    var roomSelection by remember {
        mutableStateOf(MeetingRoomInfo(
            id = -1,
            name = "请选择会议室",
            info = "",
            location = "",
            related_booking = null,
            stat_id = 0
        ))
    }
    var meetingRoomList by remember {
        mutableStateOf(mutableListOf<MeetingRoomInfo>())
    }

    val getMeetingRoomHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val type = Types.newParameterizedType(MutableList::class.java, MeetingRoomInfo::class.java)
            val meetingRoomInfoAdapter = MainActivity().moshi.adapter<MutableList<MeetingRoomInfo>>(type)

            if (msg.data.getBoolean("state")) {
                meetingRoomList = msg.data.getString("json")?.let { meetingRoomInfoAdapter.fromJson(it) }!!
            } else {
                SettingFragment().logout(true)
            }
        }
    }

    LaunchedEffect(Unit) {
        val getMeetingRoomRequest = Request.Builder()
            .url("http://api.mcyou.cc:2023/admin/rooms?token=$token")
            .get()
            .build()

        MainActivity().okHttpClient.newCall(getMeetingRoomRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GetMeetingRoom", e.printStackTrace().toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val responseState = response.isSuccessful
                val responseJson = response.body!!.string()

                val responseMessage = obtain()
                val responseBundle = Bundle()
                responseBundle.putBoolean("state", responseState)
                responseBundle.putString("json", responseJson)
                responseMessage.data = responseBundle

                getMeetingRoomHandler.sendMessage(responseMessage)
            }
        })
    }

    var dateYearSelection by remember {
        mutableIntStateOf(LocalDate.now().year)
    }
    var dateMonthSelection by remember {
        mutableIntStateOf(LocalDate.now().month.value)
    }
    var dateDaySelection by remember {
        mutableIntStateOf(LocalDate.now().dayOfMonth)
    }

    var meetingTitleInput by remember {
        mutableStateOf("我的会议")
    }

    var startTime by remember {
        mutableFloatStateOf(0f)
    }
    var endTime by remember {
        mutableFloatStateOf(0f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "预定会议",
                        color = Color.Black,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigate(R.id.appFragment)
                        }
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = colorResource(id = R.color.glimmer)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Divider()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                MeetingRoomSelect(
                    onRoomSelectionChanged = {room ->
                        roomSelection = room
                    },
                    meetingRooms = meetingRoomList
                )
                MeetTitleInput(onMeetingTitleChanged = {title ->
                    meetingTitleInput = title
                })
                DateSelect(
                    onDateYearSelectionChanged = {year ->
                        dateYearSelection = year
                    },
                    onDateMonthSelectionChanged = {month ->
                        dateMonthSelection = month
                    },
                    onDateDaySelectionChanged = {day ->
                        dateDaySelection = day
                    }
                )
                TimeSelection(
                    onStartTimeChanged = {time ->
                        startTime = time
                    },
                    onEndTimeChanged = {time ->
                        endTime = time
                    }
                )
                BookButton(
                    meetingRoom = roomSelection,
                    meetingTitle = meetingTitleInput,
                    meetingDate = "$dateYearSelection-" + "%02d".format(dateMonthSelection) + "-" + "%02d".format(dateDaySelection),
                    meetingTime = HomeFragment.DurationData(
                        beginhour = startTime.toInt() / 60,
                        beginminute = startTime.toInt() - startTime.toInt() / 60 * 60,
                        endhour = endTime.toInt() / 60,
                        endminute = endTime.toInt() - endTime.toInt() / 60 * 60
                    ),
                    onNavigate = onNavigate,
                    token = token,
                    context = context
                )
            }
        }
    }
}

@JsonClass(generateAdapter = true)
data class BookInfo(
    val duration: HomeFragment.DurationData,
    val day: String,
    val theme: String,
    val roomid: Int,
    val attendees: List<Int>,
    val token: String
)

@Composable
fun BookButton(
    meetingRoom: MeetingRoomInfo,
    meetingTitle: String,
    meetingDate: String,
    meetingTime: HomeFragment.DurationData,
    onNavigate: (Int) -> Unit,
    token: String,
    context: Context
) {
    val bookHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.data.getBoolean("state")) {
                onNavigate(R.id.appFragment)
                Toast.makeText(context, "预定成功", Toast.LENGTH_SHORT).show()
            } else {
                Log.i("bookmeetinghandlerjson", msg.data.getString("json", null))
                Toast.makeText(context, "预定失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 5.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    val bookInfoAdapter = MainActivity().moshi.adapter(BookInfo::class.java)
                    val requestJson = bookInfoAdapter.toJson(BookInfo(
                        duration = meetingTime,
                        theme = meetingTitle,
                        day = meetingDate,
                        token = token,
                        attendees = listOf(0),
                        roomid = meetingRoom.id
                    ))

                    Log.i("json", requestJson)

                    val request = Request.Builder()
                        .url("http://api.mcyou.cc:2023/book/meeting")
                        .post(requestJson.toRequestBody("application/json; charset=utf-8".toMediaType()))
                        .build()

                    MainActivity().okHttpClient.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e("BookMeeting", e.printStackTrace().toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseState = response.isSuccessful
                            val responseJson = response.body!!.string()

                            val responseMessage = obtain()
                            val responseBundle = Bundle()
                            responseBundle.putBoolean("state", responseState)
                            responseBundle.putString("json", responseJson)
                            responseMessage.data = responseBundle
                            bookHandler.sendMessage(responseMessage)
                        }
                    })
                },
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.light_grey)
                )
            ) {
                Text(
                    text = "预定",
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.glimmer)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelection(
    onStartTimeChanged: (Float) -> Unit,
    onEndTimeChanged:(Float) -> Unit
) {
    var ranges by remember {
        mutableStateOf(480f..1440f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 5.dp
            )
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                )
                Text(
                    text = "时间",
                    fontSize = 22.sp
                )
            }
            RangeSlider(
                value = ranges,
                valueRange = 480f..1440f,
                onValueChange = {
                    ranges = it
                    onStartTimeChanged(ranges.start)
                    onEndTimeChanged(ranges.endInclusive)
                },
                steps = 959,
                colors = SliderDefaults.colors(
                    thumbColor = colorResource(id = R.color.glimmer),
                    inactiveTrackColor = Color.Transparent,
                    activeTrackColor = colorResource(id = R.color.glimmer)
                )
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(50.dp)
            ) {
                val startHour = ranges.start.toInt() / 60
                val startMinute = ranges.start.toInt() - startHour * 60
                val endHour = ranges.endInclusive.toInt() / 60
                val endMinute = ranges.endInclusive.toInt() - endHour * 60

                Text(
                    text = "开始时间: $startHour:" + "%02d".format(startMinute),
                    fontSize = 20.sp
                )
                Text(
                    text = "结束时间: $endHour:" + "%02d".format(endMinute),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MeetTitleInput(
    onMeetingTitleChanged: (String) -> Unit
) {
    var meetingTitle by remember {
        mutableStateOf("我的会议")
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 5.dp
            )
    ) {
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_title),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                )
                Text(
                    text = "会议主题",
                    fontSize = 22.sp
                )
            }
            OutlinedTextField(
                value = meetingTitle,
                onValueChange = {
                    meetingTitle = it
                    onMeetingTitleChanged(meetingTitle)
                },
                modifier = Modifier
                    .width((LocalConfiguration.current.screenWidthDp * 0.9).dp)
                    .wrapContentHeight(),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 20.sp
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
        }
    }
}

@Composable
fun MeetingRoomSelect(
    onRoomSelectionChanged: (MeetingRoomInfo) -> Unit,
    meetingRooms: MutableList<MeetingRoomInfo>
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedItem by remember {
        mutableStateOf(MeetingRoomInfo(
            id = -1,
            name = "请选择会议室",
            info = "",
            location = "",
            related_booking = null,
            stat_id = 0
        ))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 5.dp
            )
    ) {
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_meeting),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                )
                Text(
                    text = "会议室",
                    fontSize = 22.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(colorResource(id = R.color.light_grey))
                        .width((LocalConfiguration.current.screenWidthDp * 0.8).dp)
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.glimmer)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedItem.name,
                        fontSize = 20.sp
                    )
                }
                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
            DropdownMenu(
                modifier = Modifier
                    .width((LocalConfiguration.current.screenWidthDp * 0.9).dp),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                meetingRooms.forEach {room ->
                    DropdownMenuItem(
                        text = {
                            Text(text = room.name)
                        },
                        onClick = {
                            selectedItem = room
                            expanded = false
                            onRoomSelectionChanged(selectedItem)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DateSelect(
    onDateYearSelectionChanged: (Int) -> Unit,
    onDateMonthSelectionChanged: (Int) -> Unit,
    onDateDaySelectionChanged: (Int) -> Unit
) {
    var yearSelection by remember {
        mutableIntStateOf(LocalDate.now().year)
    }
    var monthSelection by remember {
        mutableIntStateOf(LocalDate.now().month.value)
    }
    var daySelection by remember {
        mutableIntStateOf(LocalDate.now().dayOfMonth)
    }
    var yearExpanded by remember {
        mutableStateOf(false)
    }
    var monthExpanded by remember {
        mutableStateOf(false)
    }
    var dayExpanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 5.dp
            )
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_date),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                )
                Text(
                    text = "日期",
                    fontSize = 22.sp
                )
            }
            Row {
                Box(
                    modifier = Modifier
                        .width((LocalConfiguration.current.screenWidthDp * 0.3).dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(colorResource(id = R.color.light_grey))
                                    .width((LocalConfiguration.current.screenWidthDp * 0.2).dp)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = R.color.glimmer)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = yearSelection.toString(),
                                    fontSize = 20.sp
                                )
                            }
                            IconButton(
                                onClick = {
                                    yearExpanded = true
                                }
                            ) {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                        DropdownMenu(
                            modifier = Modifier
                                .width((LocalConfiguration.current.screenWidthDp * 0.3).dp)
                                .heightIn(50.dp, 300.dp),
                            expanded = yearExpanded,
                            onDismissRequest = {
                                yearExpanded = false
                            }
                        ) {
                            (2020..2100).forEach { year ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = year.toString())
                                    },
                                    onClick = {
                                        yearSelection = year
                                        monthSelection = 1
                                        daySelection = 1
                                        yearExpanded = false
                                        onDateYearSelectionChanged(yearSelection)
                                        onDateMonthSelectionChanged(monthSelection)
                                        onDateDaySelectionChanged(daySelection)
                                    }
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .width((LocalConfiguration.current.screenWidthDp * 0.3).dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(colorResource(id = R.color.light_grey))
                                    .width((LocalConfiguration.current.screenWidthDp * 0.2).dp)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = R.color.glimmer)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = monthSelection.toString(),
                                    fontSize = 20.sp
                                )
                            }
                            IconButton(
                                onClick = {
                                    monthExpanded = true
                                }
                            ) {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                        DropdownMenu(
                            modifier = Modifier
                                .width((LocalConfiguration.current.screenWidthDp * 0.3).dp)
                                .heightIn(50.dp, 300.dp),
                            expanded = monthExpanded,
                            onDismissRequest = {
                                monthExpanded = false
                            }
                        ) {
                            (1..12).forEach { month ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = month.toString())
                                    },
                                    onClick = {
                                        monthSelection = month
                                        daySelection = 1
                                        monthExpanded = false
                                        onDateYearSelectionChanged(monthSelection)
                                        onDateDaySelectionChanged(daySelection)
                                    }
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .width((LocalConfiguration.current.screenWidthDp * 0.3).dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(colorResource(id = R.color.light_grey))
                                    .width((LocalConfiguration.current.screenWidthDp * 0.2).dp)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = R.color.glimmer)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = daySelection.toString(),
                                    fontSize = 20.sp
                                )
                            }
                            IconButton(
                                onClick = {
                                    dayExpanded = true
                                }
                            ) {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                        DropdownMenu(
                            modifier = Modifier
                                .width((LocalConfiguration.current.screenWidthDp * 0.3).dp)
                                .heightIn(50.dp, 300.dp),
                            expanded = dayExpanded,
                            onDismissRequest = {
                                dayExpanded = false
                            }
                        ) {
                            when(monthSelection) {
                                1, 3, 5, 7, 8, 10, 12 -> (1..31)
                                4, 6, 9, 11 ->(1..30)
                                2 -> if (yearSelection % 4 == 0 && yearSelection % 400 != 0) (1..29) else (1..28)
                                else -> (0..0)
                            }.forEach { day ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = day.toString())
                                    },
                                    onClick = {
                                        daySelection = day
                                        dayExpanded = false
                                        onDateYearSelectionChanged(daySelection)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}