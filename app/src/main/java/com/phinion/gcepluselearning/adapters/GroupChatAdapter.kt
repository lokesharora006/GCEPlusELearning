package com.phinion.gcepluselearning.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.phinion.gcepluselearning.R
import com.phinion.gcepluselearning.models.GroupChat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GroupChatAdapter(
    val context: Context,
    private val messageList: ArrayList<GroupChat>,
    private val database: DatabaseReference,
    private var username: String? = null


) : RecyclerView.Adapter<ViewHolder>() {

    private var currentUserId: String = ""
    private var currentUserName: String = ""

    fun setCurrentUserId(senderId: String) {
        currentUserId = senderId
    }

    fun setCurrentUserName(senderName: String) {
        currentUserName = senderName
    }


    val VIEW_TYPE_SENT = 2
    val VIEW_TYPE_RECEIVED = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            // inflate receive

            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.receive_groupchat_items, parent, false)

            return ReceiveViewHolder(view)

        } else {
            // inflate sent
            val view: View =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.send_groupchat_items, parent, false)

            return SentViewHolder(view)

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]
        val senderId = currentMessage.senderId

        if (holder is SentViewHolder) {
            holder.sentmessage?.text = currentMessage.message
            holder.senttimestamp?.text = formatDate(currentMessage.timestamp, "hh:mm a")

            // Set sender name to "Me" if the message is sent by the user
            holder.sentName?.text = if (senderId == username) username else username

        } else if (holder is ReceiveViewHolder) {
            // Retrieve sender's name from the "users" node
            if (senderId != null) {
                database.child("users").child(senderId).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val senderName = snapshot.child("name").getValue(String::class.java)
                        Log.d("senderName",snapshot.toString())
                        holder.receiveName?.text = senderName ?: "Unknown"
//                        holder.receiveName?.text = currentMessage.senderName ?: "Unknown"
                        holder.receivemessage?.text = currentMessage.message
                        holder.receivetimestamp?.text =
                            formatDate(currentMessage.timestamp, "hh:mm a")
                    }

                    override fun onCancelled(error: DatabaseError) {


                    }
                })

            }
        }
    }

    private fun formatDate(dateInMilliseconds: Long, dateFormat: String): String {
        val timeZone = TimeZone.getDefault()
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(Date(dateInMilliseconds))
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {

            return VIEW_TYPE_SENT
        } else {

            return VIEW_TYPE_RECEIVED
        }

    }

    class SentViewHolder(itemView: View) : ViewHolder(itemView) {
        val sentName: TextView? = itemView.findViewById(R.id.senderName)
        val sentmessage: TextView? = itemView.findViewById(R.id.message)
        val senttimestamp: TextView? = itemView.findViewById(R.id.sendtime)
    }


}

class ReceiveViewHolder(itemView: View) : ViewHolder(itemView) {

    val receiveName: TextView? = itemView.findViewById(R.id.receivename)
    val receivemessage: TextView? = itemView.findViewById(R.id.receivemessage)
    val receivetimestamp: TextView? = itemView.findViewById(R.id.receivetime)


}




