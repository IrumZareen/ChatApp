package com.sofit.test.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sofit.test.databinding.ItemLeftMessageBinding
import com.sofit.test.databinding.ItemRightMessageBinding
import com.sofit.test.model.Message

class MessageAdapter(var messageList: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            RightMessageViewHolder(
                ItemRightMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            LeftMessageViewHolder(
                ItemLeftMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (messageList[position].viewType == 0) {
            (holder as RightMessageViewHolder).bind(position)
        } else {
            (holder as LeftMessageViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return messageList[position].viewType
    }

    fun updateList(listOfMessages: MutableList<Message>) {
       messageList = listOfMessages
        notifyDataSetChanged()
    }

    inner class RightMessageViewHolder(private val holder: ItemRightMessageBinding) :
        RecyclerView.ViewHolder(holder.root) {
        fun bind(position: Int) {
           holder.tvMessage.text = messageList[position].message
        }
    }

    inner class LeftMessageViewHolder(private val holder: ItemLeftMessageBinding) :
        RecyclerView.ViewHolder(holder.root) {
        fun bind(position: Int) {
            holder.tvMessage.text = messageList[position].message
        }
    }
}