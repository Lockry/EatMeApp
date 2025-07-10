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
import com.romeo.eatmeapp.data.model.CategoryModel
import com.romeo.eatmeapp.databinding.FragmentMenuBinding
import com.romeo.eatmeapp.ui.adapters.CategoryAdapter
import com.romeo.eatmeapp.ui.adapters.DishAdapter
import com.romeo.eatmeapp.ui.adapters.SubcategoryAdapter

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MenuViewModel::class.java]
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

        binding.topMenuBar.btnHomeMenu.setOnClickListener {}
        binding.topMenuBar.btnInfoMenu.setOnClickListener {}
        binding.topMenuBar.btnCallWaiterMenu.setOnClickListener {}

        binding.rvCategoryMenu.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
//        binding.rvSubmenu.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.rvMenu.adapter = subcategoryAdapter

        val categoryAdapter = CategoryAdapter {
            viewModel.selectCategory(it)

            Toast.makeText(requireContext(),
                "Нажата категория: ${it.name}",
                Toast.LENGTH_SHORT).show()
        }
        binding.rvCategoryMenu.adapter = categoryAdapter

        val dishesAdapter = DishAdapter {
            Toast.makeText(requireContext(),
                "Нажата категория: ${it.name}",
                Toast.LENGTH_SHORT).show()
        }

        val subcategoryAdapter = SubcategoryAdapter {
            viewModel.selectSubcategory(it)
        }



        lifecycleScope.launchWhenStarted {
            viewModel.currentSubcategories.collect {
                if (it.isNotEmpty()) {
                  //  categoryAdapter.setData(it)
                    dishesAdapter.setData(emptyList())
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.currentDishes.collect {
                if (it.isNotEmpty()) {
                    dishesAdapter.setData(it)
                    subcategoryAdapter.setData(emptyList())
                }
            }
        }

        viewModel.setCategories(getFakeCategories())
    }

    fun getFakeCategories(): List<CategoryModel> {
        return listOf(
            CategoryModel("1", "Горячие блюда", "https://i.imgur.com/GjtTrlr.png", listOf()),
            CategoryModel("2", "Закуски", "https://i.imgur.com/Vc6I4G5.png", listOf()),
            CategoryModel("3", "Салаты", "https://i.imgur.com/kXnxzlI.jpeg", listOf()),
            CategoryModel("4", "Пиццы", "https://i.imgur.com/jGuZnFJ.png", listOf()),
            CategoryModel("5", "Напитки", "https://i.imgur.com/jyoifeP.png", listOf())
        )
    }


    companion object {
        fun newInstance() = MenuFragment()
    }

}