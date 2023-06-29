package com.glimmer.glimmermeeting.app

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Message.obtain
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.glimmer.glimmermeeting.MainActivity
import com.glimmer.glimmermeeting.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

class HomeFragment : Fragment(R.layout.home_layout) {
    private lateinit var myMeetingListView: ListView
    private lateinit var addActionButton: FloatingActionButton
    private lateinit var userToken: String
    private lateinit var emptyInfoText: TextView
    private lateinit var emptyBoxView: ImageView

    @JsonClass(generateAdapter = true)
    data class MeetingInfo(
        var duration: DurationData,
        var attendees: MutableList<Int>,
        var bookerid: Int,
        var roomid: Int,
        var theme: String,
        var roomlocation: String,
        var meetingid: Int,
        var day: String
    )

    @JsonClass(generateAdapter = true)
    data class DurationData(
        var beginhour: Int,
        var beginminute: Int,
        var endhour: Int,
        var endminute: Int
    )

    private fun setMeetingList(meetingList: MutableList<Map<String, String>>) {
        val simpleAdapter = SimpleAdapter(
            requireContext(),
            meetingList,
            R.layout.meeting_info_layout,
            arrayOf("weekdayName", "date", "roomInfo", "meetingTitle", "meetingTime"),
            intArrayOf(R.id.weekdayName, R.id.dateText, R.id.roomInfoText, R.id.meetingTitleText, R.id.meetingTimeText)
        )

        myMeetingListView.adapter = simpleAdapter
    }

    private val getMeetingListSuccessHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val type = Types.newParameterizedType(MutableList::class.java, MeetingInfo::class.java)
            val meetingListJsonAdapter = MainActivity().moshi.adapter<MutableList<MeetingInfo>>(type)
            Log.i("meetingjson", msg.data.getString("json")!!)

            if (msg.data.getBoolean("state")) {
                val meetingListJson = msg.data.getString("json")?.let { meetingListJsonAdapter.fromJson(it) }

                var meetingList = mutableListOf<Map<String, String>>()
                if (meetingListJson != null) {
                    for (aMeeting in meetingListJson) {
                        val beginMinute = "%02d".format(aMeeting.duration.beginminute)
                        val endMinute = "%02d".format(aMeeting.duration.endminute)
                        meetingList.add(mapOf(
                            "weekdayName" to  SimpleDateFormat("yyyy-MM-dd").parse(aMeeting.day).toInstant().atZone(
                                ZoneId.systemDefault()).toLocalDate().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                            "date" to SimpleDateFormat("yyyy年MM月dd日").format(SimpleDateFormat("yyyy-MM-dd").parse(aMeeting.day)),
                            "roomInfo" to aMeeting.roomlocation,
                            "meetingTitle" to aMeeting.theme,
                            "meetingTime" to "${aMeeting.duration.beginhour}:$beginMinute--" +
                                    "${aMeeting.duration.endhour}:$endMinute"
                        ))
                    }
                    emptyInfoText.visibility = View.INVISIBLE
                    emptyBoxView.visibility = View.INVISIBLE
                    setMeetingList(meetingList)
                }
            } else {
                SettingFragment().logout(true)
            }
        }
    }

    private fun getMeetingList(token: String) {
        val requestBody = FormBody.Builder()
            .add("token", token)
            .build()
        val getMeetingListRequest = Request.Builder()
            .url("http://api.mcyou.cc:2023/book/personalmeeting")
            .post(requestBody)
            .build()

        MainActivity().okHttpClient.newCall(getMeetingListRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GetMeetingList", e.printStackTrace().toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val getMeetingListState = response.isSuccessful

                val getMeetingListMessage = obtain()
                val getMeetingListBundle = Bundle()
                getMeetingListBundle.putBoolean("state", getMeetingListState)
                getMeetingListBundle.putString("json", response.body!!.string())
                getMeetingListMessage.data = getMeetingListBundle

                getMeetingListSuccessHandler.sendMessage(getMeetingListMessage)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myMeetingListView = view.findViewById(R.id.meetingList)
        addActionButton = view.findViewById(R.id.addActionButton)
        emptyInfoText = view.findViewById(R.id.emptyInfoTextView)
        emptyBoxView = view.findViewById(R.id.emptyBoxImage)

        addActionButton.setOnClickListener {
            activity?.supportFragmentManager?.findFragmentById(R.id.main_fragment_container)
                ?.findNavController()
                ?.navigate(R.id.addMeetingFragment)
        }

        emptyInfoText.visibility = View.VISIBLE
        emptyBoxView.visibility = View.VISIBLE

        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        userToken = sharedPreferences.getString("loginToken", "null") ?: "null"

        Log.i("token", userToken)
        getMeetingList(userToken)
    }
}