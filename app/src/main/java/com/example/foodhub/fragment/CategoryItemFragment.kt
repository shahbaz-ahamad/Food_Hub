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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodhub.R
import com.example.foodhub.adapter.CategoryItemAdapter
import com.example.foodhub.databinding.FragmentCategoryItemBinding
import com.example.foodhub.helper.HideBottomNavigation
import com.example.foodhub.sealedclass.Resources
import com.example.foodhub.viewmodel.CategoryItemViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryItemFragment : Fragment() {
    private lateinit var binding : FragmentCategoryItemBinding
    private lateinit var categoryName : String
    private val categoryItemAdapter by lazy {
        CategoryItemAdapter()
    }

    private val viewmodel by viewModels<CategoryItemViewmodel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        HideBottomNavigation()
        // Inflate the layout for this fragment
        binding= FragmentCategoryItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTheCategoryname()
        setUpRecyclerView()
        observeData()
        lifecycleScope.launch {
            viewmodel.getCategoryData(categoryName)
        }

        categoryItemAdapter.onClick ={
            val data =Bundle().apply {
                putParcelable("category",it)
            }
            findNavController().navigate(R.id.action_categoryItemFragment_to_detailsFragment,data)
        }

    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewmodel.state.collectLatest {
                when(it) {

                    is Resources.Loading -> {
                        binding.progressBar.visibility=View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.progressBar.visibility=View.INVISIBLE
                        if(it.data!= null){
                            categoryItemAdapter.differ.submitList(it.data!!)
                        }
                        Log.d("dataCategoryItem",it.data.toString())

                    }

                    is Resources.Error -> {
                        binding.progressBar.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                        Log.d("error",it.message.toString())
                    }

                    else -> Unit
                }
            }
        }
    }


    fun getTheCategoryname(){
        val args = requireArguments()
        if(args != null) {
            categoryName = args.getString("name").toString()
        }
    }

    fun setUpRecyclerView(){
        binding.recyclerview.apply {
            layoutManager=GridLayoutManager(requireContext(),2)
            adapter=categoryItemAdapter
        }
    }
}