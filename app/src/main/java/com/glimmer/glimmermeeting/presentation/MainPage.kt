package com.glimmer.glimmermeeting.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glimmer.glimmermeeting.R
import com.glimmer.glimmermeeting.ui.theme.BlueDeep
import com.glimmer.glimmermeeting.ui.theme.BlueLight
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme

@Composable
fun MainPage(onDrawerStageChanged: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.background),
                contentScale = ContentScale.FillBounds
            )
    ) {
        MainPageTopBar(onDrawerStageChanged = onDrawerStageChanged)
        FunctionCard()
        MyMeetingInfo()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageTopBar(onDrawerStageChanged: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onDrawerStageChanged() },
                painter = painterResource(id = R.drawable.jiahua),
                contentDescription = "User Avatar"
            )
            Column {
                Text(text = "陈佳华", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = "电子科技大学", fontSize = 14.sp, color = Color(0xFF808080))
            }
        }
        BadgedBox(
            modifier = Modifier
                .padding(end = 20.dp),
            badge = {
                Badge {
                    Text(text = "8")
                }
            }
        ) {
            Icon(
                modifier = Modifier
                    .clickable {},
                imageVector = Icons.Outlined.MailOutline,
                contentDescription = "System Message"
            )
        }
    }
}

@Composable
fun FunctionCard() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FunctionButton(
                functionDescriptor = "扫码签到",
                functionPainter = painterResource(id = R.drawable.scanner),
                onFunctionClicked = {}
            )
            FunctionButton(
                functionDescriptor = "预定会议",
                functionPainter = painterResource(id = R.drawable.add),
                onFunctionClicked = {}
            )
            FunctionButton(
                functionDescriptor = "历史会议",
                functionPainter = painterResource(id = R.drawable.history),
                onFunctionClicked = {}
            )
        }
    }
}

@Composable
fun FunctionButton(
    functionDescriptor: String,
    functionPainter: Painter,
    onFunctionClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(top = 12.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .clickable { onFunctionClicked() },
            painter = functionPainter,
            contentDescription = functionDescriptor
        )
        Text(text = functionDescriptor, fontSize = 14.sp)
    }
}

@Composable
fun MyMeetingInfo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "我的会议", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(4) {
                MeetingInfoDayCard()
            }
        }
    }
}

@Composable
fun MeetingInfoDayCard() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(15.dp),
                painter = painterResource(id = R.drawable.calendar_month),
                contentDescription = "calendar"
            )
            Text(text = "12月7日 星期四", fontSize = 14.sp, color = Color(0xFF707070))
        }
        repeat(2) {
            MeetingInfoCard()
        }
    }
}

@Composable
fun MeetingInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "综设进度汇报", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "12:00", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text(text = "GMT+08:00", fontSize = 14.sp, color = Color(0xFF808080))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.left_dots),
                        contentDescription = "left-dots"
                    )
                    Card(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = BlueDeep,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = BlueLight,
                            contentColor = BlueDeep
                        )
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(all = 4.dp),
                            text = "待开始",
                            fontSize = 12.sp
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.right_dots),
                        contentDescription = "right-dots"
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "14:00", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text(text = "GMT+08:00", fontSize = 14.sp, color = Color(0xFF808080))
                }
            }
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "会议室：信软楼西306", fontSize = 14.sp, color = Color(0xFF606060))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    GlimmerMeetingTheme {
        MainPage({})
    }
}