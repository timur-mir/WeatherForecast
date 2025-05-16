package ru.presentationpatterns.weatherforecast

import android.Manifest
import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import ru.presentationpatterns.weatherforecast.MainActivity.Panel.currentPlace
import ru.presentationpatterns.weatherforecast.MainActivity.Panel.netState
import ru.presentationpatterns.weatherforecast.MainActivity.Panel.temperature
import ru.presentationpatterns.weatherforecast.databinding.ActivityMainBinding
import ru.presentationpatterns.weatherforecast.domain.CityWeatherInfo
import java.net.URL

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var locationManager: LocationManager
    lateinit var geocoder: Geocoder
    private var gps_enabled = false
    private var network_enabled = false
    private val networkReceiver by lazy { NetworkReceiver() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val filter = IntentFilter().apply { addAction(ConnectivityManager.CONNECTIVITY_ACTION) }
        registerReceiver(
            networkReceiver,
            filter
        )
        geocoder = Geocoder(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val bottomNavigationView = binding.panelNavigationMain
        val navController = findNavController(R.id.fragCont)
//        val builder = AppBarConfiguration.Builder(navController.graph)
//        val appBarConfiguration = builder.build()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.searchFragment2,
                R.id.cityFragment2
            )
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        Panel.panelNav = bottomNavigationView
        refreshWeatherInfo()

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment2)
                }

                R.id.cityFragment -> {
                    navController.navigate(R.id.cityFragment2)
                }
            }
            true
        }


    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val needRationale: Boolean = ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (needRationale) {
                Toast.makeText(this, "Разрешение не одобренно", Toast.LENGTH_SHORT).show();
            } else {
                requestLocationPermission()
            }
        }
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10.0f,localListener())
        // locationManager.requestSingleUpdate (LocationManager.GPS_PROVIDER,localListener(),myLooper)
        updateLocation()

    }

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        val myLooper = Looper.myLooper()
        val myhandler = myLooper?.let { Handler(it) }
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.powerRequirement = Criteria.POWER_HIGH
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        if (gps_enabled && network_enabled) {
            locationManager.requestSingleUpdate(criteria, localListener(), myLooper)

            if (myhandler != null) {
                Handler().postDelayed({ locationManager.removeUpdates(localListener()) }, 120000)
            }
        } else {
            Toast.makeText(
                this,
                "Включите функцию определения местоположения для корректной работы виджета",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            updateLocation()
        } else {
            Toast.makeText(
                this, "Разрешения непредоставлены", Toast.LENGTH_SHORT
            ).show();
        }
    }

    private fun refreshWeatherInfo() {
        if (!netState) {
            val snackbar =
                Snackbar.make(binding.root, "Сеть недоступна", Snackbar.LENGTH_INDEFINITE)
            snackbar.setActionTextColor(Color.WHITE)
                .setBackgroundTint((Color.BLUE))
                .setAction("Понятно") {
                    snackbar.dismiss()
                }
                .show()
        }
        getLocation()
        val man = AppWidgetManager.getInstance(this)
        val ids = man.getAppWidgetIds(ComponentName(this, WeatherWidget::class.java))
        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(updateIntent)
    }

    private fun updateWidget() {
//        var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
//        val appWidgetManager = AppWidgetManager.getInstance(this)
//        val views = RemoteViews(this.packageName, R.layout.weather_widget)
//        views.setTextViewText(R.id.appwidget_text, ">>>>>>>")
//        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    private fun localListener(): LocationListener {
        return LocationListener { location ->
            getWeatherInLocationPlace(
                location.latitude,
                location.longitude
            )
        }
    }

    fun getWeatherInLocationPlace(latitude: Double, longitude: Double) {
        currentPlace = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        )?.get(0)?.adminArea.toString()
        Thread() {
            try {

                val allInfo = URL(
                    "https://api.open-meteo.com/v1/forecast?latitude=${latitude.toString()}" +
                            "&longitude=${longitude.toString()}&hourly=temperature_2m,weathercode"
                ).readText(Charsets.UTF_8)
                if (allInfo.isNotEmpty()) {
                    val hourly = JSONObject(allInfo).getJSONObject("hourly")
                    temperature = hourly.getJSONArray("temperature_2m").getDouble(13).toInt()
                    Thread.sleep(1000)
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Температура в полдень сегодня:${temperature}°С",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("ResponseL", temperature.toString())

                    }
                } else {
                    Thread.currentThread().interrupt()
                }
            } catch (e: InterruptedException) {
                Log.e("ErrorXYZ", e.message.toString())
            }

        }.start()
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(networkReceiver)
        } catch (e: IllegalArgumentException) {
            Log.e("Broadcast", "Приёмник проверки состоянии сети не зарегистрирован", e)
            Toast.makeText(
                this,
                "Приёмник проверки состоянии сети не зарегистрирован",
                Toast.LENGTH_SHORT
            )
                .show();
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 4444

        class NetworkReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                    val connectivity =
                        intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
                    if (connectivity) {
                        netState = false

                    }
                }

            }
        }
    }


    object Panel {
        var netState = true
        var currentPlace = ""
        var searchPlaсe = ""
        var temperature = 10
        var normalFoundPlaceState = false
        var readBackupFlag = false
        lateinit var panelNav: BottomNavigationView
        var cityWeatherInfo: List<CityWeatherInfo> = emptyList<CityWeatherInfo>()
    }
}

