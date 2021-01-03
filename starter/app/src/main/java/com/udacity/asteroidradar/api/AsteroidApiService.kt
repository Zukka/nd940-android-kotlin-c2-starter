package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Configs.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    fun getAsteroids(@Query("start_date") start_date: String,
                     @Query("api_key") api_key: String = API_KEY): Deferred<String>

    @GET("planetary/apod")
    fun getImageOfTheDay(@Query("api_key") api_key: String = API_KEY) : Deferred<ImageOfTheDay>
}

object Network {

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    val asteroidService = retrofit.create(AsteroidApiService::class.java)
}
