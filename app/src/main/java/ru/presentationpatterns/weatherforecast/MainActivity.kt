package ru.presentationpatterns.weatherforecast

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.presentationpatterns.weatherforecast.databinding.ActivityMainBinding
import ru.presentationpatterns.weatherforecast.domain.CityWeatherInfo
import ru.presentationpatterns.weatherforecast.presentation.WeatherViewModel


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView = binding.panelNavigationMain
        val navController = findNavController(R.id.fragCont)
        bottomNavigationView.setupWithNavController(navController)
        Panel.panelNav=bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.searchFragment-> {
                  navController.navigate(R.id.searchFragment2)
                }
                R.id.cityFragment -> {
                  navController.navigate(R.id.cityFragment2)
                }
            }
            true
        }

    }

    object Panel{
        var normalFoundPlaceState=false
        var readBackupFlag=false
        lateinit var panelNav: BottomNavigationView
        var cityWeatherInfo:List<CityWeatherInfo> = emptyList<CityWeatherInfo>()
    }
}

