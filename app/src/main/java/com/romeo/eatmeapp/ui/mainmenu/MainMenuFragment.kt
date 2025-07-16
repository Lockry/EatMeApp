package com.romeo.eatmeapp.ui.mainmenu

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
import com.romeo.eatmeapp.data.repository.MainMenuRepositoryImpl
import com.romeo.eatmeapp.databinding.FragmentMainMenuBinding
import com.romeo.eatmeapp.ui.adapters.MainMenuAdapter

class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    //фабрика
    private val viewModel: MainMenuViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = MainMenuRepositoryImpl()
                return MainMenuViewModel(repository) as T
            }
        }
    }

    private lateinit var adapter: MainMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = MainMenuAdapter { mainMenuItem ->
            Toast.makeText(requireContext(), "Нажали: ${mainMenuItem.name}", Toast.LENGTH_SHORT).show()
        }

        binding.rVBtnMenu.adapter = adapter
        binding.rVBtnMenu.layoutManager = CustomLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mainMenuItem.collect { list ->
                adapter.updateData(list)
            }
        }

        viewModel.loadMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
