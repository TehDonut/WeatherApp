package com.example.myapplication.module

import com.example.myapplication.Env
import com.example.myapplication.api.GeoAPI
import com.example.myapplication.api.WeatherAPI
import com.example.myapplication.data.GeoRepository
import com.example.myapplication.data.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
class NetModule {

    @Provides
    fun providesWeatherAPI() : WeatherAPI  {
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
        return Retrofit.Builder().baseUrl(Env.WEATHER_API_BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).client(client).build().create(WeatherAPI::class.java)
    }

    @Provides
    fun providesWeatherRepository(api : WeatherAPI) : WeatherRepository = WeatherRepository(api)

    @Provides
    fun providesGeoAPI() : GeoAPI  {
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
        return Retrofit.Builder().baseUrl(Env.GEO_API_BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).client(client).build().create(GeoAPI::class.java)
    }

    @Provides
    fun providesGeoRepository(api : GeoAPI) : GeoRepository = GeoRepository(api)
}