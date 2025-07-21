package com.romeo.eatmeapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.romeo.eatmeapp.MainActivity
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.data.network.RetrofitClient
import com.romeo.eatmeapp.data.repository.FakeRestaurantRepository
import com.romeo.eatmeapp.data.repository.RealRestaurantRepository
import com.romeo.eatmeapp.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SplashScreenViewModel

    private val isTestMode: Boolean
        get() = (activity as? MainActivity)?.isTestMode ?: false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = if (isTestMode) {
            FakeRestaurantRepository(requireContext())
        } else {
            RealRestaurantRepository(RetrofitClient.api)
        }

        val factory = SplashScreenViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SplashScreenViewModel::class.java]
        viewModel.loadSplashScreens()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val splashImage = binding.imageSplash

        lifecycleScope.launch {
            viewModel.ads.collect { splashList ->
                if (splashList.isEmpty()) return@collect

                var index = 0
                while (isActive) {
                    val imageUri = splashList[index % splashList.size].imageUri

                    splashImage.alpha = 0f

                    Glide.with(requireContext())
                        .load(imageUri)
                        .into(splashImage)

                    splashImage.animate().alpha(1f).setDuration(500).start()

                    delay(3000)

                    splashImage.animate().alpha(0f).setDuration(500).start()

                    delay(500)
                    index++
                }
            }
        }

        binding.imageSplash.setOnClickListener {
            findNavController().navigate(R.id.action_splash_to_mainMenu)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}