package com.sofit.test.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.core.widget.doAfterTextChanged
import com.sofit.test.activities.SetUpProfileActivity
import com.sofit.test.adapters.CountryAdapter
import com.sofit.test.databinding.FragmentCountryListBinding
import com.sofit.test.model.CountryItem


/**
 *Country Dialog to show countries to user, from this dialog user can select country,
 *selected country will be displayed on profile set up screen
 */
class CountryDialog(
    context: SetUpProfileActivity,
    private val list: List<CountryItem>,
    private val selectCountry: (name: String) -> Unit
) : Dialog(context), CountryAdapter.OnCountryItemClickListener {

    private lateinit var binding: FragmentCountryListBinding
    private lateinit var countryAdapter: CountryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = FragmentCountryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        initResources()
        registerListeners()

    }

    private fun registerListeners() {
        binding.etSearchCountry.doAfterTextChanged {
            countryAdapter.filter.filter(it.toString())
        }
    }

    private fun initResources() {
        countryAdapter = CountryAdapter(list, this)
        binding.recyclerViewForCountries.adapter = countryAdapter
    }

    override fun onCountryItemClick(item: CountryItem) {
        dismiss()
        item.name?.let { selectCountry.invoke(it) }
    }


}