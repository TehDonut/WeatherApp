package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName


data class Geo (
    @SerializedName("name") val name : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("lon") val long : Double,
    @SerializedName("country") val country : String,
    @SerializedName("state") val state : String?
)