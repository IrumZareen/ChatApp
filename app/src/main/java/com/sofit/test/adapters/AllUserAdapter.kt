package com.sofit.test.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sofit.test.R
import com.sofit.test.databinding.ItemUserBinding
import com.sofit.test.model.User
import com.squareup.picasso.Picasso

class AllUserAdapter(private val usersList: MutableList<User>) :
    RecyclerView.Adapter<AllUserAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvUserName.text = usersList[position].name
        Picasso.get().load(usersList[position].profilePic)
            .placeholder(R.drawable.ic_profile_pic_pace_holder)
            .error(R.drawable.ic_profile_pic_pace_holder)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }
}