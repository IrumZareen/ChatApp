package com.sofit.test.model

data class Message(
    val message: String? = null,
    val senderId: String? = null,
    val senderName: String? = null,
    val receiverId: String? = null,
    val receiverName: String? = null,
    var viewType : Int = 0
)
