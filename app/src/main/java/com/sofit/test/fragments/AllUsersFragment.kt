package com.sofit.test.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sofit.test.R
import com.sofit.test.databinding.FragmentAllUsersBinding


/**
 * Fragment to display all users, this is one of the children of home fragment
 */
class AllUsersFragment : Fragment() {

    private lateinit var binding: FragmentAllUsersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllUsersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}