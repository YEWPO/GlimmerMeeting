package com.glimmer.glimmermeeting.app

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.glimmer.glimmermeeting.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(R.layout.home_layout) {
    private lateinit var myMeetingList: ListView
    private lateinit var addActionButton: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myMeetingList = view.findViewById(R.id.meetingList)
        addActionButton = view.findViewById(R.id.addActionButton)
    }
}