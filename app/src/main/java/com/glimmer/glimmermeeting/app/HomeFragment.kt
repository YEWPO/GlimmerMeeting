package com.glimmer.glimmermeeting.app

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import com.glimmer.glimmermeeting.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(R.layout.home_layout) {
    private lateinit var myMeetingListView: ListView
    private lateinit var addActionButton: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myMeetingListView = view.findViewById(R.id.meetingList)
        addActionButton = view.findViewById(R.id.addActionButton)

        var myMeetingInfo = mutableListOf(
            mapOf(
                "weekdayName" to "周日",
                "date" to "6月25日",
                "roomInfo" to "testRoom",
                "meetingTitle" to "testTitle",
                "meetingTime" to "19:00-20:00"
            ),
            mapOf(
                "weekdayName" to "周日",
                "date" to "6月25日",
                "roomInfo" to "testRoom",
                "meetingTitle" to "testTitle",
                "meetingTime" to "19:00-20:00"
            )
        )

        val simpleAdapter = SimpleAdapter(
            requireContext(),
            myMeetingInfo,
            R.layout.meeting_info_layout,
            arrayOf("weekdayName", "date", "roomInfo", "meetingTitle", "meetingTime"),
            intArrayOf(R.id.weekdayName, R.id.dateText, R.id.roomInfoText, R.id.meetingTitleText, R.id.meetingTimeText)
        )

        myMeetingListView.adapter = simpleAdapter
    }
}