package com.romeo.eatmeapp.ui.mainmenu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.romeo.eatmeapp.MainActivity
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.adminpanel.AdminActivity
import com.romeo.eatmeapp.data.network.RetrofitClient
import com.romeo.eatmeapp.data.repository.FakeRestaurantRepository
import com.romeo.eatmeapp.data.repository.RealRestaurantRepository
import com.romeo.eatmeapp.databinding.FragmentMainMenuBinding
import kotlinx.coroutines.launch

class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainMenuViewModel

    private val isTestMode: Boolean
        get() = (activity as? MainActivity)?.isTestMode ?: false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = if (isTestMode) {
            FakeRestaurantRepository(requireContext())
        } else {
            RealRestaurantRepository(RetrofitClient.api)
        }

        val factory = MainMenuViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainMenuViewModel::class.java]
        viewModel.loadMainMenu()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        viewLifecycleOwner.lifecycleScope.launch { // TODO придумать как оптимизировать UI при gameZone = true
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gameZone.collect { hasGameZone ->
                    binding.frameGames.visibility = if (hasGameZone) View.VISIBLE else View.GONE
                }
            }
        }

        binding.btnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_menuFragment)
        }

        binding.btnGames.setOnClickListener {
            Toast.makeText(requireContext(),
                "Игоры будут потом",
                Toast.LENGTH_SHORT).show()
        }

        binding.btnPayment.setOnClickListener {
            Toast.makeText(requireContext(),
                "Оплата будет потом",
                Toast.LENGTH_SHORT).show()
        }


        binding.btnCartMainMenu.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_cartFragment2)
        }

        binding.btnCallWaiterMainMenu.setOnClickListener {
            val intent = Intent(requireContext(), AdminActivity::class.java).apply {
                putExtra(AdminActivity.EXTRA_IS_TEST_MODE, isTestMode)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
