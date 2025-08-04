package com.romeo.eatmeapp.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.romeo.eatmeapp.AppApplication
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SplashScreenViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // инжекция зависимости через AppComponent
        (requireActivity().application as AppApplication)
            .appComponent
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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