package com.glimmer.glimmermeeting.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.glimmer.glimmermeeting.R

class AddMeetingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_meeting_layout, container, false).apply {
            findViewById<ComposeView>(R.id.addMeetingComposeView).apply {
                setContent {
                    AddMeetingScreen(onNavigate = {
                        activity?.supportFragmentManager?.findFragmentById(R.id.main_fragment_container)
                            ?.findNavController()?.navigate(it)
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMeetingScreen(
    onNavigate: (Int) -> Unit
) {
    var roomSelection by remember {
        mutableStateOf("请选择会议室")
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
            MeetingRoomSelect(
                onRoomSelectionChanged = {room ->
                    roomSelection = room
                }
            )
        }
    }
}

@Composable
fun MeetingRoomSelect(
    onRoomSelectionChanged: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedItem by remember {
        mutableStateOf("请选择会议室")
    }

    val items = listOf("1", "2", "3")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 10.dp
            )
    ) {
        Column() {
            Text(
                text = "预定会议室",
                fontSize = 18.sp
            )
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
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = selectedItem,
                        fontSize = 16.sp
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
                items.forEach {item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item)
                        },
                        onClick = {
                            selectedItem = item
                            expanded = false
                            onRoomSelectionChanged(selectedItem)
                        }
                    )
                }
            }
        }
    }
}