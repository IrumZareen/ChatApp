package com.sofit.test.activities

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sofit.test.R
import com.sofit.test.databinding.ActivitySetUpProfileBinding
import com.sofit.test.fragments.CountryDialog
import com.sofit.test.model.CountryItem
import com.sofit.test.model.User
import com.sofit.test.viewmodel.CountriesViewModel
import kotlinx.coroutines.flow.collect

class SetUpProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetUpProfileBinding
    private lateinit var countriesViewModel: CountriesViewModel
    private var countryList = ArrayList<CountryItem>()

    private var firebaseStorage =
        FirebaseStorage.getInstance().reference // firebase storage instance to store image
    private var firebaseDataBase = FirebaseDatabase.getInstance()
        .getReference("Users")     //firebase database instance to save data in real time database

    private var imageUri: Uri? = null
    private var logoPathOfImage: String? =
        null // to store the path we will get from firebase storage after uploading image

    private val firebaseAuth = Firebase.auth
    private val currentUser = firebaseAuth.currentUser

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    imageUri = data.data
                    binding.civProfileImage.setImageURI(data.data)
                    uploadImageToFireBaseStorage()
                }

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUpProfileBinding.inflate(layoutInflater)
        initResources()
        setObservers()
        triggerCountryApi()
        registerListeners()
        setContentView(binding.root)
    }

    private fun triggerCountryApi() {
        countriesViewModel.getCountries()
    }

    private fun initResources() {
        binding.tvSelectCountry.text = getString(R.string.pakistan)
        countriesViewModel = ViewModelProvider(this).get(CountriesViewModel::class.java)
    }

    private fun setObservers() {
        lifecycleScope.launchWhenStarted {
            countriesViewModel.countriesDataState.collect {
                when {
                    it.loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        disableTouchEvents()
                    }
                    it.data != null -> {
                        binding.progressBar.visibility = View.GONE
                        enableTouchEvents()
                        countryList.clear()
                        countryList.addAll(it.data)
                    }
                    it.handleError != null -> {
                        binding.progressBar.visibility = View.GONE
                        enableTouchEvents()
                        Toast.makeText(
                            this@SetUpProfileActivity,
                            it.handleError.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun registerListeners() {
        binding.tvSelectCountry.setOnClickListener {
            showCountryDialog()
        }
        binding.btnUpdateInfo.setOnClickListener {
            if (validates()) {
                saveUserInformation()
            }
        }
        binding.civProfileImage.setOnClickListener {
            val imageIntent = Intent()
            imageIntent.apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
            }
            resultLauncher.launch(imageIntent)

        }
    }

    private fun showCountryDialog() {

        val cdd1 = CountryDialog(
            this,
            countryList
        ) {
            updateCountryName(it)
        }
        val window1 = cdd1.window
        val param1 = window1!!.attributes
        param1.gravity = Gravity.CENTER
        window1.attributes = param1
        window1.setBackgroundDrawableResource(android.R.color.transparent)
        cdd1.setCanceledOnTouchOutside(false)
        if (!this.isFinishing) this.runOnUiThread { cdd1.show() }
    }

    private fun updateCountryName(countryName: String) {
        binding.tvSelectCountry.text = countryName

    }

    private fun disableTouchEvents() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun enableTouchEvents() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun validates(): Boolean {
        if (binding.etName.text.isNullOrEmpty()) {
            binding.etName.error = getString(R.string.field_can_not_be_empty)
            return false
        }
        return true
    }

    private fun saveUserInformation() {

        if (currentUser != null) {
            val user = User(
                binding.etName.text.toString(),
                binding.tvSelectCountry.text.toString(),
                logoPathOfImage,
                currentUser.email!!,
                null
            )
            binding.progressBar.visibility = View.VISIBLE
            firebaseDataBase.push().key?.let {
                firebaseDataBase.child(it).setValue(user).addOnSuccessListener {
                    binding.progressBar.visibility = View.GONE
                    navigateToHomeScreen()
                }.addOnFailureListener {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@SetUpProfileActivity,
                        "Insertion Failed !!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this@SetUpProfileActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun uploadImageToFireBaseStorage() {
        if (imageUri != null) {
            val fileRef = firebaseStorage.child(
                System.currentTimeMillis().toString() + "." + getFileExtension(imageUri!!)
            )
            fileRef.putFile(imageUri!!).addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                fileRef.downloadUrl.addOnSuccessListener {
                    logoPathOfImage = it.toString()
                }
            }.addOnProgressListener {
                binding.progressBar.visibility = View.VISIBLE
            }.addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    this@SetUpProfileActivity,
                    "Uploading Failure !!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            Toast.makeText(this@SetUpProfileActivity, "Please Select Image", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getFileExtension(imageUri: Uri): String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getMimeTypeFromExtension(cr.getType(imageUri))
    }

}