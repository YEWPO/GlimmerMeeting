package com.glimmer.glimmermeeting.userentry

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.glimmer.glimmermeeting.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.Date

class UserentryFragment : Fragment(R.layout.userentry_layout) {
    private lateinit var userentryFragmentStateAdapter: UserentryFragmentStateAdapter
    private lateinit var userentryViewPager: ViewPager2
    private lateinit var userentryTabLayout: TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userentryFragmentStateAdapter = UserentryFragmentStateAdapter(this)
        userentryViewPager = view.findViewById(R.id.userentryViewPage)
        userentryViewPager.adapter = userentryFragmentStateAdapter

        userentryTabLayout = view.findViewById(R.id.userentryTabLayout)
        TabLayoutMediator(userentryTabLayout, userentryViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "登录"
                1 -> "注册"
                else -> null
            }
        }.attach()

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val loginToken = sharedPref.getString("loginToken", null)

        if (loginToken != null) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val loginDate = sharedPref.getString("loginTime", null)?.let { dateFormat.parse(it) }

            val loginDiff = (Date().time - loginDate!!.time) / (1000 * 60 * 60 * 24)

            if (loginDiff <= 30) {
                Navigation.findNavController(view).navigate(R.id.appFragment)
            }
        }
    }
}

class UserentryFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> SignupFragment()
            else -> {
                throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}