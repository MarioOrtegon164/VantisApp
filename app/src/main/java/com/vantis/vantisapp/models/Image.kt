package com.vantis.vantisapp.models


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("url")
    val url: String
)