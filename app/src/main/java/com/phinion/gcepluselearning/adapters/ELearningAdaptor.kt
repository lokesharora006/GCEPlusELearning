package com.phinion.gcepluselearning.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.phinion.gcepluselearning.fragments.ChatFragment
import com.phinion.gcepluselearning.fragments.ContactsFragment
import com.phinion.gcepluselearning.fragments.GroupChatFragment

class ELearningAdaptor(fm: FragmentManager, var tabCount: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ChatFragment()
            1 -> GroupChatFragment()
            2 -> ContactsFragment()
            else -> ChatFragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "CHAT"
            1 -> "GROUP"
            2 -> "CONTACTS"
            else -> ""
        }
    }
}
