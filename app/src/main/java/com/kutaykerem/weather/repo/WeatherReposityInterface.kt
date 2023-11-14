package com.kutaykerem.weather.repo

import androidx.lifecycle.LiveData
import com.kutaykerem.weather.RoomDb.WeatherRoom

interface WeatherReposityInterface {

    suspend fun insertWeather(weatherRoom: WeatherRoom)

    suspend fun deleteWeather(weatherRoom: WeatherRoom)

    suspend fun deleteAllWeather()

    fun getWeather() : LiveData<WeatherRoom>


}