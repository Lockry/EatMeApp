package com.romeo.eatmeapp.ui.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.romeo.eatmeapp.R
import com.romeo.eatmeapp.databinding.FragmentCartBinding
import com.romeo.eatmeapp.ui.adapters.CartAdapter


class CartFragment : Fragment(R.layout.fragment_cart) {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CartFragmentViewModel
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CartFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupTopBar()
        setupObservers()
        setupListeners()

        viewModel.loadCart()
    }

    private fun setupRecyclerView() = with(binding.rvCart) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = CartAdapter(viewModel).also { this@CartFragment.adapter = it }
        (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }

    private fun setupObservers() {
        viewModel.dishesCart.observe(viewLifecycleOwner) { cartList ->
            Log.d("CartFragment", "cartList updated: ${cartList.size}")

            adapter.items = cartList
            updateCartTotal()

            if (cartList.isEmpty()) {
                showEmptyCart()
            } else {
                hideEmptyCart()
                showCartContainer()
            }

            updateTopBarCounter(cartList.sumOf { it.quantity })
        }
    }

    private fun setupListeners() = with(binding) {
        btnCreateOrder.setOnClickListener {
            viewModel.clearCart()
            hideCartContainer()
            showEmptyCart()
            Toast.makeText(requireContext(), "Заказ принят!", Toast.LENGTH_SHORT).show()
            // TODO: Отправить заказ на кассу или куда надо там надо
        }

        binding.btnBackToMenu.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_menuFragment)
        }
    }

    private fun showEmptyCart() {
        binding.cartContainerEmpty.apply {
            visibility = View.VISIBLE
        }
        binding.topMenuBar.btnCallWaiterMenu.visibility = View.INVISIBLE

    }
    private fun showCartContainer() {
        binding.cartContainer.apply {
            visibility = View.VISIBLE
        }
    }

    private fun hideCartContainer() {
        binding.cartContainer.apply {
            visibility = View.GONE
        }
    }

    private fun hideEmptyCart() {
        binding.cartContainerEmpty.visibility = View.GONE
    }

    private fun updateCartTotal() {
        binding.textViewDishCartPrice.text = "${viewModel.getTotalPrice()} ₽"
    }

    private fun updateTopBarCounter(totalDishes: Int) {
        binding.topMenuBar.tVTypeMenu.text = "Ваш заказ ($totalDishes)"
    }

    private fun setupTopBar() = with(binding.topMenuBar) {
        btnInfoMenu.visibility = View.GONE

        btnCallWaiterMenu.apply {
            layoutParams = layoutParams.apply { width = 350 }
            text = "Очистить корзину"
            setOnClickListener {
                viewModel.clearCart()
                showEmptyCart()
                hideCartContainer()
            }
        }

        btnHomeMenu.setImageResource(R.drawable.ic_back_btn)
        btnHomeMenu.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_menuFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
