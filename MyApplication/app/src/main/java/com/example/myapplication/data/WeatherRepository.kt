package com.example.myapplication.data

import com.example.myapplication.api.WeatherAPI
import com.example.myapplication.data.model.CurrentWeather
import retrofit2.Response
import javax.inject.Inject


class WeatherRepository @Inject constructor(val api: WeatherAPI) {

    suspend fun getCurrentWeather(lat: Double, long: Double) : Response<CurrentWeather> {
        return api.getWeather(lat, long)
    }
}