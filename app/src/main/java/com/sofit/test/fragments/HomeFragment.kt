package com.sofit.test.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.sofit.test.adapters.ViewPagerAdapter
import com.sofit.test.databinding.FragmentHomeBinding


/**
 * A Simple Fragment having two child fragments to show friends list and users List.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        setUpViewPager()
        return binding.root
    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(
            arrayListOf<Fragment>(
                AllUsersFragment(),
                FriendListFragment()
            ),
            requireActivity().supportFragmentManager,
            lifecycle
        )
    }
}