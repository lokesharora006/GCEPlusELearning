package com.phinion.gcepluselearning

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.phinion.gcepluselearning.databinding.ActivityLogInBinding


class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth
        loadingDialog = LoadingDialog(this)

        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.submitBtn.setOnClickListener {
            val email: String = binding.email.text.toString()
            val pass: String = binding.password.text.toString()

            if (validation(email, pass)) {
                loadingDialog.showLoadingDialog()
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        loadingDialog.dismissLoadingDialog()
                        Toast.makeText(this, "Log In Success", Toast.LENGTH_SHORT).show()

                        // After successfully logging in with Firebase
                        val user = mAuth.currentUser

                        database = FirebaseFirestore.getInstance()
                        val docRef =
                            user?.let { it1 -> database.collection("users").document(it1.uid) }

                        docRef?.get()?.addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                val name = documentSnapshot.getString("name") ?: ""


                                // Store name  SharedPreferences
                                val sharedPrefs =
                                    getSharedPreferences("UserData", Context.MODE_PRIVATE)
                                val editor = sharedPrefs.edit()
                                editor.putString("username", name)
                                editor.apply()
                            }
                        }


                        // getuserdetail
                        // store name in sharedprefrece
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        loadingDialog.dismissLoadingDialog()
                        Toast.makeText(
                            baseContext,
                            it.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill up the required fields.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun validation(email: String, pass: String): Boolean {
        return email.isNotEmpty() && pass.isNotEmpty()


    }
}
