package com.phinion.gcepluselearning.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phinion.gcepluselearning.R
import com.phinion.gcepluselearning.SubjectSelectionActivity
import com.phinion.gcepluselearning.YearSelectionOnClick
import com.phinion.gcepluselearning.models.Exam
import com.phinion.gcepluselearning.models.Paper
import com.phinion.gcepluselearning.models.Subject

class YearSelectionAdapterForPaper3(
    private val context: android.content.Context,
    private val paperList: java.util.ArrayList<Paper>,
    private val yearSelectionOnClick: YearSelectionOnClick
) : RecyclerView.Adapter<YearSelectionAdapterForPaper3.YearSelectionViewHolder>() {


    inner class YearSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearSelectionViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.paper_selection_layout, parent, false)
        return YearSelectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: YearSelectionViewHolder, position: Int) {

        val paper: Paper = paperList[position]

        val subjectNumber: TextView = holder.itemView.findViewById(R.id.icon_number)
        val subjectTitle: TextView = holder.itemView.findViewById(R.id.subject_title)





        subjectTitle.text = paper.paper3Title
        subjectNumber.text = paper.year

        holder.itemView.setOnClickListener {

            yearSelectionOnClick.yearSelectionOnClickListener(position)

        }

    }

    override fun getItemCount(): Int {
        return paperList.size
    }

}