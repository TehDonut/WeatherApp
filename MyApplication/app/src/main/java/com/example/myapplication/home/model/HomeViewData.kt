package com.example.myapplication.home.model

import com.example.myapplication.data.model.CurrentWeather

data class HomeViewData(
    val city : String,
    val temp : String,
    val iconCode: String,
    val feelsLike : String,
    val high : String,
    val low : String,
    val humidity: String,
    val wind : String,
    val visibility : String
) {
    companion object {
        fun fromWeatherResponse(response: CurrentWeather) : HomeViewData {
            return HomeViewData(
                city = response.name?.uppercase() ?: "",
                temp = response.main?.temp?.toString() ?: "",
                iconCode = response.weather?.firstOrNull()?.icon ?: "",
                feelsLike = response.main?.feelsLike?.toString() ?: "",
                high = response.main?.tempMax?.toString() ?: "",
                low = response.main?.tempMin?.toString() ?: "",
                humidity = response.main?.humidity?.toString() ?: "",
                wind = response.wind?.speed.toString() ?: "",
                visibility = response.visibility?.toString() ?: ""
            )
        }
    }
}




