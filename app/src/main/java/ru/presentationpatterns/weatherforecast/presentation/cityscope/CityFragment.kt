package ru.presentationpatterns.weatherforecast.presentation.cityscope

import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.presentationpatterns.weatherforecast.MainActivity
import ru.presentationpatterns.weatherforecast.databinding.CityFragmentBinding



class CityFragment : Fragment() {
    private var _binding: CityFragmentBinding? = null
    val binding get() = _binding!!

    var cityInStoreAdapter: CityInStoreAdapter =
        CityInStoreAdapter { cityName -> toDetailInfo(cityName) }
    private val viewModel by viewModels<CityViewModel>
    {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                CityViewModel() as T
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CityFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
              MainActivity.Panel.panelNav.menu[0].isChecked = true
               findNavController().navigate(ru.presentationpatterns.weatherforecast.R.id.searchFragment2)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getCities()
            MainActivity.Panel.readBackupFlag=true
            delay(1000)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.citiesWeatherLiveData.observe(viewLifecycleOwner) { cities ->
                    with(binding.cityInStoreRecycler) {
                        adapter = cityInStoreAdapter.apply { setCities(cities) }
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                    }
                }

            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.cityInStoreRecycler.adapter=null
        _binding = null
    }

    private fun toDetailInfo(cityNameOut: String) {
        val action=CityFragmentDirections.actionCityFragment2ToDetailFragment(cityNameOut)
        findNavController().navigate(action)
    }
}
