package com.example.foodhub.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodhub.R
import com.example.foodhub.adapter.CartAdapter
import com.example.foodhub.databinding.FragmentCartBinding
import com.example.foodhub.datamodel.CartProduct
import com.example.foodhub.helper.Increase_Deacrease
import com.example.foodhub.helper.verticaldecorationRecyclerview
import com.example.foodhub.sealedclass.Resources
import com.example.foodhub.viewmodel.CartProdcutViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val viewmodel by viewModels<CartProdcutViewmodel>()
    private val cartAdapter by lazy {
        CartAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SetupRecyclerView()
        observeCartItem()
        cartAdapter.onClick = {

            val data = Bundle().apply {
                putParcelable("cartproduct", it)
            }
            findNavController().navigate(R.id.action_cartFragment_to_cartDetailFragment, data)
        }


        cartAdapter.onDeleteButtonClick = {
            showAlertDialogue(it)
        }

        cartAdapter.onPlusClick = {
                viewmodel.changeQuantity(it,Increase_Deacrease.quantityChanging.INCREASAE)
        }

        cartAdapter.onMinusClick = {
            viewmodel.changeQuantity(it,Increase_Deacrease.quantityChanging.DECREASE)

        }
    }

    private fun observeCartItem() {
        lifecycleScope.launchWhenStarted {
            viewmodel.cartproduct.collectLatest {
                when (it) {
                    is Resources.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.progressbar.visibility = View.INVISIBLE
                        if (it.data != null) {
                            binding.emptycartViewm.visibility = View.INVISIBLE
                            Log.d("data", it.data.toString())
                            cartAdapter.differ.submitList(it.data)
                        } else {
                            binding.emptycartViewm.visibility = View.VISIBLE
                            binding.Procceed.isEnabled = false
                        }
                    }

                    is Resources.Error -> {
                        binding.progressbar.visibility = View.INVISIBLE
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun SetupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = cartAdapter
            verticaldecorationRecyclerview()
        }
    }

    private fun showAlertDialogue(cartProduct: CartProduct) {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete Item From Cart")
                .setMessage("Do you want to delete this item form cart.")
                .setNegativeButton("Cancel") { dialogue, _ ->
                    dialogue.dismiss()
                }
                .setPositiveButton("Delete") { dialogue, _ ->
                    viewmodel.deleteItem(cartProduct)
                    dialogue.dismiss()
                }
        }
        alertDialog.create()
        alertDialog.show()
    }
}
