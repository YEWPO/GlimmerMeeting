package com.glimmer.glimmermeeting.app

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.glimmer.glimmermeeting.R

class SettingFragment : Fragment(R.layout.setting_layout) {
    private lateinit var logoutButton: Button

    private lateinit var settingUserAccount: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logoutButton = view.findViewById(R.id.logoutButton)
        settingUserAccount = view.findViewById(R.id.settingUserAccount)

        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        settingUserAccount.text = sharedPreferences.getString("loginToken", "null")

        logoutButton.setOnClickListener {
            with(sharedPreferences.edit()) {
                clear()
                apply()
            }

            activity?.supportFragmentManager?.findFragmentById(R.id.main_fragment_container)!!.findNavController().navigate(R.id.userentryFragment)
        }
    }
}