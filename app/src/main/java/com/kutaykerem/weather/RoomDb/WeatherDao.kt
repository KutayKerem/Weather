package com.kutaykerem.weather.RoomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {

    @Insert
    suspend fun insertWeather(weatherRoom: WeatherRoom)

    @Delete
    suspend fun deleteWeather(weatherRoom: WeatherRoom)

    @Query("DELETE FROM weather")
    suspend fun deleteAllWeather()

    @Query("SELECT * FROM weather")
    fun observeWeather() : LiveData<WeatherRoom>



}