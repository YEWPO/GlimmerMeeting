package com.glimmer.glimmermeeting.app

import android.icu.util.LocaleData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import com.glimmer.glimmermeeting.R
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.atStartOfMonth
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.selects.select
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.round

class CalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_layout, container, false).apply {
            findViewById<ComposeView>(R.id.calendarComposeView).apply { 
                setContent {
                    MainScreen()
                }
            }
        }
    }
}

private val monthFormat = DateTimeFormatter.ofPattern("yyyy年MM月")

@Preview
@Composable
fun MainScreen() {
    val currentDate = remember {
        LocalDate.now()
    }
    val currentMonth = remember {
        YearMonth.now()
    }
    val startDate = remember {
        currentMonth.minusMonths(120).atStartOfMonth()
    }
    val endDate = remember {
        currentMonth.plusMonths(120).atEndOfMonth()
    }
    var selection by remember {
        mutableStateOf(currentDate)
    }

    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate
    )
    
    Column {
        Text(
            text = monthFormat.format(selection),
            color = Color.Black,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0099FF))
                .padding(
                    start = 10.dp,
                    top = 10.dp,
                    bottom = 10.dp
                )
        )
        WeekCalendar(
            modifier = Modifier
                .background(color = Color.White),
            state = state,
            dayContent = { day ->
                Day(date = day.date, isSelected = selection == day.date) { clicked ->
                    if (selection != clicked) {
                        selection = clicked
                    }
                }
            }
        )
        Divider()
        Box(
            modifier = Modifier
            .padding(10.dp)
        ) {
            RoomSchedule(roomName = "testRoom", roomSize = 30)
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
private fun Day(
    date: LocalDate,
    isSelected: Boolean,
    onClick: (LocalDate) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Light,
            )
            Divider()
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(6.dp)
                    .clip(CircleShape)
                    .border(
                        shape = CircleShape,
                        width = 1.dp,
                        color = if (isSelected) colorResource(id = R.color.glimmer) else Color.Transparent
                    )
                    .background(
                        color = if (date == LocalDate.now() && isSelected) colorResource(id = R.color.glimmer) else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dateFormatter.format(date),
                    fontSize = 20.sp,
                    color = if (date < LocalDate.now())
                        Color.Gray
                    else if (date == LocalDate.now() && isSelected)
                        Color.White
                    else if(date == LocalDate.now())
                        colorResource(id = R.color.glimmer)
                    else Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun RoomSchedule(
    roomName: String,
    roomSize: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = 2.dp,
                color = colorResource(id = R.color.glimmer)
            )
            .padding(
                horizontal = 10.dp
            )
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Text(
                text = roomName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.room_people),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                )
                Text(
                    text = "$roomSize 人",
                    fontSize = 14.sp
                )
            }
            TimerBar(ranges = listOf(1 to 2))
        }
    }
}

@Composable
private fun TimerBar(
    ranges: List<Pair<Int, Int>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            repeat(8) {
                Box(
                    modifier = Modifier
                        .size(
                            width = 36.dp,
                            height = 46.dp
                        )

                ) {
                    Column() {
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.glimmer)
                                )
                        ) {
                            Row {
                                repeat(4) {
                                    Box(
                                        modifier = Modifier
                                            .size(
                                                width = 9.dp,
                                                height = 30.dp
                                            )
                                    )
                                }
                            }
                        }
                        Text(
                            text = (it * 2 + 8).toString(),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}