package com.phinion.gcepluselearning.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.gcepluselearning.LoadingDialog
import com.phinion.gcepluselearning.adapters.ExamSelectionAdapter
import com.phinion.gcepluselearning.databinding.FragmentHomeBinding
import com.phinion.gcepluselearning.models.Exam
import java.util.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: ExamSelectionAdapter
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        loadingDialog = LoadingDialog(requireContext())

        mAuth = Firebase.auth
        database = Firebase.firestore

        loadingDialog.showLoadingDialog()



        val examList: ArrayList<Exam> = ArrayList<Exam>()

        adapter = ExamSelectionAdapter(requireContext(), examList)
        binding.examList.adapter = adapter
        binding.examList.layoutManager = LinearLayoutManager(requireContext())

        database.collection("exams")
            .addSnapshotListener { value, error ->

                examList.clear()
                if (value != null) {
                    for (snapshot in value.documents) {
                        val exams: Exam? = snapshot.toObject(Exam::class.java)
                        if (exams != null) {
                            exams.id = snapshot.id
                            examList.add(exams)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
                loadingDialog.dismissLoadingDialog()
            }
        return binding.root
    }
}
