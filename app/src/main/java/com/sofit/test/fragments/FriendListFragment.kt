package com.sofit.test.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sofit.test.R
import com.sofit.test.adapters.AllUserAndFriendAdapter
import com.sofit.test.databinding.FragmentFriendListBinding
import com.sofit.test.model.User


/**
 * A fragment to display the list of friends , this is one of the children of home fragment
 */
class FriendListFragment : Fragment(), AllUserAndFriendAdapter.OnUserItemClick {

    private lateinit var binding: FragmentFriendListBinding
    private lateinit var friendsAdapter: AllUserAndFriendAdapter
    private var friendList: MutableList<User> = ArrayList()

    private var firebaseDataBase = FirebaseDatabase.getInstance()
        .getReference("Users")     //firebase database instance to save data in real time database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFriendListBinding.inflate(layoutInflater, container, false)
        setAdapter()
        fetchData()
        return binding.root
    }

    private fun fetchData() {
        binding.progressBar.visibility = View.VISIBLE
        firebaseDataBase.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                friendList.clear()
                for (snapShot in snapshot.children) {
                    val user = snapShot.getValue(User::class.java)
                    if (user != null && user.email == FirebaseAuth.getInstance().currentUser?.email) {
                        if (!user.friends.isNullOrEmpty()) {
                            friendList.clear()
                            friendList.addAll(user.friends)
                        }
                    }
                }
                binding.progressBar.visibility = View.GONE
                if (friendList.size == 0) {
                    binding.tvNoFriend.visibility = View.VISIBLE
                    binding.rvFriendsList.visibility = View.GONE
                } else {
                    binding.tvNoFriend.visibility = View.GONE
                    binding.rvFriendsList.visibility = View.VISIBLE
                }
                friendsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireActivity(), "Error in fetching Data", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    private fun setAdapter() {
        friendsAdapter = AllUserAndFriendAdapter(friendList, this)
        binding.rvFriendsList.adapter = friendsAdapter
    }

    override fun onUserItemClick(item: User) {
        val bundle = Bundle()
        bundle.putSerializable("selectedFriend", item)
        findNavController().navigate(R.id.action_homeFragment_to_chatFragment, bundle)
    }
}