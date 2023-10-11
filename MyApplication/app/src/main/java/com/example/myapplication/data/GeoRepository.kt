package com.example.myapplication.data

import com.example.myapplication.api.GeoAPI

import com.example.myapplication.data.model.Geo
import retrofit2.Response
import javax.inject.Inject

class GeoRepository @Inject constructor(val api: GeoAPI) {

    suspend fun getLocationsQuery(query: String) : Response<List<Geo>> {
        return api.getLocationsQuery(query)
    }
}
