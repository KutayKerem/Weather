package com.kutaykerem.weather.view

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kutaykerem.weather.Model.Weather
import com.kutaykerem.weather.R
import com.kutaykerem.weather.adapter.ViewPagerAdapter
import com.kutaykerem.weather.adapter.WeatherAdapter
import com.kutaykerem.weather.databinding.FragmentHomeBinding
import com.kutaykerem.weather.databinding.FragmentWeatherBinding
import com.kutaykerem.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime



@AndroidEntryPoint
class Fragment_Home : Fragment() {

    lateinit var binding : FragmentHomeBinding
    lateinit var viewPagerAdapter : ViewPagerAdapter

    lateinit var viewModel : WeatherViewModel
    lateinit var permissionLauncher : ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)

        if(requireActivity().supportFragmentManager != null){

            viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager,lifecycle)
            binding.homeViewpager2.adapter = viewPagerAdapter


            TabLayoutMediator(binding.homeTabLayout,binding.homeViewpager2){tab,position ->
                when (position){
                    0->{
                        tab.text = "Weather"

                    }
                    1->{
                        tab.text = "5 Day Weather "
                    }
                    else->{
                        tab.text = "Weather"
                    }
                }

            }.attach()

        }




        main()
        subsribeToObservers()

        binding.homeSearchImage.setOnClickListener {
            var cityName = binding.homeSearchEditText.text.toString()

            if(cityName != null){
                viewModel.deleteAllWeather()
                viewModel.makeWeather(cityName,requireContext())
                viewModel.getDataWeatherAPI(cityName)
                viewModel.getDataWeatherForecastAPI(cityName)
            }else{

            }
        }
        binding.homeSearchEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                var cityName = binding.homeSearchEditText.text.toString()
                viewModel.deleteAllWeather()
                viewModel.makeWeather(cityName,requireContext())
                viewModel.getDataWeatherAPI(cityName)
                viewModel.getDataWeatherForecastAPI(cityName)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        registerLauncher()
        getLocation()





    }

    fun subsribeToObservers(){
        viewModel.weatherGetName.observe(viewLifecycleOwner, Observer { data->
            data?.let {
                viewModel.getDataWeatherAPI(data.cityName)
                viewModel.getDataWeatherForecastAPI(data.cityName)
            }
            if(data == null){
                viewModel.deleteAllWeather()
                viewModel.getDataWeatherAPI("istanbul")
                viewModel.getDataWeatherForecastAPI("istanbul")
            }


        })


    }
    @SuppressLint("MissingPermission")
    fun getLocation(){



        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)){

                Snackbar.make(binding.root,"Permission needed for location", Snackbar.LENGTH_LONG).setAction("Give Permission"){
                    permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    lifecycleScope.launch {

                        viewModel.locationLoad(requireContext(),true)
                    }
                }.show()
            }else{
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                lifecycleScope.launch {

                    viewModel.locationLoad(requireContext(),true)
                }

            }

        }else{

            lifecycleScope.launch {

                viewModel.locationLoad(requireContext(),true)

            }


            // Bu işlemleri viewModel de yapıyoruz bu şekilde kullanıcının arayüzü daha hızlı şekilde çalışacaktır.
            /*
                        fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener {location->
                                location?.let {
                                  val geocoder = Geocoder(context,Locale.getDefault())
                                    val addresses  = geocoder.getFromLocation(location.latitude,location.longitude,1)
                                    val locationAdress = addresses!![0]

                                    val fullAdres = locationAdress.getAddressLine(0)
                                    val country = locationAdress.countryCode // ülke kodu
                                    val cityName = locationAdress.adminArea // şehir
                                    val district = locationAdress.subAdminArea // ilçe
                                    val neighborhood = locationAdress.subLocality // mahalle

                                    deleteAllWeather()
                                    makeWeather(district,context)
                                    getDataWeatherAPI(district)

                              }

                            }.addOnFailureListener {error->
                                Log.d("Error:",error.stackTraceToString())
                            }
                        */


        }
    }



    fun  registerLauncher(){

        // izin verildiyse olacaklar verilmediyse olacaklar
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->
            if(result){
                if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    lifecycleScope.launch {

                        viewModel.locationLoad(requireContext(),true)
                    }

                }
            }else{
                Toast.makeText(requireContext(),"Permission Granted!", Toast.LENGTH_LONG).show()
            }

        }


    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun main(){
        val aksamStart: LocalTime = LocalTime.of(18, 0)
        val aksamEnd: LocalTime = LocalTime.of(23, 0)

        val geceStart: LocalTime = LocalTime.of(23, 0)
        val geceEnd: LocalTime = LocalTime.of(5, 0)

        val sabahStart: LocalTime = LocalTime.of(5, 0)
        val sabahEnd: LocalTime = LocalTime.of(10, 0)

        val gunStart: LocalTime = LocalTime.of(10, 0)
        val gunEnd: LocalTime = LocalTime.of(18, 0)

        val currentTime : LocalTime = LocalDateTime.now().toLocalTime()


        when{
            currentTime.isAfter(aksamStart) && currentTime.isBefore(aksamEnd) ->
                binding.homeDurumText.text = "İyi Akşamlar"

            currentTime.isAfter(geceStart) && currentTime.isBefore(geceEnd) ->
                binding.homeDurumText.text = "İyi Geceler"
            currentTime.isAfter(sabahStart) && currentTime.isBefore(sabahEnd) ->
                binding.homeDurumText.text = "Günaydın"
            currentTime.isAfter(gunStart) && currentTime.isBefore(gunEnd) ->
                binding.homeDurumText.text = "İyi Günler"
            else -> {

            }

        }




    }

}