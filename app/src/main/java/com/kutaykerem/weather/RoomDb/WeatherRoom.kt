package com.kutaykerem.weather.RoomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherRoom(
    var cityName : String,
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
)