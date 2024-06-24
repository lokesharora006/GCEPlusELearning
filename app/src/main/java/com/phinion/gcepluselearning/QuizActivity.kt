package com.phinion.gcepluselearning

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.gcepluselearning.databinding.ActivityQuizBinding
import com.phinion.gcepluselearning.models.Question
import java.util.*

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var questions: ArrayList<Question>
    var index = 0
    private lateinit var question: Question
    private lateinit var timer: CountDownTimer
    private lateinit var loadingDialog: LoadingDialog
    var correctAnswers = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore

        loadingDialog = LoadingDialog(this)
        loadingDialog.showLoadingDialog()

        binding.backBtn.setOnClickListener {
            finish()
        }

        question = Question()
        questions = ArrayList<Question>()

        val random = Random()
        val rand = random.nextInt(25)

        val id: String? = intent.getStringExtra("id")
        val id2: String? = intent.getStringExtra("id2")
        val id3: String? = intent.getStringExtra("id3")
        val subjectType: String? = intent.getStringExtra("subject")

        if (id != null) {
            if (subjectType != null) {
                if (id2 != null) {
                    if (id3 != null) {
                        database.collection("exams")
                            .document(id)
                            .collection(subjectType)
                            .document(id2)
                            .collection("years")
                            .document(id3)
                            .addSnapshotListener { value, error ->
                                binding.examTitle.text =
                                    value?.getString("paper1Title") + " " + value?.getString("year")
                            }
                        database.collection("exams")
                            .document(id)
                            .collection(subjectType)
                            .document(id2)
                            .collection("years")
                            .document(id3)
                            .collection("questions")
                            .whereLessThanOrEqualTo("index", 500)
                            .orderBy("index")
                            .get().addOnSuccessListener { queryDocumentSnapshots ->

                                if (queryDocumentSnapshots.documents.size < 24) {
                                    database.collection("exams")
                                        .document(id)
                                        .collection(subjectType)
                                        .document(id2)
                                        .collection("years")
                                        .document(id3)
                                        .collection("questions")
                                        .whereLessThanOrEqualTo("index", 500)
                                        .orderBy("index")
                                        .get()
                                        .addOnSuccessListener { queryDocumentSnapshots ->
                                            for (snapshot in queryDocumentSnapshots) {
                                                val question =
                                                    snapshot.toObject(Question::class.java)
                                                questions.add(question)
                                            }
                                            setNextQuestion()
                                            loadingDialog.dismissLoadingDialog()
                                        }
                                } else {
                                    for (snapshot in queryDocumentSnapshots) {
                                        val question = snapshot.toObject(Question::class.java)
                                        questions.add(question)
                                    }
                                    setNextQuestion()
                                    loadingDialog.dismissLoadingDialog()
                                }
                            }
                    }
                }
            }
        }

        resetTimer()
    }

    fun resetTimer() {
        timer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                Toast.makeText(baseContext, "Time Up :(", Toast.LENGTH_SHORT).show()

                val intent = Intent(baseContext, ResultActivity::class.java)
                intent.putExtra("correct", correctAnswers)
                intent.putExtra("total", questions.size)
                startActivity(intent)
                finish()
            }
        }
    }

    fun setNextQuestion() {
        if (timer != null) timer.cancel()
        timer.start()
        if (index < questions.size) {
            binding.questionCounter.text = String.format("%d/%d", index + 1, questions.size)
            question = questions[index]
            binding.questionText.text = question.question
            binding.option1.text = question.option1
            binding.option2.text = question.option2
            binding.option3.text = question.option3
            binding.option4.text = question.option4

            Glide.with(this)
                .load(question.imageLink)
                .apply(
                    RequestOptions()
                        .error(R.drawable.placeholder)
                        .fitCenter(),
                )
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean,
                    ): Boolean {
                        // on load failed
                        binding.questionImage.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        // on load success
                        binding.questionImage.visibility = View.VISIBLE
                        return false
                    }
                })
                .into(binding.questionImage)

            binding.option1.isEnabled = true
            binding.option2.isEnabled = true
            binding.option3.isEnabled = true
            binding.option4.isEnabled = true
        }
    }

    fun showAnswer() {
        if (question.answer
                .equals(binding.option1.text.toString())
        ) {
            binding.option1.background =
                resources.getDrawable(R.drawable.right_answer_background)
            binding.option1.setTextColor(resources.getColor(R.color.white))
        } else if (question.answer
                .equals(binding.option2.text.toString())
        ) {
            binding.option2.background =
                resources.getDrawable(R.drawable.right_answer_background)
            binding.option2.setTextColor(resources.getColor(R.color.white))
        } else if (question.answer
                .equals(binding.option3.text.toString())
        ) {
            binding.option3.background =
                resources.getDrawable(R.drawable.right_answer_background)
            binding.option3.setTextColor(resources.getColor(R.color.white))
        } else if (question.answer
                .equals(binding.option4.text.toString())
        ) {
            binding.option4.background =
                resources.getDrawable(R.drawable.right_answer_background)
            binding.option4.setTextColor(resources.getColor(R.color.white))
        }

        binding.option1.isEnabled = false
        binding.option2.isEnabled = false
        binding.option3.isEnabled = false
        binding.option4.isEnabled = false
    }

    fun checkAnswer(textView: TextView) {
        val selectedAnswer = textView.text.toString()
        if (selectedAnswer == question.answer) {
            correctAnswers++
            textView.background = resources.getDrawable(R.drawable.right_answer_background)
            binding.option1.isEnabled = false
            binding.option2.isEnabled = false
            binding.option3.isEnabled = false
            binding.option4.isEnabled = false
            textView.setTextColor(resources.getColor(R.color.white))
        } else {
            showAnswer()
            textView.background = resources.getDrawable(R.drawable.wrong_answer_background)
            binding.option1.isEnabled = false
            binding.option2.isEnabled = false
            binding.option3.isEnabled = false
            binding.option4.isEnabled = false
            textView.setTextColor(resources.getColor(R.color.white))
        }
    }

    fun reset() {
        binding.option1.background = resources.getDrawable(R.drawable.option_background)
        binding.option2.background = resources.getDrawable(R.drawable.option_background)
        binding.option3.background = resources.getDrawable(R.drawable.option_background)
        binding.option4.background = resources.getDrawable(R.drawable.option_background)
        binding.option1.setTextColor(resources.getColor(R.color.option_text_color))
        binding.option2.setTextColor(resources.getColor(R.color.option_text_color))
        binding.option3.setTextColor(resources.getColor(R.color.option_text_color))
        binding.option4.setTextColor(resources.getColor(R.color.option_text_color))
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.option1, R.id.option2, R.id.option3, R.id.option4 -> {
                if (timer != null) timer.cancel()
                val selected = view as TextView
                checkAnswer(selected)
            }
            R.id.next_btn -> {
                reset()
                if (index < questions.size - 1) {
                    index++
                    setNextQuestion()
                } else {
                    val intent = Intent(
                        baseContext,
                        ResultActivity::class.java,
                    )
                    intent.putExtra("correct", correctAnswers)
                    intent.putExtra("total", questions.size)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Exam Finished.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
