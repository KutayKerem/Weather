package com.kutaykerem.weather.repo

import androidx.lifecycle.LiveData
import com.kutaykerem.weather.RoomDb.WeatherDao
import com.kutaykerem.weather.RoomDb.WeatherRoom
import javax.inject.Inject

class WeatherReposity @Inject constructor(
    val weatherDao : WeatherDao
  ): WeatherReposityInterface {


    override suspend fun insertWeather(weatherRoom: WeatherRoom) {
        weatherDao.insertWeather(weatherRoom)
    }

    override suspend fun deleteWeather(weatherRoom: WeatherRoom) {
        weatherDao.deleteWeather(weatherRoom)
    }

    override suspend fun deleteAllWeather() {
        weatherDao.deleteAllWeather()
    }

    override fun getWeather(): LiveData<WeatherRoom> {
       return weatherDao.observeWeather()
    }
}