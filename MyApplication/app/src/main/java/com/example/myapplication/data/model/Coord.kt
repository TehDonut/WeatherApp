package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat") val lat: Double? = 0.0,
    @SerializedName("lon") val long: Double? = 0.0,
)