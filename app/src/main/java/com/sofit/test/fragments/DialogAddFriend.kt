package com.sofit.test.fragments

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import com.sofit.test.databinding.DialogAddFriendBinding
import com.sofit.test.model.User

class DialogAddFriend(
    private val context: Activity,
    private val message: String,
    private val onYesClick: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogAddFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setMessage()
        registerListeners()
    }

    private fun setMessage() {
        binding.tvDialogMessage.text = message
    }


    private fun registerListeners() {
        binding.btnNo.setOnClickListener {
            dismiss()
        }
        binding.btnYes.setOnClickListener {
            onYesClick()
            dismiss()
        }
    }

    fun showDialog() {
        val window1 = window
        val param1 = window1!!.attributes
        param1.gravity = Gravity.CENTER
        window1.attributes = param1
        window1.setBackgroundDrawableResource(android.R.color.transparent)
        setCanceledOnTouchOutside(false)
        if (!context.isFinishing) context.runOnUiThread { show() }
    }
}