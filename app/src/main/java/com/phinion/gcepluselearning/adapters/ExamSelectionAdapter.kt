package com.phinion.gcepluselearning.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phinion.gcepluselearning.R
import com.phinion.gcepluselearning.SubjectSelectionActivity
import com.phinion.gcepluselearning.models.Exam

class ExamSelectionAdapter(private val context: android.content.Context, private val examList: java.util.ArrayList<Exam>) : RecyclerView.Adapter<ExamSelectionAdapter.ExamSelectionViewHolder>() {

    inner class ExamSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamSelectionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.exam_selection_layout, parent, false)
        return ExamSelectionViewHolder(view)
    }

    // @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ExamSelectionViewHolder, position: Int) {
        val exam: Exam = examList[position]
        //  holder.itemView.setBackgroundDrawable(ColorDrawable(R.color.second_app_primary))

        val subjectCategoryIcon: ImageView = holder.itemView.findViewById(R.id.exam_icon)
        val subjectTitle: TextView = holder.itemView.findViewById(R.id.exam_title)
        val subjectCategoryDes: TextView = holder.itemView.findViewById(R.id.exam_des)
        val constraintLayout: ConstraintLayout = holder.itemView.findViewById(R.id.constraintLayout)

        Glide.with(context)
            .load(exam.icon)
            .into(subjectCategoryIcon)
        subjectTitle.text = exam.title
        subjectCategoryDes.text = exam.description

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SubjectSelectionActivity::class.java)
            intent.putExtra("id", exam.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return examList.size
    }
}
