package com.phinion.gcepluselearning

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.gcepluselearning.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  val myFont = Typeface.createFromAsset(assets, "fonts/Poppins-SemiBold.ttf")
      //  binding.home.setTypeface(myFont)
      //  binding.home      Poppins-Medium  android:fontFamily="@font/poppins_semibold"


        val correct: Int? = intent.getIntExtra("correct", 0)
        val total: Int? = intent.getIntExtra("total", 0)

        binding.score.text = "$correct / $total"

        binding.home.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }
}