package com.kutaykerem.weather.view

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kutaykerem.weather.R
import com.kutaykerem.weather.adapter.WeatherAdapter
import com.kutaykerem.weather.databinding.FragmentWeatherBinding
import com.kutaykerem.weather.databinding.FragmentWeatherForecastBinding
import com.kutaykerem.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherForecastFragment : Fragment() {

    lateinit var viewModel : WeatherViewModel
    lateinit var binding : FragmentWeatherForecastBinding
    lateinit var permissionLauncher : ActivityResultLauncher<String>
    lateinit var weatherAdapter: WeatherAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherForecastBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)

        val color = ContextCompat.getColor(requireContext(), R.color.black)
        binding.weatherForecastProgressBar.indeterminateTintList = ColorStateList.valueOf(color)

        subsribeToObservers()

    }
    fun subsribeToObservers(){




        viewModel.getForecastDataList.observe(viewLifecycleOwner, Observer { data->

            data.let {
                var cityName =""
                var sunrise =""
                var sunset =""
                viewModel.getForecastList.observe(viewLifecycleOwner, Observer { data->
                    cityName = data.city.name
                    sunrise= data.city.sunrise.toString()
                    sunset = data.city.sunset.toString()
                })


                binding.recyclerViewWeather.layoutManager = LinearLayoutManager(requireContext())
                weatherAdapter = WeatherAdapter(data,requireContext(),cityName,sunrise,sunset)
                binding.recyclerViewWeather.adapter = weatherAdapter
            }
            if(data == null){
                viewModel.deleteAllWeather()
                viewModel.getDataWeatherAPI("istanbul")
                viewModel.getDataWeatherForecastAPI("istanbul")
            }




        })

        viewModel.weatherLoad.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                binding.weatherForecastProgressBar.visibility = View.VISIBLE
                binding.recyclerViewWeather.visibility = View.GONE
                binding.weatherForecastErrorText.visibility = View.GONE

            }else
            {
                binding.recyclerViewWeather.visibility = View.VISIBLE
                binding.weatherForecastProgressBar.visibility = View.GONE

            }

        })



        viewModel.weatherError.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                binding.recyclerViewWeather.visibility = View.GONE
                binding.weatherForecastProgressBar.visibility = View.GONE
                binding.weatherForecastErrorText.visibility = View.VISIBLE

            }else
            {
                binding.weatherForecastErrorText.visibility = View.GONE


            }

        })



    }





}