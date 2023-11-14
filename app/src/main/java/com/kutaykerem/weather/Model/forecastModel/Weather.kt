package com.kutaykerem.weather.Model.forecastModel

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)