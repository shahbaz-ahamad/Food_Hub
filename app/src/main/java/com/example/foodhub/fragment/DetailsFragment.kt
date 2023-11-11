package com.example.foodhub.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentDetailsBinding
import com.example.foodhub.datamodel.CartProduct
import com.example.foodhub.datamodel.retrofitdatamodel.EachCategoryItem
import com.example.foodhub.datamodel.retrofitdatamodel.Popular
import com.example.foodhub.sealedclass.Resources
import com.example.foodhub.viewmodel.DetailsViewmodel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var binding: FragmentDetailsBinding
    private val viewmodel by viewModels<DetailsViewmodel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //form the category item
        val category = args.category


        updateUi(category)

        observeTheAdditionOfProductToCart()

        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonAddToCart.setOnClickListener {
            viewmodel.addUpdateProductInCart(
                CartProduct(
                    category.strMeal,
                    category.strMealThumb,
                    category.idMeal,
                    1
                )
            )

        }
    }



    private fun updateUi(category: EachCategoryItem) {

        binding.apply {
            foodName.text = category.strMeal
            Glide
                .with(requireContext())
                .load(category.strMealThumb)
                .into(foodImage)

            tvDescDetails.text = "No description available"
            tvIngridentDetails.text = "No Data available"
        }
    }

    private fun observeTheAdditionOfProductToCart() {
        lifecycleScope.launchWhenStarted {
            viewmodel.addtoCart.collectLatest {
                when (it) {
                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        showSnackBar(it.data.toString())

                    }

                    is Resources.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        showSnackBar(it.message.toString())

                    }

                    else -> Unit
                }
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message.toString(), Snackbar.LENGTH_SHORT).show()
    }


}


