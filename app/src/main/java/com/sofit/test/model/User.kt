package com.sofit.test.model

import java.io.Serializable

data class User(
    var name: String? = null,
    val country: String? = null,
    val profilePic: String? = null,
    var email: String? = null,
    val friends: MutableList<User>? = null
) : Serializable
