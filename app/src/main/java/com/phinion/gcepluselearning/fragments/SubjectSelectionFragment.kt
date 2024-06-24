package com.phinion.gcepluselearning.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.gcepluselearning.LoadingDialog
import com.phinion.gcepluselearning.SubjectSelectionOnClick
import com.phinion.gcepluselearning.YearSelectionActivity
import com.phinion.gcepluselearning.adapters.SubjectSelectionAdapter
import com.phinion.gcepluselearning.databinding.FragmentSubjectSelectionBinding
import com.phinion.gcepluselearning.models.Subject
import java.util.ArrayList

class SubjectSelectionFragment : Fragment() {

    private lateinit var binding: FragmentSubjectSelectionBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: SubjectSelectionAdapter
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubjectSelectionBinding.inflate(layoutInflater, container, false)

        database = Firebase.firestore

        loadingDialog = LoadingDialog(requireContext())
        loadingDialog.showLoadingDialog()

        val id: String? = requireActivity().intent.getStringExtra("id")
        if (id != null) {
            Log.d("DBPS", id)
        }

        val subjectList: ArrayList<Subject> = ArrayList<Subject>()

        database.collection("exams")
            .document(id!!)
            .collection("generalSubjects")
            .addSnapshotListener { value, error ->
                subjectList.clear()
                if (value != null) {
                    for (snapshot in value.documents) {
                        val subject: Subject? = snapshot.toObject(Subject::class.java)
                        if (subject != null) {
                            subject.id = snapshot.id
                            Log.d("DBPS", snapshot.id)
                            subjectList.add(subject)
                        }
                    }
                } else {
                    Log.d("DBPS", "Value is null")
                }
                adapter.notifyDataSetChanged()
                loadingDialog.dismissLoadingDialog()
            }

        adapter = SubjectSelectionAdapter(
            requireContext(),
            subjectList,
            object :
                SubjectSelectionOnClick {
                override fun subjectSelectionOnClickListener(position: Int) {
                    val intent = Intent(requireContext(), YearSelectionActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("id2", subjectList[position].id)
                    intent.putExtra("subject", "generalSubjects")
                    requireContext().startActivity(intent)
                }
            },
        )
        binding.subjectList.adapter = adapter
        binding.subjectList.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }
}
