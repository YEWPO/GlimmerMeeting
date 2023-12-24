package com.glimmer.glimmermeeting.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.glimmer.glimmermeeting.R
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingDetailedPage() {
    Column {
        CenterAlignedTopAppBar(
            title = { Text(text = "会议详情") },
            navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Arrow Back")
                }
            },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Outlined.Share, contentDescription = "Share")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.more), contentDescription = "more")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MeetingPageDetailedPagePreview() {
    GlimmerMeetingTheme {
        MeetingDetailedPage()
    }
}