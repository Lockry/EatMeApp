package com.romeo.eatmeapp.ui.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.romeo.eatmeapp.AppApplication
import com.romeo.eatmeapp.MainActivity
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.databinding.FragmentMenuBinding
import com.romeo.eatmeapp.ui.adapters.CategoryAdapter
import com.romeo.eatmeapp.ui.adapters.MenuAdapter
import com.romeo.eatmeapp.ui.dialogs.InfoDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MenuViewModel by viewModels { viewModelFactory }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        // инжекция зависимости через AppComponent
        (requireActivity().application as AppApplication)
            .appComponent
            .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setupTopBar()

    }

    private fun setupTopBar() {
        lifecycleScope.launch {
            viewModel.selectedCategory.collect { category ->
                category?.let {
                    binding.topMenuBar.tVTypeMenu.text = it.name
                }
            }
        }
        binding.topMenuBar.btnHomeMenu.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_mainMenuFragment)
        }

        binding.topMenuBar.btnInfoMenu.setOnClickListener {
            InfoDialog.show(this, getString(R.string.dialog_menu_text))
        }

        binding.topMenuBar.btnCallWaiterMenu.setOnClickListener {
            InfoDialog.show(this, getString(R.string.dialog_btn_call_waiter_text))
        }
    }

    private fun setupMenu() {

        val menuAdapter = MenuAdapter(
            onDishClick = { dish ->
                AddToCartBottomFragment.newInstance(dish)
                    .show(
                        (context as FragmentActivity)
                            .supportFragmentManager, "AddToCartBottomSheet"
                    )
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
                menuAdapter.items = items
            }
        }

        binding.btnCart.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_cartFragment)
        }
    }

    private fun setupCategory() {
        // адаптер категорий
        val categoryAdapter = CategoryAdapter { selectedCategory ->
            viewModel.selectCategory(selectedCategory)

        }

        binding.rvCategoryMenu.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }

        lifecycleScope.launch {
            viewModel.categories.collect { categories ->
                categoryAdapter.items = categories
                categoryAdapter.selectFirstIfEmpty()

                categories.firstOrNull()?.name?.let { name ->
                    binding.topMenuBar.tVTypeMenu.text = name
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}