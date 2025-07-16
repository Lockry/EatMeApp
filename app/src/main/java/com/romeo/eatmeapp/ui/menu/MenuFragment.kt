package com.romeo.eatmeapp.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.romeo.eatmeapp.data.network.RetrofitClient
import com.romeo.eatmeapp.data.repository.FakeMenuRepository
import com.romeo.eatmeapp.data.repository.FakeRestaurantLoader
import com.romeo.eatmeapp.data.repository.MenuDataSource
import com.romeo.eatmeapp.data.repository.MenuRepository
import com.romeo.eatmeapp.databinding.FragmentMenuBinding
import com.romeo.eatmeapp.ui.adapters.CategoryAdapter
import com.romeo.eatmeapp.ui.adapters.MenuAdapter
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MenuViewModel

    private var isTestMode: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val repository = if (isTestMode) {
            FakeMenuRepository(requireContext())
        } else {
            MenuRepository(RetrofitClient.api)
        }

        val factory = MenuViewModelFactory(repository as MenuDataSource)
        viewModel = ViewModelProvider(this, factory)[MenuViewModel::class.java]
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

        val restaurant = FakeRestaurantLoader.loadFakeRestaurant(requireContext())
        viewModel.setMenu(restaurant.menu)


        binding.topMenuBar.btnHomeMenu.setOnClickListener { /* TODO */ }
        binding.topMenuBar.btnInfoMenu.setOnClickListener { /* TODO */ }
        binding.topMenuBar.btnCallWaiterMenu.setOnClickListener { /* TODO */ }
    }

    private fun setupMenu() {
        // адаптер (блюда + подкатегории) ---
        val menuAdapter = MenuAdapter(
            onDishClick = { dish ->
                Toast.makeText(requireContext(), "Нажали на блюдо: ${dish.name}", Toast.LENGTH_SHORT).show()
            },
            onSubcategoryClick = { subcategory ->
                viewModel.selectSubcategory(subcategory)
            }
        )

        binding.rvMenu.layoutManager = GridLayoutManager(
            requireContext(),
            2)
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
            //binding.topMenuBar.btnCallWaiterMenu.setOnClickListener { /* TODO */ }
        binding.rvCategoryMenu.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }


        lifecycleScope.launch {
            viewModel.categories.collect { categories ->
                categoryAdapter.setData(categories)
            }
        }
    }

    companion object {
        private const val ARG_IS_TEST_MODE = "arg_is_test_mode"

        fun newInstance(isTestMode: Boolean): MenuFragment {
            val fragment = MenuFragment()
            val args = Bundle()
            args.putBoolean(ARG_IS_TEST_MODE, isTestMode)
            fragment.arguments = args
            return fragment
        }
    }


}