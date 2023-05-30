package com.glimmer.glimmermeeting.app

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.glimmer.glimmermeeting.R

class AppFragment : Fragment(R.layout.app_layout) {
    val createdArgs: AppFragmentArgs by navArgs()

    private lateinit var textView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView = view.findViewById(R.id.textView)
        textView.text = createdArgs.token
    }
}