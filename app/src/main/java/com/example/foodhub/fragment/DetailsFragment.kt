package com.example.foodhub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentDetailsBinding
import com.example.foodhub.datamodel.retrofitdatamodel.Popular


class DetailsFragment : Fragment() {

    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var binding : FragmentDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //to fetch the product
        val product = args.popularProduct
        setTheData(product)

        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setTheData(product: Popular) {

        binding.apply {
            foodName.text= product.strMeal
            Glide
                .with(requireContext())
                .load(product.strMealThumb)
                .into(foodImage)

            tvDescDetails.text ="No description available"
            tvIngridentDetails.text="No Data available"
        }
    }
}


