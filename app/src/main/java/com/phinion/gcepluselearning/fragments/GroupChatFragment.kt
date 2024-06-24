package com.phinion.gcepluselearning.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import kotlin.text.Regex
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.phinion.gcepluselearning.R
import com.phinion.gcepluselearning.adapters.GroupChatAdapter
import com.phinion.gcepluselearning.models.GroupChat


class GroupChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageList: ArrayList<GroupChat>
    private lateinit var adapter: GroupChatAdapter
    private lateinit var sendButton: ImageView
    private lateinit var messageEditText: EditText
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var currentUserName: String
    private val groupChatRef = FirebaseDatabase.getInstance().getReference("users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("group-chat")
        firebaseAuth = FirebaseAuth.getInstance()
        currentUserId = firebaseAuth.currentUser?.uid.toString()
        currentUserName = firebaseAuth.currentUser?.displayName.toString()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_group_chat, container, false)

        recyclerView = view.findViewById(R.id.recyclerview_messages)
        sendButton = view.findViewById(R.id.button_send)
        messageEditText = view.findViewById(R.id.edittext_message)



        messageList = ArrayList()
        adapter = GroupChatAdapter(requireContext(), messageList, firebaseDatabase.reference)
        adapter.setCurrentUserId(currentUserId)
        adapter.setCurrentUserName(currentUserName)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter



        sendButton.setOnClickListener {
            val message = messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                val senderId = firebaseAuth.currentUser?.uid.toString()
                val senderName = firebaseAuth.currentUser?.displayName.toString()
                val timestamp = System.currentTimeMillis()

                val chat = GroupChat(
                    senderId,
                    senderName,
                    message,
                    timestamp,
                    isSent = true,
                    listOf(currentUserId)
                )

// Retrieve the name from shared preferences
                val sharedPrefs =
                    requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val name = sharedPrefs.getString("username", true.toString())

                // Check if the name is not null or empty
                if (name.isNullOrEmpty()) {
                    // Name is not stored in shared preferences
                    Log.d("MyApp", "Name is not stored in shared preferences.")
                } else {
                    // Name is stored in shared preferences
                    Log.d("MyApp", "Name is stored in shared preferences: $name")
                }


                // Check if message contains a phone number or external link
                val containsInvalidInput = checkMessageForPhoneNumberOrLink(message) as Boolean
                if (!containsInvalidInput) {
                    // No phone number or external link found in message, add to database
                    databaseReference.push().setValue(chat)
                    messageEditText.text.clear()
                } else {
                    // Phone number or external link found in message, show error message
                    messageEditText.error = "Phone number or external link not allowed"
                }

            }
        }
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                val chat = snapshot.getValue(GroupChat::class.java)
                messageList.add(chat!!)
                adapter.notifyDataSetChanged()
                recyclerView.smoothScrollToPosition(messageList.size - 1)
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {

            }

            override fun onCancelled(error: DatabaseError) {


            }


        })

        return view

    }

    private fun checkMessageForPhoneNumberOrLink(message: String): Boolean {

        // Regular expression to match phone numbers
        val phoneNumberRegex = Regex("\\b\\d{3}[\\-]?\\d{3}[\\-]?\\d{4}\\b")

        // Regular expression to match external links
        val linkRegex = Regex("(http|https)://[^\\s]+")

        // Check if message contains a phone number or external link
        val containsInvalidInput =
            phoneNumberRegex.containsMatchIn(message) || linkRegex.containsMatchIn(message)

        return containsInvalidInput

    }

}



