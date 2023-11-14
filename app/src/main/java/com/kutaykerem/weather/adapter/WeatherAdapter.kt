package com.kutaykerem.weather.adapter

import android.content.Context
import android.os.Build
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kutaykerem.weather.Model.forecastModel.ForecastData
import com.kutaykerem.weather.R
import com.kutaykerem.weather.databinding.WeatherItemBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherAdapter(val forecastList : ArrayList<ForecastData>,val context: Context,val cityName:String,val sunrise : String ,val sunset : String) : RecyclerView.Adapter<WeatherAdapter.WeatherHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        val view = WeatherItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WeatherHolder(view)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        val currentItem = forecastList[position]
        holder.binding.apply {
            val imageIcon = currentItem.weather[0].icon
            val imageUrl = "https://api.openweathermap.org/img/w/$imageIcon.png"
            Glide.with(context).load(imageUrl).into(holder.binding.havaImageItem)

            holder.binding.havaDereceItem.text = "${currentItem.main.temp.toInt()} °C"
            holder.binding.aciklamaItem.text = "${currentItem.weather[0].description}"
            holder.binding.hummudityTextItem.text = "%${currentItem.main.humidity.toInt()}"
            holder.binding.speedTextItem.text = "${currentItem.wind.speed.toInt()}km/s"

            var newCityName=""
            if(cityName.contains("Province")){
                 newCityName = cityName.replace("Province","")
            }else{
                newCityName = cityName
            }

            holder.binding.cityNameItemText.text = newCityName


            holder.binding.timeTextItem.text = displayTime(currentItem.dt_txt)




        }


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayTime(dtTxt: String): CharSequence? {
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val output = DateTimeFormatter.ofPattern("MM-dd  HH:mm")
        val dateTime = LocalDateTime.parse(dtTxt,input)
        return output.format(dateTime)

    }
    override fun getItemCount(): Int {
        return forecastList.size
    }
    class WeatherHolder(val binding:WeatherItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

}