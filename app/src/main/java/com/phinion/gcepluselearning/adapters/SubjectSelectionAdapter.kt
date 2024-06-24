package com.phinion.gcepluselearning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phinion.gcepluselearning.R
import com.phinion.gcepluselearning.SubjectSelectionOnClick
import com.phinion.gcepluselearning.models.Subject

class SubjectSelectionAdapter(
    private val context: android.content.Context,
    private val subjectList: java.util.ArrayList<Subject>,
    private val subjectSelectionOnClick: SubjectSelectionOnClick,
) : RecyclerView.Adapter<SubjectSelectionAdapter.SubjectSelectionViewHolder>() {

    inner class SubjectSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectSelectionViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.subject_selection_layout, parent, false)
        return SubjectSelectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectSelectionViewHolder, position: Int) {
        val subject: Subject = subjectList[position]

        val subjectNumber: TextView = holder.itemView.findViewById(R.id.icon_number)
        val subjectTitle: TextView = holder.itemView.findViewById(R.id.subject_title)

        subjectTitle.text = subject.title
        subjectNumber.text = subject.iconNumber

        holder.itemView.setOnClickListener {
            subjectSelectionOnClick.subjectSelectionOnClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return subjectList.size
    }
}
