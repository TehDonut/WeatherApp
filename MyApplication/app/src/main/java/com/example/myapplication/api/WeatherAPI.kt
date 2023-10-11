package com.example.myapplication.api

import com.example.myapplication.Env
import com.example.myapplication.data.model.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {

    @GET("weather")
    suspend fun getWeather(@Query("lat") lat: Double, @Query("lon") long: Double, @Query("appid") apiKey: String = Env.WEATHER_API_KEY, @Query("units") unit: String = "imperial") : Response<CurrentWeather>
}