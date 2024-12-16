package ru.presentationpatterns.weatherforecast.presentation.detailcityscope

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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


class DetailFragment : Fragment() {
    private var _binding: DetailFragmentBinding? = null
    val binding get() = _binding!!
    var detailAdapter: DetailAdapter = DetailAdapter()
    var audioFon: MediaPlayer? = null
    private val detailViewModel by viewModels<DetailViewModel>
    {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                DetailViewModel() as T
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
            if (MainActivity.Panel.cityWeatherInfo.size != 0&&!MainActivity.Panel.readBackupFlag) {
                val lastWeatherInfo =
                    MainActivity.Panel.cityWeatherInfo[(MainActivity.Panel.cityWeatherInfo.size) - 1]
                binding.cityCurrentTemp.text =
                    "сегодня ${lastWeatherInfo.currentTempC}°С  завтра ${lastWeatherInfo.tomorrowTempC}°С "
            } else {
                binding.cityCurrentTemp.text ="Режим просмотра температур "
            }
        }
        binding.deleteCity.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                async { detailViewModel.deleteCity(arg.cityName) }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        audioFon = MediaPlayer.create(App.appContext, R.raw.nice)
        audioFon?.start()
        audioFon?.isLooping = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.cityDetail.adapter = null
        _binding = null
        audioFon?.apply {
            pause()
            reset()
            release()
        }.also { audioFon = null }
    }
}