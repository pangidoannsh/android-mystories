package com.pangidoannsh.mystories.data.api.payload

import com.google.gson.annotations.SerializedName

data class Login(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)
