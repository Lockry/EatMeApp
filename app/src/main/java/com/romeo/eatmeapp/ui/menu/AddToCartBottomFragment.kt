package com.romeo.eatmeapp.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.romeo.eatmeapp.data.model.DishModel
import com.romeo.eatmeapp.databinding.FragmentDishDetailBottomBinding
import com.romeo.eatmeapp.ui.cart.CartFragmentViewModel


@Suppress("DEPRECATION")
class AddToCartBottomFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDishDetailBottomBinding? = null
    private val binding get() = _binding!!

    private var quantity = 1
    private lateinit var dish: DishModel

    lateinit var viewModel: CartFragmentViewModel


    override fun onStart() {
        super.onStart()

        // Растягиваем жоска фрагмент
        val dialog = dialog
        if (dialog != null) {
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
            bottomSheet?.let {
                // Устанавливаем высоту на весь экран
                val layoutParams = it.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                it.layoutParams = layoutParams

                // Устанавливаем поведение: STATE_EXPANDED
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[CartFragmentViewModel::class.java]
        dish = requireArguments().getParcelable(ARG_DISH)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDishDetailBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext())
            .load(dish.imageUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imageDish)

        binding.textDishName.text = dish.name
        binding.textDishDesc.text = dish.desc
        binding.textViewDishPrice.text = dish.price.toString()

        quantity = 1
        updateQuantityUI()

        binding.btnPlus.setOnClickListener {
            quantity++
            updateQuantityUI()
        }

        binding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantityUI()
            }
        }

        binding.btnAddToCart.setOnClickListener {
            viewModel.addToCart(dish.copy(quantity = quantity))
            dismiss()
        }
    }

    private fun updateQuantityUI() {
        binding.textCount.text = quantity.toString()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DISH = "arg_dish"

        fun newInstance(dish: DishModel): AddToCartBottomFragment {
            return AddToCartBottomFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DISH, dish)
                }
            }
        }
    }
}
