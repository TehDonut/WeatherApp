package com.example.myapplication.api

import com.example.myapplication.Env
import com.example.myapplication.data.model.Geo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoAPI {

    @GET("direct")
    suspend fun getLocationsQuery(@Query("q") query: String, @Query("limit") limit: Int = 20, @Query("appid") apiKey: String = Env.WEATHER_API_KEY) : Response<List<Geo>>
}