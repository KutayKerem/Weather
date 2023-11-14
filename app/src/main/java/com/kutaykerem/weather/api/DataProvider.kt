package com.kutaykerem.weather.api

import com.kutaykerem.weather.Model.ModelWeather
import io.reactivex.Single
import javax.inject.Inject

class DataProvider @Inject constructor(private val retrofitApi:Retrofit_APİ) {

    fun getData(cityName : String) : Single<ModelWeather> {
        return  retrofitApi.getWeather(cityName)
    }


}