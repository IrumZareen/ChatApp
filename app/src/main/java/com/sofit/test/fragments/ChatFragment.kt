package com.sofit.test.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sofit.test.R
import com.sofit.test.activities.HomeActivity
import com.sofit.test.adapters.MessageAdapter
import com.sofit.test.databinding.FragmentChatBinding
import com.sofit.test.model.Message
import com.sofit.test.model.User
import com.sofit.test.services.DataStoreHandlerClass
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass. Used for to send message and display messages

 */
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var selectedFriend: User
    private lateinit var messageAdapter: MessageAdapter
    private var listOfMessages: MutableList<Message> = ArrayList()
    private var firebaseDataBase = FirebaseDatabase.getInstance()
        .getReference("Messages")     //firebase database instance to save data in real time database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        selectedFriend = arguments?.getSerializable("selectedFriend") as User
        initResources()
        registerListeners()
        readMessage()
        setMessageAdapter()
        return binding.root
    }

    private fun initResources() {
        Picasso.get().load(selectedFriend.profilePic)
            .placeholder(R.drawable.ic_profile_pic_pace_holder)
            .error(R.drawable.ic_profile_pic_pace_holder)
            .into(binding.toolBar.civProfileImage)
        binding.toolBar.tvUserName.text = selectedFriend.name
    }

    private fun setMessageAdapter() {
        val manager = LinearLayoutManager(context)
        manager.stackFromEnd = true
        binding.rvChat.layoutManager = manager
        messageAdapter = MessageAdapter(listOfMessages)
        binding.rvChat.adapter = messageAdapter
    }

    private fun readMessage() {
        firebaseDataBase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfMessages.clear()
                for (snapShot in snapshot.children) {
                    val message = snapShot.getValue(Message::class.java)
                    if ((message?.senderId == HomeActivity.currentUserEmail && message?.receiverId == selectedFriend.email) ||
                        (message?.senderId == selectedFriend.email && message?.receiverId == HomeActivity.currentUserEmail)) {
                        if (message?.senderId == HomeActivity.currentUserEmail)
                            message?.viewType = 0
                        else
                            message?.viewType = 1
                        listOfMessages.add(message!!)
                    }
                }
                messageAdapter.updateList(listOfMessages)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireActivity(), "Error in fetching messages", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }


    private fun registerListeners() {
        binding.btnSend.setOnClickListener {
            if (!binding.etTypeMessage.text.isNullOrEmpty()) {
                sendMessage()
            }
        }
    }

    private fun sendMessage() {
        val message = Message(
            binding.etTypeMessage.text.toString(),
            HomeActivity.currentUserEmail,
            HomeActivity.currentUserName,
            selectedFriend.email,
            selectedFriend.name
        )

        firebaseDataBase.push().setValue(message).addOnSuccessListener {
            binding.etTypeMessage.text.clear()
        }.addOnFailureListener {
            Toast.makeText(requireActivity(), "Error sending message", Toast.LENGTH_SHORT).show()
        }

    }

}