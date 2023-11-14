package com.kutaykerem.weather.view

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kutaykerem.weather.R
import com.kutaykerem.weather.Util.lightStatusBar
import com.kutaykerem.weather.Util.setFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                setFullScreen(window)
                lightStatusBar(window, false, false) // durum çubuğunu karanlık moda ayarlar

            }
            Configuration.UI_MODE_NIGHT_NO -> {
                setFullScreen(window)
                lightStatusBar(window, true, false) // durum çubuğunu karanlık moda ayarlar
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {

            }
        }
    }



}