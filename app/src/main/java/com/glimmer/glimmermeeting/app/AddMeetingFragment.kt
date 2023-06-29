package com.glimmer.glimmermeeting.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
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
fun AddMeetingScreen(onNavigate: (Int) -> Unit) {
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
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            Text(text = "here")
        }
    }
}