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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())

        adapter = CartAdapter(viewModel)
        binding.rvCart.adapter = adapter

        viewModel.dishesCart.observe(viewLifecycleOwner) { cartList ->
            Log.d("CartFragment", "cartList updated: ${cartList.size}")
            adapter.submitList(cartList)
            binding.textViewDishCartPrice.text = viewModel.getTotalPrice().toString()
            if (cartList.isEmpty()) {
                clearedCartUI()
                binding.topMenuBar.tVTypeMenu.text = "Ваш заказ (0)"
            } else {
                binding.textViewEmptyMsg.visibility = View.GONE
            }
        }

        viewModel.loadCart()

        binding.btnCreateOrder.setOnClickListener {
            viewModel.clearCart()
            Toast.makeText(
                requireContext(),
                "Заказ принят!",
                Toast.LENGTH_SHORT
            ).show()

            //TODO отправляем на кассу бла бла бла
        }

        binding.topMenuBar.btnInfoMenu.visibility = View.GONE


        val btnClearCart = binding.topMenuBar.btnCallWaiterMenu
        val layoutParams = btnClearCart.layoutParams
        layoutParams.width = 350
        btnClearCart.layoutParams = layoutParams

        btnClearCart.text = "Очистить корзину"

        binding.topMenuBar.tVTypeMenu.text = "Ваш заказ (${viewModel.getTotalDishes()})"

        binding.topMenuBar.btnHomeMenu.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_menuFragment)
        }

        binding.topMenuBar.btnCallWaiterMenu.setOnClickListener {
            viewModel.clearCart()
        }

        binding.topMenuBar.btnHomeMenu.setImageResource(R.drawable.ic_back_btn)
    }

    fun clearedCartUI() {

        binding.textViewEmptyMsg.visibility = View.VISIBLE
        binding.textViewEmptyMsg.text = getString(R.string.cart_is_empty)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = CartFragment()
    }

}