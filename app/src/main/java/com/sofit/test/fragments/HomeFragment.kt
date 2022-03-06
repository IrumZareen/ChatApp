package com.sofit.test.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.FirebaseDatabase
import com.sofit.test.R
import com.sofit.test.activities.HomeActivity
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
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setUpViewPager()
        registerListeners()
        return binding.root
    }

    private fun registerListeners() {
        binding.toolBar.ivMenu.setOnClickListener {
            (requireActivity() as HomeActivity).drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(
            arrayListOf(
                AllUsersFragment(),
                FriendListFragment()
            ),
            requireActivity().supportFragmentManager,
            lifecycle
        )
        val tabTitles = listOf(
            requireActivity().getString(R.string.all_users),
            requireActivity().getString(R.string.friends)
        )
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}