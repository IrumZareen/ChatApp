package com.sofit.test.adapters

import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.sofit.test.databinding.ItemCountryBinding
import com.sofit.test.model.CountryItem
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter(
    private val countryList: List<CountryItem>,
    private val onCountryItemClickListener: OnCountryItemClickListener
) :
    RecyclerView.Adapter<CountryAdapter.ViewHolder>(), Filterable {

    private var countryFilterList = countryList

    class ViewHolder(val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCountryName.text = countryFilterList[position].name
        holder.binding.tvCountryName.setOnClickListener {
            onCountryItemClickListener.onCountryItemClick(countryFilterList[position])
        }
    }

    override fun getItemCount(): Int {
        return countryFilterList.size
    }

    interface OnCountryItemClickListener {
        fun onCountryItemClick(item: CountryItem)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                countryFilterList = if (charSearch.isEmpty()) {
                    countryList
                } else {
                    val resultList = ArrayList<CountryItem>()
                    for (country in countryList) {
                        if (country.name!!.lowercase(Locale.ROOT)
                                .startsWith(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(country)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<CountryItem>
                notifyDataSetChanged()
                // It is in my TO DO list to study that what can we use instead of
                // notifyDataSetChanged for different scenarios as it is showing warning now.
            }
        }
    }
}