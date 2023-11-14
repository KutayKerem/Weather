package com.kutaykerem.weather.api
import com.kutaykerem.weather.Model.ModelWeather
import com.kutaykerem.weather.Util.Util.BASE_URL
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// Hilt kullanmasaydık direk burdan viewmodel de çekecektik
class CountryAPIService {

/*
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Retrofit_APİ::class.java)


    // Single tek seferlik
    fun getData(): Single<ModelWeather>{
        return api.getWeather()
    }
*/





}