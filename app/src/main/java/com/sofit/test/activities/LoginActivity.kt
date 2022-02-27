package com.sofit.test.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sofit.test.R
import com.sofit.test.databinding.ActivityLoginBinding


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
                    loginUser()
                } else {
                    createNewUser()
                }
            }
        }
        binding.tvAccountStatus.setOnClickListener {
            toggleText()
        }
    }

    private fun loginUser() {
        firebaseAuth.signInWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigating user to home screen
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun createNewUser() {
        firebaseAuth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, we will navigate to profile screen to get user info from user
                    val intent = Intent(this@LoginActivity, SetUpProfileActivity::class.java)
                    startActivity(intent)
                    finish()

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