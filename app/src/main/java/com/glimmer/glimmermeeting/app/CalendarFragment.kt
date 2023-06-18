package com.glimmer.glimmermeeting.app

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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
                .background(color = Color.White)
                .padding(
                    bottom = 18.dp
                ),
            state = state,
            dayContent = { day ->
                Day(date = day.date, isSelected = selection == day.date) { clicked ->
                    if (selection != clicked) {
                        selection = clicked
                    }
                }
            }
        )
        RoomSchedule(roomName = "testRoom", roomSize = 30)
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
private fun Day(date: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Light,
            )
            Text(
                text = dateFormatter.format(date),
                fontSize = 20.sp,
                color = if (isSelected) {
                    if (date == LocalDate.now()) colorResource(id = R.color.glimmer) else Color.DarkGray
                } else if (date < LocalDate.now())
                    Color.Gray
                else if (date == LocalDate.now()) colorResource(
                    id = R.color.glimmer
                ) else Color.Black,
                fontWeight = FontWeight.Bold,
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(if (date == LocalDate.now()) colorResource(id = R.color.glimmer) else Color.DarkGray)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun RoomSchedule(roomName: String, roomSize: Int) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .border(
            width = 1.dp,
            color = colorResource(id = R.color.glimmer)
        )
        .padding(
            horizontal = 20.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
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
                        .size(20.dp)
                )
                Text(
                    text = "$roomSize 人",
                    fontSize = 15.sp
                )
            }
        }
    }
}