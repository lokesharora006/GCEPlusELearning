package com.phinion.gcepluselearning.fragments

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.URLUtil
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.downloader.PRDownloader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.phinion.gcepluselearning.LoadingDialog
import android.Manifest
import android.content.Intent
import com.phinion.gcepluselearning.PdfViewActivity
import com.phinion.gcepluselearning.YearSelectionOnClick
import com.phinion.gcepluselearning.adapters.YearSelectionAdapterForPaper2
import com.phinion.gcepluselearning.databinding.FragmentPaper2Binding
import com.phinion.gcepluselearning.models.Paper
import java.io.File

class Paper2Fragment : Fragment() {

    private lateinit var binding: FragmentPaper2Binding
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: YearSelectionAdapterForPaper2
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaper2Binding.inflate(layoutInflater, container, false)
        database = Firebase.firestore

        loadingDialog = LoadingDialog(requireContext())
        loadingDialog.showLoadingDialog()

        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adview.loadAd(adRequest)


        val id: String? = requireActivity().intent.getStringExtra("id")
        val id2: String? = requireActivity().intent.getStringExtra("id2")
        val subjectType: String? = requireActivity().intent.getStringExtra("subject")

        PRDownloader.initialize(requireContext())

        val paperList: java.util.ArrayList<Paper> = ArrayList<Paper>()

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

        adapter = YearSelectionAdapterForPaper2(
            requireContext(),
            paperList,
            object :
                YearSelectionOnClick {
                override fun yearSelectionOnClickListener(position: Int) {
                    val pdfUrl: String = paperList[position].paper2pdfLink



                    val intent = Intent(requireContext(), PdfViewActivity::class.java)
                    intent.putExtra("pdf_url", pdfUrl)
                    startActivity(intent)
                }
            },
        )

        binding.paperList.adapter = adapter
        binding.paperList.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }
}
