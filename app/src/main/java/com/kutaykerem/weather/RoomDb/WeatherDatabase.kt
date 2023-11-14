package com.kutaykerem.weather.RoomDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherRoom::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

}