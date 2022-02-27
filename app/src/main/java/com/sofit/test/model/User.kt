package com.sofit.test.model

data class User(
    val name: String? = null,
    val country: String? = null,
    val profilePic: String? = null,
    val email: String? = null,
    val friends: MutableList<User>? = null
)
