package com.romeo.eatmeapp.ui.mainmenu

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.romeo.eatmeapp.AppApplication
import com.romeo.eatmeapp.MainActivity
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.RestaurantDataObject
import com.romeo.eatmeapp.adminpanel.AdminActivity
import com.romeo.eatmeapp.databinding.FragmentMainMenuBinding
import com.romeo.eatmeapp.ui.dialogs.InfoDialog
import kotlinx.coroutines.launch

class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private val isTestMode: Boolean
        get() = (activity as? AppApplication)?.isTestMode ?: false

    private var tapCount = 0
    private val requiredTaps = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RestaurantDataObject.restaurantModel.collect { data ->
                    val hasGameZone = data?.hasGameZone == true
                    binding.frameGames.visibility = if (hasGameZone) View.VISIBLE else View.GONE
                }
            }
        }

        binding.btnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_menuFragment)
        }

        binding.btnHowItMainMenu.setOnClickListener {
            InfoDialog.show(this, getString(R.string.dialog_main_menu_text))
        }

        binding.btnGames.setOnClickListener {}
        binding.btnPayment.setOnClickListener {}

        binding.btnCallWaiterMainMenu.setOnClickListener {
            InfoDialog.show(this, getString(R.string.dialog_btn_call_waiter_text))

        }

        binding.btnCartMainMenu.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_cartFragment2)
        }

        binding.btnAdminPanel.setOnClickListener {

            tapCount++
            if (tapCount >= requiredTaps) {
                val intent = Intent(requireContext(), AdminActivity::class.java).apply {
                    putExtra(AdminActivity.EXTRA_IS_TEST_MODE, isTestMode)
                }
                startActivity(intent)

                tapCount = 0
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
