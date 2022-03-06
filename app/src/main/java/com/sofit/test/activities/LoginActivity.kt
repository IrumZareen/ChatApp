package com.sofit.test.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.sofit.test.R
import com.sofit.test.databinding.ActivityLoginBinding
import com.sofit.test.model.User
import com.sofit.test.services.DataStoreHandlerClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 *Login Activity , will be used for both sign in and sign up functionalities.
 * On the basis of text of the button user can sign in or sign up the account.
 **/


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        initResources()
        registerListeners()
        setContentView(binding.root)
    }


    private fun initResources() {
        firebaseAuth = Firebase.auth
        binding.tvAccountStatus.text = getString(R.string.dont_have_account)
    }


    private fun registerListeners() {
        binding.btnLogin.setOnClickListener {
            if (validates()) {
                if (binding.btnLogin.text == getString(R.string.login)) {
                    loginUser("")
                } else {
                    createNewUser()
                }
            }
        }
        binding.tvAccountStatus.setOnClickListener {
            toggleText()
        }
    }

    private fun loginUser(s: String) {
        firebaseAuth.signInWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigating user to home screen
                    if (s == getString(R.string.from_profile)) {
                        val intent = Intent(this@LoginActivity, SetUpProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        storeDataOfUserInDataStore()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun storeDataOfUserInDataStore() {
        FirebaseDatabase.getInstance().getReference("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapShot in snapshot.children) {
                        val user = dataSnapShot.getValue(User::class.java)
                        if (user?.email == binding.etEmail.text.toString()) {
                            saveData(user,binding.etEmail.text.toString())
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error in saving user data",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun saveData(user: User?, email: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreHandlerClass(this@LoginActivity).saveUserToPreferencesStore(
                user = User(
                    name = user?.name,
                    email = email,
                    profilePic = user?.profilePic,
                    country = user?.country
                )
            )
        }
        //navigate to home activity after storing data
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createNewUser() {
        firebaseAuth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, we will navigate to profile screen to get user info from user
                    loginUser(getString(R.string.from_profile))

                } else {
                    // If the case of failure, we will display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun toggleText() {
        if (binding.tvAccountStatus.text == getString(R.string.already_have_account)) {
            binding.tvAccountStatus.text = getString(R.string.dont_have_account)
            binding.btnLogin.text = getString(R.string.login)
            binding.etEmail.text?.clear()
            binding.etPassword.text?.clear()
        } else if (binding.tvAccountStatus.text == getString(R.string.dont_have_account)) {
            binding.tvAccountStatus.text = getString(R.string.already_have_account)
            binding.btnLogin.text = getString(R.string.sign_up)
            binding.etEmail.text?.clear()
            binding.etPassword.text?.clear()

        }
    }

    private fun validates(): Boolean {
        if (binding.etEmail.text.isNullOrEmpty()) {
            binding.etEmail.error = getString(R.string.field_can_not_be_empty)
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString())
                .matches()
        ) {
            binding.etEmail.error = getString(R.string.enter_valid_email)
            return false
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            binding.etPassword.error = getString(R.string.field_can_not_be_empty)
            return false
        }
        return true

    }


}