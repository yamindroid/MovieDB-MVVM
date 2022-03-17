package com.ymo.ui


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ymo.ui.component.popular.PopularFragment
import com.ymo.ui.component.upcoming.UpcomingFragment

private const val NUM_TABS = 4 //todo clean later

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return PopularFragment()
            1 -> return UpcomingFragment()
            2 -> return PopularFragment()
            3 -> return UpcomingFragment()
        }
        return PopularFragment()
    }
}