package com.glimmer.glimmermeeting.app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.glimmer.glimmermeeting.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AppFragment : Fragment(R.layout.app_layout) {
    val createdArgs: AppFragmentArgs by navArgs()

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomNavigationView = view.findViewById(R.id.appBottomNavView)

        bottomNavigationView.setupWithNavController(childFragmentManager.findFragmentById(R.id.app_fragment_container)!!.findNavController())
    }
}