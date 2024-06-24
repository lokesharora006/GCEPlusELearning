package com.phinion.gcepluselearning.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.gcepluselearning.ContactActivity
import com.phinion.gcepluselearning.SignUpActivity
import com.phinion.gcepluselearning.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        database = Firebase.firestore
        mAuth = Firebase.auth

        binding.contactBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ContactActivity::class.java))
        }

        binding.signOutBtn.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(requireContext(), SignUpActivity::class.java))
            requireActivity().finish()
        }

        binding.email.isEnabled = false

        mAuth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .addSnapshotListener { value, error ->

                    val user: com.phinion.gcepluselearning.models.User? = value?.toObject(com.phinion.gcepluselearning.models.User::class.java)

                    if (user != null) {
                        binding.name.setText(user.name)
                        binding.school.setText(user.school)
                        binding.email.setText(user.email)
                    }
                }
        }

        binding.updateBtn.setOnClickListener {
            if (binding.name.text.isNotEmpty() && binding.school.text.isNotEmpty()) {
                mAuth.currentUser?.let {
                    database.collection("users")
                        .document(it.uid)
                        .update("name", binding.name.text.toString())
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Name Updated Successfully",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                }

                mAuth.currentUser?.let {
                    database.collection("users")
                        .document(it.uid)
                        .update("school", binding.school.text.toString())
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "School Updated Successfully",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Field cannot be empty",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        return binding.root
    }
}
