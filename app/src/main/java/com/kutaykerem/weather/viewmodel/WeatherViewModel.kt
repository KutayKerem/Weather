package com.kutaykerem.weather.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kutaykerem.weather.Model.ModelWeather
import com.kutaykerem.weather.Model.forecastModel.Forecast
import com.kutaykerem.weather.Model.forecastModel.ForecastData
import com.kutaykerem.weather.R
import com.kutaykerem.weather.RoomDb.WeatherRoom
import com.kutaykerem.weather.api.DataProvider
import com.kutaykerem.weather.api.DataProviderForecast
import com.kutaykerem.weather.repo.WeatherReposityInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
 class WeatherViewModel @Inject constructor(
    private val dataProvider: DataProvider,
    private val dataProviderForecast: DataProviderForecast,
    private val reposity : WeatherReposityInterface
)  :ViewModel(){

    private val compositeDisposable= CompositeDisposable()

    val setWeather = MutableLiveData<ModelWeather>()
    val getWeather: LiveData<ModelWeather> get() = setWeather



    var setForecastDataList = MutableLiveData<ArrayList<ForecastData>>()
    var setForecast = MutableLiveData<Forecast>()
    val getForecastDataList: LiveData<ArrayList<ForecastData>> get() = setForecastDataList
    val getForecastList: LiveData<Forecast> get() = setForecast


    val weatherLoad = MutableLiveData<Boolean>()
    val weatherError =  MutableLiveData<Boolean>()
    val weatherGetName: LiveData<WeatherRoom> = reposity.getWeather()

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
   // var getLocationAddress: MutableLiveData<Address> = MutableLiveData()


    @SuppressLint("MissingPermission")
   suspend fun locationLoad(context: Context, boolean: Boolean) = viewModelScope.launch{
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (boolean){
            fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener {location->
                    location?.let {
                        val geocoder = Geocoder(context,Locale.getDefault())
                        val addresses  = geocoder.getFromLocation(location.latitude,location.longitude,1)
                        val locationAdress = addresses!![0]
                     //   getLocationAddress.value = addresses!![0]

                        val fullAdres = locationAdress.getAddressLine(0)
                        val country = locationAdress.countryCode // ülke kodu
                        val cityName = locationAdress.adminArea // şehir
                        val district = locationAdress.subAdminArea // ilçe
                        val neighborhood = locationAdress.subLocality // mahalle

                        deleteAllWeather()
                        makeWeather(district,context)
                        getDataWeatherAPI(district)
                        getDataWeatherForecastAPI(district)


                    }

                }.addOnFailureListener {error->
                    Log.d("Error:",error.stackTraceToString())
                }
        }



    }



    fun getDataWeatherAPI(cityName : String){
        weatherError.value = false
        weatherLoad.value = true



        compositeDisposable.add(
                dataProvider.getData(cityName)
                     .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<ModelWeather>(){
                        override fun onSuccess(list: ModelWeather) {
                            weatherLoad.value = false
                            setWeather.value = list

                        }

                        override fun onError(e: Throwable) {
                            weatherError.value = true
                            println(e.localizedMessage)
                            deleteAllWeather()
                        }

                    })

        )




    }
    fun getDataWeatherForecastAPI(cityName : String){
        weatherError.value = false
        weatherLoad.value = true


        compositeDisposable.add(

            dataProviderForecast.getData(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<Forecast>(){
                    override fun onSuccess(forecast: Forecast) {
                        val data = forecast
                        var forecastArray = arrayListOf<ForecastData>()
                        forecastArray = data.list as ArrayList<ForecastData>

                        setForecastDataList.value = forecastArray
                        setForecast.value = data

                    }

                    override fun onError(e: Throwable) {
                        weatherError.value = true
                        println(e.localizedMessage)
                    }



                })

        )

    }




    fun makeWeather(name:String,context: Context){
        if(name.isEmpty() ){
            Toast.makeText(context,"Enter your city name",Toast.LENGTH_SHORT).show()
        }else{
            val weather = WeatherRoom(name)

            insertWeather(weather)

        }

    }

    fun insertWeather(weatherRoom:WeatherRoom) = viewModelScope.launch{
        reposity.insertWeather(weatherRoom)
    }

    fun deleteWeather(weatherRoom: WeatherRoom)= viewModelScope.launch{
        reposity.deleteWeather(weatherRoom)

    }
    fun deleteAllWeather() = viewModelScope.launch {
        reposity.deleteAllWeather()
    }






    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}