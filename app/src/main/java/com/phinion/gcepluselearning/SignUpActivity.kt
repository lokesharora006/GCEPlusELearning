package com.phinion.gcepluselearning

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.gcepluselearning.databinding.ActivitySignUpBinding
import com.phinion.gcepluselearning.models.User

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.firestore

        loadingDialog = LoadingDialog(this)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        binding.submitBtn.setOnClickListener {
            if (validation(
                    binding.name.text.toString(),
                    binding.email.text.toString(),
                    binding.password.text.toString(),
                    binding.school.text.toString()
                )
            ) {
                loadingDialog.showLoadingDialog()
                authWithFirebase(
                    binding.email.text.toString(),
                    binding.password.text.toString(),
                    binding.name.text.toString(),
                    binding.school.text.toString()
                )
            } else {
                Toast.makeText(this, "Please fill up the required fields.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun authWithFirebase(email: String, pass: String, name: String, school: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = auth.currentUser // currentUser --> email, pass, uid
                    val user1 = User(name, email, pass, school)
                    if (user != null) {

                        saveUserToFirebaseDatabase(user1.name, user1.email, user1.school)
                        database.collection("users").document(user.uid)
                            .set(user1).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    loadingDialog.dismissLoadingDialog()
                                    Toast.makeText(
                                        this,
                                        "Account Created Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                            }
                    }
                } else {
                    // If sign in fails, display a message to the user.

                    loadingDialog.dismissLoadingDialog()
                    Toast.makeText(
                        baseContext,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }


    }


    private fun saveUserToFirebaseDatabase(name: String, email: String, school: String) {

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(name, email, school)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }
}


private fun validation(name: String, email: String, pass: String, school: String): Boolean {
    return name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && school.isNotEmpty()
}
