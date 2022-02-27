package com.sofit.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sofit.test.adapters.AllUserAndFriendAdapter
import com.sofit.test.databinding.FragmentAllUsersBinding
import com.sofit.test.model.User


/**
 * Fragment to display all users, this is one of the children of home fragment
 */
class AllUsersFragment : Fragment(), AllUserAndFriendAdapter.OnUserItemClick {

    private lateinit var binding: FragmentAllUsersBinding
    private var firebaseDataBase = FirebaseDatabase.getInstance()
        .getReference("Users")     //firebase database instance to save data in real time database

    private var usersList: MutableList<User> = ArrayList()
    private var friendList: MutableList<User> = ArrayList()
    private lateinit var usersAdapter: AllUserAndFriendAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllUsersBinding.inflate(layoutInflater, container, false)
        fetchData()
        setAdapter()
        return binding.root
    }

    private fun setAdapter() {
        usersAdapter = AllUserAndFriendAdapter(usersList, this)
        binding.rvAllUsers.adapter = usersAdapter
    }

    private fun fetchData() {
        binding.progressBar.visibility = View.VISIBLE
        firebaseDataBase.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                for (snapShot in snapshot.children) {
                    val user = snapShot.getValue(User::class.java)
                    if (user != null && user.email != FirebaseAuth.getInstance().currentUser?.email) {
                        usersList.add(user)
                    } else if (user != null && user.email == FirebaseAuth.getInstance().currentUser?.email) {
                        if (!user.friends.isNullOrEmpty()) {
                            friendList.clear()
                            friendList.addAll(user.friends)
                        }
                    }
                }
                binding.progressBar.visibility = View.GONE
                usersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireActivity(), "Error in fetching Data", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    override fun onUserItemClick(item: User) {
        showDialog(item)
    }

    private fun showDialog(item: User) {
        DialogAddFriend(
            requireActivity(),
            "Do you want to Add ".plus(item.name).plus(" to your friend list?")
        ) {
            addUserAsFriend(item)
        }.showDialog()
    }

    private fun addUserAsFriend(item: User) {
        if (friendList.contains(item)) {
            Toast.makeText(
                requireActivity(),
                item.name + " is already your friend.",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            binding.progressBar.visibility = View.VISIBLE
            firebaseDataBase.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null && user.email == FirebaseAuth.getInstance().currentUser?.email) {
                        addFriend(snapshot, item)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    private fun addFriend(snapShot: DataSnapshot?, item: User) {
        friendList.add(item)
        firebaseDataBase.child(snapShot?.key!!).child("friends")
            .setValue(friendList)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireActivity(), "Added to your friend list", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    requireActivity(),
                    "Error while adding friend.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }
}