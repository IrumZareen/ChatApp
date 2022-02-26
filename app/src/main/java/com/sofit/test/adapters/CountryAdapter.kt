package com.sofit.test.adapters

import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.sofit.test.databinding.ItemCountryBinding
import com.sofit.test.model.CountryItem

class CountryAdapter(
    private val countryList: List<CountryItem>,
    private val onCountryItemClickListener: OnCountryItemClickListener
) :
    RecyclerView.Adapter<CountryAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCountryName.text = countryList[position].name
        holder.binding.tvCountryName.setOnClickListener {
           onCountryItemClickListener.onCountryItemClick(countryList[position])
        }
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

   /* override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }*/
    interface OnCountryItemClickListener{
        fun onCountryItemClick(item : CountryItem)
    }
}