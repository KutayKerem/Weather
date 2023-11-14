package com.kutaykerem.weather.api

import com.kutaykerem.weather.Model.ModelWeather
import com.kutaykerem.weather.Model.forecastModel.Forecast
import com.kutaykerem.weather.Util.Util.API_KEY_FORECAST
import com.kutaykerem.weather.Util.Util.API_KEY_WEATHER
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Retrofit_APİ {


    // 1 günlük
    @GET(API_KEY_WEATHER)
    fun getWeather(
        @Query("q") cityName : String,
    ): Single<ModelWeather>


    // 5 günlük
    @GET(API_KEY_FORECAST)
     fun getWeatherForecast(
        @Query("q") cityName : String,
        ): Single<Forecast>


}