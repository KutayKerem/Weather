package com.kutaykerem.weather.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.kutaykerem.weather.RoomDb.WeatherDao
import com.kutaykerem.weather.RoomDb.WeatherDatabase
import com.kutaykerem.weather.Util.Util.BASE_URL
import com.kutaykerem.weather.api.DataProvider
import com.kutaykerem.weather.api.DataProviderForecast
import com.kutaykerem.weather.api.Retrofit_APİ
import com.kutaykerem.weather.repo.WeatherReposity
import com.kutaykerem.weather.repo.WeatherReposityInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule  {





    @Singleton
    @Provides
    fun providerRetrofitApi() : Retrofit_APİ {

         return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(Retrofit_APİ::class.java)

    }

    @Singleton
    @Provides
    fun providerData(retrofitApi : Retrofit_APİ) : DataProvider{
        return DataProvider(retrofitApi)
    }
    @Singleton
    @Provides
    fun providerDataForecast(retrofitApi : Retrofit_APİ) : DataProviderForecast{
        return DataProviderForecast(retrofitApi)
    }








    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context :Context) = Room.databaseBuilder(context,WeatherDatabase::class.java,"WeatherDataDB").build()

    @Singleton
    @Provides
    fun injectDao(database: WeatherDatabase) = database.weatherDao()

    @Singleton
    @Provides
    fun injectNormalRepo(dao:WeatherDao) = WeatherReposity(dao) as WeatherReposityInterface




}