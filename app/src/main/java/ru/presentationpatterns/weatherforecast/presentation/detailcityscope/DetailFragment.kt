package ru.presentationpatterns.weatherforecast.presentation.detailcityscope

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.presentationpatterns.weatherforecast.App
import ru.presentationpatterns.weatherforecast.MainActivity
import ru.presentationpatterns.weatherforecast.R
import ru.presentationpatterns.weatherforecast.databinding.DetailFragmentBinding
import ru.presentationpatterns.weatherforecast.presentation.MusicService


class DetailFragment : Fragment() {
    private var _binding: DetailFragmentBinding? = null
    val binding get() = _binding!!
    var detailAdapter: DetailAdapter = DetailAdapter()
    var towm:String=""
    var temp=15.00
    var stateLookingHistory=false
    private var musicService: MusicService? = null
    private var isBound = false
    private val detailViewModel by viewModels<DetailViewModel>
    {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                DetailViewModel() as T
        }
    }
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailFragmentBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg: DetailFragmentArgs by navArgs()
        towm=arg.cityName
        val intent = Intent(requireContext(), MusicService::class.java)
        requireActivity().startService(intent)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            detailViewModel.getCityDao(arg.cityName)
            delay(1000)
            viewLifecycleOwner.lifecycleScope.launch {
                detailViewModel.cityWeatherLiveDataDetailFragment.observe(viewLifecycleOwner) { cityList ->
                    MainActivity.Panel.cityWeatherInfo = cityList
                    with(binding.cityDetail) {
                        adapter = detailAdapter.apply { refreshWeather(cityList) }
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                    }
                }
            }
        }
        binding.cityTitle.text = arg.cityName
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            if (MainActivity.Panel.cityWeatherInfo.isNotEmpty() && !MainActivity.Panel.readBackupFlag) {
                val lastWeatherInfo =
                    MainActivity.Panel.cityWeatherInfo[(MainActivity.Panel.cityWeatherInfo.size) - 1]
                temp=lastWeatherInfo.currentTempC
                binding.cityCurrentTemp.text =
                    "сейчас ${lastWeatherInfo.currentTempC}°С  завтра ${lastWeatherInfo.tomorrowTempC}°С "
                if (lastWeatherInfo.currentTempC > 5) {
//                    binding.star4.background=ResourcesCompat.getDrawable(getResources(), R.drawable.sunrise_desert, null)
                    binding.star4.setImageResource(R.drawable.sunrise_desert)
                } else {
                    binding.star4.setImageResource(R.drawable.winter)
                }
            } else {
                binding.cityCurrentTemp.text = "Режим просмотра температур "
            }
        }
        binding.deleteCity.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                async { detailViewModel.deleteCity(arg.cityName) }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(requireContext(), MusicService::class.java)
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            musicService?.startMusic(towm,temp,MainActivity.Panel.readBackupFlag)
        }, 1228)
    }

    override fun onPause() {
        super.onPause()
    }
    override fun onStop() {
        super.onStop()
        musicService?.stopMusic()
        requireActivity().unbindService(connection)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.cityDetail.adapter = null
        _binding = null
    }
}