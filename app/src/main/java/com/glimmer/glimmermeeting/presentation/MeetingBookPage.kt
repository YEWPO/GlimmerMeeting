package com.glimmer.glimmermeeting.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingBookPage(
    onPageStateChanged: (String) -> Unit
) {
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
        MeetingBookInfo()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingBookInfo() {
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
                containerColor = Color.White
            ),
            value = meetingTitle,
            onValueChange = { meetingTitle = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MeetingBookPagePreview() {
    GlimmerMeetingTheme {
        MeetingBookPage({})
    }
}