package com.glimmer.glimmermeeting.userentry

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.glimmer.glimmermeeting.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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