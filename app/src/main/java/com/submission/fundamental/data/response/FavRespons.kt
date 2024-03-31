package com.submission.fundamental.data.response

import com.google.gson.annotations.SerializedName

data class FavRespons (
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,
)