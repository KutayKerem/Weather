package com.kutaykerem.weather.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kutaykerem.weather.view.WeatherForecastFragment
import com.kutaykerem.weather.view.WeatherFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle:androidx.lifecycle.Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){

            0->{
                return WeatherFragment()
            }
            1->{
                return WeatherForecastFragment()

            }else-> {
                return WeatherFragment()
            }
        }


    }

}