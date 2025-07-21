package com.romeo.eatmeapp.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.romeo.eatmeapp.MainActivity
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.data.network.RetrofitClient
import com.romeo.eatmeapp.data.repository.FakeRestaurantRepository
import com.romeo.eatmeapp.data.repository.RealRestaurantRepository
import com.romeo.eatmeapp.databinding.FragmentMenuBinding
import com.romeo.eatmeapp.ui.adapters.CategoryAdapter
import com.romeo.eatmeapp.ui.adapters.MenuAdapter
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MenuViewModel

    private val isTestMode: Boolean
        get() = (activity as? MainActivity)?.isTestMode ?: false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val repository = if (isTestMode) {
            FakeRestaurantRepository(requireContext())
        } else {
            RealRestaurantRepository(RetrofitClient.api)
        }

        val factory = MenuViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MenuViewModel::class.java]
        viewModel.loadMenu()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        setupCategory()

        binding.topMenuBar.btnHomeMenu.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_mainMenuFragment)
        }

        binding.topMenuBar.btnInfoMenu.setOnClickListener { /* TODO */ }

        binding.topMenuBar.btnCallWaiterMenu.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_cartFragment)
        }

    }

    private fun setupMenu() {

        val menuAdapter = MenuAdapter(
            onDishClick = { dish ->
                AddToCartBottomFragment.newInstance(dish)
                    .show((context as FragmentActivity)
                        .supportFragmentManager, "AddToCartBottomSheet")
            },
            onSubcategoryClick = { subcategory ->
                viewModel.selectSubcategory(subcategory)
            }
        )

        binding.rvMenu.layoutManager = GridLayoutManager(
            requireContext(),
            2
        )
        binding.rvMenu.adapter = menuAdapter

        lifecycleScope.launch {
            viewModel.currentMenuItems.collect { items ->
                menuAdapter.setData(items)
            }
        }
    }

    private fun setupCategory() {
        // адаптер категорий
        val categoryAdapter = CategoryAdapter { selectedCategory ->
            viewModel.selectCategory(selectedCategory)
            binding.topMenuBar.tVTypeMenu.text = selectedCategory.name.toString()
        }

        binding.rvCategoryMenu.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }

        lifecycleScope.launch {
            viewModel.categories.collect { categories ->
                categoryAdapter.setData(categories, autoSelectFirst = true)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}