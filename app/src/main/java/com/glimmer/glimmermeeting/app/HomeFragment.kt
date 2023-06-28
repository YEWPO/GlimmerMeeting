package com.glimmer.glimmermeeting.app

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Message.obtain
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.glimmer.glimmermeeting.MainActivity
import com.glimmer.glimmermeeting.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.moshi.JsonClass
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HomeFragment : Fragment(R.layout.home_layout) {
    private lateinit var myMeetingListView: ListView
    private lateinit var addActionButton: FloatingActionButton
    private lateinit var userToken: String

    @JsonClass(generateAdapter = true)
    data class MeetingListJson(
        var duration: DurationData,
        var attendees: MutableList<Int>,
        var roomid: Int,
        var theme: String,
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

    private val getMeetingListSuccessHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val meetingListJsonAdapter = MainActivity().moshi.adapter(MeetingListJson::class.java)
            Log.i("meetingjson", msg.data.getString("json")!!)

            if (msg.data.getBoolean("state")) {
                Toast.makeText(context, "获取用户会议列表信息成功", Toast.LENGTH_SHORT).show()
                val meetingListJson = msg.data.getString("json")?.let { meetingListJsonAdapter.fromJson(it) }
            } else {
                Toast.makeText(context, "获取用户会议列表信息失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getMeetingList(token: String) {
        val requestBody = FormBody.Builder()
            .add("token", token)
            .build()
        val getMeetingListRequest = Request.Builder()
            .url("http://api.mcyou.cc:2023/login")
            .post(requestBody)
            .build()

        MainActivity().okHttpClient.newCall(getMeetingListRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Login", e.printStackTrace().toString())
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
        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        userToken = sharedPreferences.getString("token", "null") ?: "null"

        getMeetingList(userToken)

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