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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodhub.R
import com.example.foodhub.adapter.CategoryAdapter
import com.example.foodhub.datamodel.retrofitdatamodel.Popular
import com.example.foodhub.databinding.FragmentHomeBinding
import com.example.foodhub.helper.HorizontalItemDecorationForCatrgory
import com.example.foodhub.helper.verticaldecorationRecyclerview
import com.example.foodhub.sealedclass.Resources
import com.example.foodhub.viewmodel.MealViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private lateinit var binding:FragmentHomeBinding
    private lateinit var imageList : ArrayList<SlideModel>
    private lateinit var data : List<Popular>
    private val adapterCategory :CategoryAdapter by lazy {
        CategoryAdapter()
    }
    private val mealViewmodel by viewModels<MealViewmodel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageList  = ArrayList()
        data = ArrayList()

        setupImageSlider()
        setupRecylerView()

        //observe category
        observeCategory()
        //click event on imageslider
        binding.imageSlider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {

                Toast.makeText(requireContext(),"Clicked",Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(position: Int) {

                Toast.makeText(requireContext(),"Clicked",Toast.LENGTH_SHORT).show()
                val meal = data[position]
                val data =Bundle().apply {
                    putParcelable("popularProduct",meal)
                }

                findNavController().navigate(R.id.action_homeFragment_to_detailsFragment,data)
            }

        })

        adapterCategory.onClick = {

           val  data = Bundle().apply {
                putString("name", it.strCategory.trim())
            }

            findNavController().navigate(R.id.action_homeFragment_to_categoryItemFragment,data)
        }

    }

    private fun observeCategory() {
        lifecycleScope.launchWhenStarted {
            mealViewmodel.categoryState.collectLatest {
                when(it){

                    is Resources.Loading ->{
                        binding.ProgressBar.visibility=View.VISIBLE
                    }
                    is Resources .Success ->{
                        binding.ProgressBar.visibility=View.INVISIBLE
                        adapterCategory.differ.submitList(it.data!!)
                        adapterCategory.notifyDataSetChanged()
                    }
                    is Resources.Error ->{
                        binding.ProgressBar.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }else   ->Unit
                }
            }
        }

    }

    private fun setupImageSlider() {
        lifecycleScope.launchWhenStarted {
            mealViewmodel.mealState.collectLatest {
                when(it){

                    is Resources.Loading ->{
                        binding.ProgressBar.visibility=View.VISIBLE
                    }
                    is Resources .Success ->{
                        addDataToSlider(it.data!!)
                        data = it.data
                        binding.ProgressBar.visibility=View.INVISIBLE
                    }
                    is Resources.Error ->{
                        binding.ProgressBar.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }else   ->Unit
                }
            }
        }

    }

    private fun addDataToSlider(data: List<Popular>) {
        for ( item in data){

            val slideModel = SlideModel(item.strMealThumb,item.strMeal,ScaleTypes.FIT)
            imageList.add(slideModel)
        }
        binding.imageSlider.setImageList(imageList,ScaleTypes.FIT)
    }

    private fun setupRecylerView(){
        binding.recyclerview.apply {
            layoutManager=GridLayoutManager(requireContext(),3)
            adapter=adapterCategory
            HorizontalItemDecorationForCatrgory()
        }
    }

}