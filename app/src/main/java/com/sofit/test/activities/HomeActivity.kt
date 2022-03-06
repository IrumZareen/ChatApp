package com.sofit.test.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.sofit.test.R
import com.sofit.test.databinding.ActivityHomeBinding
import com.sofit.test.model.User
import com.sofit.test.services.DataStoreHandlerClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityHomeBinding
    lateinit var drawerLayout: DrawerLayout
    companion object{
        var currentUserName: String? = null
        var currentUserEmail: String? = null
        var currentUserProfilePic: String? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        drawerLayout = binding.drawerLayout
        getUserInfo()
        registerListeners()
        setContentView(binding.root)
    }

    private fun registerListeners() {
        binding.navView.setNavigationItemSelectedListener(this)
        /*  binding.toolBar.ivMenu.setOnClickListener {
              binding.drawerLayout.openDrawer(GravityCompat.START)
          }*/
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                // will uncomment this after populating data in profile and implementation of update profile functionality
                 val intent = Intent(this@HomeActivity, SetUpProfileActivity::class.java)
                 startActivity(intent)
            }
            R.id.nav_logout -> {
                logoutUser()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getUserInfo() {
        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreHandlerClass(this@HomeActivity).getUserFromPreferencesStore().catch { e ->
                e.printStackTrace()
            }.collect {
                withContext(Dispatchers.Main) {
                    currentUserName = it.name
                    currentUserEmail = it.email
                    currentUserProfilePic = it.profilePic
                }
            }
        }
    }

    private fun logoutUser() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val authStateListener =
            AuthStateListener { auth ->
                if (auth.currentUser == null) {
                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        firebaseAuth.addAuthStateListener(authStateListener)
        firebaseAuth.signOut()
    }
}