package com.sofit.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sofit.test.adapters.AllUserAdapter
import com.sofit.test.databinding.FragmentAllUsersBinding
import com.sofit.test.model.User


/**
 * Fragment to display all users, this is one of the children of home fragment
 */
class AllUsersFragment : Fragment() {

    private lateinit var binding: FragmentAllUsersBinding
    private var firebaseDataBase = FirebaseDatabase.getInstance()
        .getReference("Users")     //firebase database instance to save data in real time database

    private var usersList: MutableList<User> = ArrayList()
    private lateinit var usersAdapter : AllUserAdapter
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
        usersAdapter = AllUserAdapter(usersList)
        binding.rvAllUsers.adapter = usersAdapter
    }

    private fun fetchData() {
        binding.progressBar.visibility = View.VISIBLE
        firebaseDataBase.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
              for(snapShot in snapshot.children){
                  val user = snapShot.getValue(User::class.java)
                  if (user != null && user.email != FirebaseAuth.getInstance().currentUser?.email) {
                      usersList.add(user)
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

}