package com.phinion.gcepluselearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.gcepluselearning.adapters.VPAdapter
import com.phinion.gcepluselearning.databinding.ActivitySubjectSelectionBinding
import com.phinion.gcepluselearning.fragments.PoolSubjectSelectionFragment
import com.phinion.gcepluselearning.fragments.SubjectSelectionFragment

class SubjectSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubjectSelectionBinding
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore

        binding.backBtn.setOnClickListener{
            finish()
        }

        binding.tabLayout.setupWithViewPager(binding.viewpager)

        val vpAdapter: VPAdapter = VPAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        vpAdapter.addFragment(SubjectSelectionFragment(), "General Subject")
        vpAdapter.addFragment(PoolSubjectSelectionFragment(), "Pool Subject")

        binding.viewpager.adapter = vpAdapter


    }
}