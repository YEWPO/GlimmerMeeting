package com.glimmer.glimmermeeting.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.fragment.app.Fragment
import com.glimmer.glimmermeeting.R
import androidx.compose.ui.platform.ComposeView

class CalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_layout, container, false).apply {
            findViewById<ComposeView>(R.id.calendarComposeView).apply { 
                setContent { 
                    Text(text = "hello")
                }
            }
        }
    }
}