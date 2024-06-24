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
import com.phinion.gcepluselearning.QuizActivity
import com.phinion.gcepluselearning.YearSelectionOnClick
import com.phinion.gcepluselearning.adapters.YearSelectionAdapter
import com.phinion.gcepluselearning.databinding.FragmentPaper1Binding
import com.phinion.gcepluselearning.models.Paper
import java.util.ArrayList

class Paper1Fragment : Fragment() {

    private lateinit var binding: FragmentPaper1Binding
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: YearSelectionAdapter
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaper1Binding.inflate(layoutInflater, container, false)

        database = Firebase.firestore
        loadingDialog = LoadingDialog(requireContext())
        loadingDialog.showLoadingDialog()

        val id: String? = requireActivity().intent.getStringExtra("id")
        val id2: String? = requireActivity().intent.getStringExtra("id2")
        val subjectType: String? = requireActivity().intent.getStringExtra("subject")

        val paperList: ArrayList<Paper> = ArrayList<Paper>()

        if (subjectType != null) {
            if (id2 != null) {
                database.collection("exams")
                    .document(id!!)
                    .collection(subjectType)
                    .document(id2)
                    .collection("years")
                    .addSnapshotListener { value, error ->
                        paperList.clear()
                        if (value != null) {
                            for (snapshot in value.documents) {
                                val paper: Paper? = snapshot.toObject(Paper::class.java)
                                if (paper != null) {
                                    paper.id = snapshot.id
                                    Log.d("DBPS", snapshot.id)
                                    paperList.add(paper)
                                }
                            }
                        } else {
                            Log.d("DBPS", "Value is null")
                        }
                        adapter.notifyDataSetChanged()
                        loadingDialog.dismissLoadingDialog()
                    }
            }
        }

        adapter = YearSelectionAdapter(
            requireContext(),
            paperList,
            object :
                YearSelectionOnClick {
                override fun yearSelectionOnClickListener(position: Int) {
                    val intent = Intent(requireContext(), QuizActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("id2", id2)
                    intent.putExtra("id3", paperList[position].id)
                    intent.putExtra("subject", subjectType)
                    requireContext().startActivity(intent)

                }
            },
        )
        binding.paperList.adapter = adapter
        binding.paperList.layoutManager = LinearLayoutManager(requireContext())

        return binding.root

    }

    
}
