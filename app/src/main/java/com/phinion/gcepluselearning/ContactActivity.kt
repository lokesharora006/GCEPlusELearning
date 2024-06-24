package com.phinion.gcepluselearning

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.gcepluselearning.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth
        database = Firebase.firestore

        database.collection("contactDetails")
            .document("UCUzbq3I8k2Ce9D7Y54E")
            .addSnapshotListener { value, error ->

                binding.contactEmail.text = value?.getString("email")
                binding.contactPhone.text = value?.getString("phone")
            }

        binding.submitBtn.setOnClickListener {
            if (binding.query.text.toString().isNotEmpty()) {
                mAuth.currentUser?.email?.let { com.phinion.gcepluselearning.models.Query(it, binding.query.text.toString()) }
                    ?.let {
                        database.collection("queries")
                            .add(it).addOnSuccessListener {
                                Toast.makeText(this, "Request send successfully", Toast.LENGTH_SHORT).show()
                            }
                    }
            } else {
                Toast.makeText(this, "Field is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
