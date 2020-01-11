package com.example.weatherapp.Interfaces

import WeatherDataClass
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherService {

    @Headers("X-Yandex-API-Key:42427c83-abc2-4757-99e9-062beb4398af")
    @GET("v1/forecast")
    fun hitCountCheck(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Observable<WeatherDataClass>

    companion object {
        fun create(): WeatherService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl("https://api.weather.yandex.ru")
                .build()

            return retrofit.create(WeatherService::class.java)
        }
    }
}