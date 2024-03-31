package com.submission.fundamental.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.submission.fundamental.ui.main.ListDetailFollow

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""
    override fun createFragment(position: Int): Fragment {
        val fragment = ListDetailFollow()
        fragment.arguments = Bundle().apply {
            putInt(ListDetailFollow.ARG_POSITION, position + 1)
            putString(ListDetailFollow.ARG_USERNAME, username)
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return 2
    }
}