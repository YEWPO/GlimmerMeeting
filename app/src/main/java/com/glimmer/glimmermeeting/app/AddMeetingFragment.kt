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
import androidx.compose.runtime.LaunchedEffect
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
import com.glimmer.glimmermeeting.MainActivity
import com.glimmer.glimmermeeting.R
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

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
                        token = activity?.getPreferences(Context.MODE_PRIVATE)?.getString("loginToken", null)?:"null"
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
    token: String
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
                },
                meetingRooms = meetingRoomList
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
                vertical = 10.dp
            )
    ) {
        Column() {
            Text(
                text = "选择会议室",
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
                        text = selectedItem.name,
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