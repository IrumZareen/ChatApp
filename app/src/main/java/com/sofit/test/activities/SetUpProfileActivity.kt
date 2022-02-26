package com.sofit.test.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import com.sofit.test.R
import com.sofit.test.databinding.ActivitySetUpProfileBinding
import com.sofit.test.fragments.CountryDialog
import com.sofit.test.model.CountryItem

class SetUpProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetUpProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUpProfileBinding.inflate(layoutInflater)
        registerListeners()
        setContentView(binding.root)
    }

    private fun registerListeners() {
        binding.tvSelectCountry.setOnClickListener {
            showCountryDialog()
        }
    }

    private fun showCountryDialog() {

        val cdd1 = CountryDialog(
            this,
            listOf(
                CountryItem(name = "Pakistan"),
                CountryItem(name = "Pakistan"),
                CountryItem(name = "Pakistan")
            )
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

    private fun updateCountryName(countryName : String) {
        binding.tvSelectCountry.text = countryName

    }

}