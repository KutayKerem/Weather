package com.kutaykerem.weather.api

import com.kutaykerem.weather.Model.ModelWeather
import com.kutaykerem.weather.Model.forecastModel.Forecast
import com.kutaykerem.weather.Model.forecastModel.ForecastData
import com.kutaykerem.weather.R
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class DataProviderForecast @Inject constructor(private val retrofitApi:Retrofit_APİ) {

    fun getData(cityName : String) : Single<Forecast> {
        return  retrofitApi.getWeatherForecast(cityName)
    }


}