package com.example.navigasidanapi.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.navigasidanapi.view.FollowerFragment
import com.example.navigasidanapi.view.FollowingFragment

class FragmentAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle,private var data : Bundle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {
                val fragment = FollowerFragment()
                fragment.arguments = data
                return fragment
            }
            1 -> {
                val fragment = FollowingFragment()
                fragment.arguments = data
                return fragment
            }
            else -> {
                val fragment = FollowerFragment()
                fragment.arguments = data
                return fragment
            }
        }
    }

}