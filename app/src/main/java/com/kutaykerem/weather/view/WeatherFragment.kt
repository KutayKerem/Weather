package com.kutaykerem.weather.view

import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.kutaykerem.weather.R
import com.kutaykerem.weather.Model.Weather
import com.kutaykerem.weather.adapter.WeatherAdapter
import com.kutaykerem.weather.databinding.FragmentWeatherBinding
import com.kutaykerem.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@AndroidEntryPoint
class WeatherFragment : Fragment() {

    lateinit var viewModel : WeatherViewModel
    lateinit var binding : FragmentWeatherBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(layoutInflater,container,false)
        return binding.root


    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)


        val color = ContextCompat.getColor(requireContext(), R.color.black)
        binding.weatherProgressBar.indeterminateTintList = ColorStateList.valueOf(color)

        subsribeToObservers()



    }





    @RequiresApi(Build.VERSION_CODES.O)
    fun subsribeToObservers(){






        viewModel.getWeather.observe(viewLifecycleOwner, Observer {data ->
            data.let {
                binding.weatherProgressBar.visibility = View.GONE

                var newCityName=""
                if(data.name.contains("Province")){
                    newCityName = data.name.replace("Province","")
                }else{
                    newCityName = data.name
                }

                val bugun = LocalDate.now()
                val gun = bugun.dayOfWeek
                val gunIsmi = gun.getDisplayName(TextStyle.FULL, Locale("tr"))
                binding.infoDayName.text = gunIsmi
                binding.weatherCityName.text= newCityName


                binding.weatherHavaDerece.text = "${data.main.temp.toInt()}°C"
                binding.weatherSpeetText.text = "${data.wind.speed.toInt()} km/s"
                binding.infoDayMain.text = "${data.main.temp.toInt()}°C"
                binding.weatherHummudity.text = "%${data.main.humidity.toString()} "




                // https://openweathermap.org/weather-conditions



                viewModel.getForecastDataList.observe(viewLifecycleOwner, Observer { forecastData->
                    val iconId = forecastData[0].weather[0].icon
                    val imageUrl = "https://api.openweathermap.org/img/w/$iconId.png"

                    Glide.with(this).load(imageUrl).into(binding.weatherHavaImage)

                })





            }
            if(data.weather == null){
                viewModel.deleteAllWeather()
                viewModel.getDataWeatherAPI("istanbul")
                viewModel.getDataWeatherForecastAPI("istanbul")
            }




        })




        viewModel.weatherLoad.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                binding.weatherProgressBar.visibility = View.VISIBLE
                binding.linearDetails.visibility = View.GONE
                binding.linearInfo.visibility = View.GONE
                binding.weatherCityName.visibility = View.GONE
            }else
            {
                binding.linearDetails.visibility = View.VISIBLE
                binding.linearInfo.visibility = View.VISIBLE
                binding.weatherProgressBar.visibility = View.GONE
                binding.weatherCityName.visibility = View.VISIBLE

            }

        })



        viewModel.weatherError.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                binding.weatherProgressBar.visibility = View.GONE
                binding.weatherErrorText.visibility = View.VISIBLE
                binding.linearDetails.visibility = View.GONE
                binding.linearInfo.visibility = View.GONE
                binding.weatherErrorText.text = "Error!"
                binding.weatherCityName.visibility = View.GONE

            }else
            {
                binding.weatherErrorText.visibility = View.GONE
                binding.linearDetails.visibility = View.VISIBLE
                binding.linearInfo.visibility = View.VISIBLE


            }

        })



    }




}


