package com.vantis.vantisapp.models


import com.google.gson.annotations.SerializedName

data class Powerstats(
    @SerializedName("combat")
    var combat: String,
    @SerializedName("durability")
    var durability: String,
    @SerializedName("intelligence")
    var intelligence: String,
    @SerializedName("power")
    var power: String,
    @SerializedName("speed")
    var speed: String,
    @SerializedName("strength")
    var strength: String
)