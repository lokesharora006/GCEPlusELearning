package com.phinion.gcepluselearning.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.phinion.gcepluselearning.R
import com.phinion.gcepluselearning.databinding.FragmentMcqPerformanceBinding
import com.phinion.gcepluselearning.databinding.FragmentProfileBinding
import java.util.zip.Inflater


class McqPerformanceFragment : Fragment() {


    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var binding: FragmentMcqPerformanceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mcq_performance, container, false)




        val adapter = ELearningPagerAdapter(childFragmentManager)
        adapter.addFragment(WeekFragment(), "This Week")
        adapter.addFragment(MonthFragment(), "This Month")
        adapter.addFragment(AllTimeFragment(), "All Times")




        return  view
    }

    class ELearningPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragments = mutableListOf<Fragment>()
        private val titles = mutableListOf<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }


}