package com.phinion.gcepluselearning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.phinion.gcepluselearning.adapters.VPAdapter
import com.phinion.gcepluselearning.databinding.ActivityYearSelectionBinding
import com.phinion.gcepluselearning.fragments.Paper1Fragment
import com.phinion.gcepluselearning.fragments.Paper2Fragment
import com.phinion.gcepluselearning.fragments.Paper3Fragment

class YearSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYearSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYearSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabLayout2.setupWithViewPager(binding.viewpager)

        binding.backBtn.setOnClickListener {
            finish()
        }

        val vpAdapter: VPAdapter = VPAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        vpAdapter.addFragment(Paper1Fragment(), "Paper 1")
        vpAdapter.addFragment(Paper2Fragment(), "Paper 2")
        vpAdapter.addFragment(Paper3Fragment(), "Paper 3")

        binding.viewpager.adapter = vpAdapter
    }
}
