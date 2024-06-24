package com.phinion.gcepluselearning.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.phinion.gcepluselearning.R
import com.phinion.gcepluselearning.adapters.ELearningAdaptor

class ELearningFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_e_learning, container, false)

        tabLayout = view.findViewById(R.id.eLearningFragmenttabLayout)
        viewPager = view.findViewById(R.id.eLearningFragmentviewPager)

        setupTabLayout()
        setupViewPager()

        return view
    }

    private fun setupViewPager() {
        viewPager.apply {
            adapter = ELearningAdaptor(childFragmentManager, tabLayout.tabCount)
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }
    }

    private fun setupTabLayout() {
        tabLayout.apply {
            addTab(this.newTab().setText("CHAT"))
            addTab(this.newTab().setText("GROUP"))
            addTab(this.newTab().setText("CONTACTS"))

            // Use setupWithViewPager() to synchronize the TabLayout and ViewPager
            setupWithViewPager(viewPager)

            // Set the color of the selected tab indicator to white
            setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.white))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewPager.currentItem = tab?.position ?: 0
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }
}