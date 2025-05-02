package ru.presentationpatterns.weatherforecast.presentation.searchscope

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.presentationpatterns.weatherforecast.MainActivity
import ru.presentationpatterns.weatherforecast.databinding.SearchFragmentBinding


class SearchFragment : Fragment() {
    private var _binding: SearchFragmentBinding? = null
    val binding get() = _binding!!
    var cityNameAdapter: CityNameAdapter = CityNameAdapter(){searchTown(it)}
    var townSearchList = arrayListOf<String>()
    private val searchViewModel by viewModels<SearchViewModel>
    {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                SearchViewModel() as T
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            init()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        (binding.searchCityView as SearchView).setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(cityName: String): Boolean {
                if (cityName.length>1) {
                    searchTown(cityName)
                }
                        else
                        Toast.makeText(
                            requireContext(),
                            "Недостаточное количество символов для поиска ",
                            Toast.LENGTH_SHORT
                        ).show()
                        return true
                    }


            override fun onQueryTextChange(cityName: String?): Boolean {
                return true
            }
        }

        )

    }
    fun searchTown(cityName:String){
        MainActivity.Panel.readBackupFlag=false
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.getCityWeatherParam(cityName) { state ->
                if (state != null) {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        MainActivity.Panel.normalFoundPlaceState = true
                        Toast.makeText(
                            requireContext(),
                            "Посмотрите на ответ",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        if (MainActivity.Panel.normalFoundPlaceState) {
                            val action =
                                SearchFragmentDirections.actionSearchFragment2ToDetailFragment(
                                    cityName
                                )
                            findNavController().navigate(action)

                        }
                    }

                }
                else {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "По Вашему запросу ничего ненайдено",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
    }
    private fun init(){
        MainActivity.Panel.readBackupFlag=false
        townSearchList.clear()
        townSearchList.add("Москва")
        townSearchList.add("Санкт-Петербург")
        townSearchList.add("Норильск")
        townSearchList.add("Сочи")
        townSearchList.add("Иркутск")
        townSearchList.add("Нижний Новгород")
        townSearchList.add("Коломна")
        townSearchList.add("Ашхабад")
        townSearchList.add("Ташкент")
        townSearchList.add("Бишкек")
        townSearchList.add("Ереван")
        townSearchList.add("Тбилиси")
        townSearchList.add("Грозный")
        townSearchList.add("Махачкала")
        townSearchList.add("Алагир")
        townSearchList.add("Минск")

        with(binding.townsRecycler) {
            adapter = cityNameAdapter.apply { refreshTown(townSearchList) }
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
//        binding.cityRecycler.adapter=null
        _binding = null
    }

}