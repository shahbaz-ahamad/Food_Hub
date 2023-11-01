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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodhub.R
import com.example.foodhub.adapter.CategoryItemAdapter
import com.example.foodhub.databinding.FragmentCategoryItemBinding
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
        // Inflate the layout for this fragment
        binding= FragmentCategoryItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTheCategoryname()
        setUpRecyclerView()
        observeData()
        viewmodel.getCategoryData(categoryName)
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewmodel.state.collectLatest {
                when(it) {

                    is Resources.Loading -> {

                    }

                    is Resources.Success -> {

                        if(it.data!= null){
                            categoryItemAdapter.differ.submitList(it.data!!)
                        }
                        Log.d("data",it.data.toString())

                    }

                    is Resources.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }
    }


    fun getTheCategoryname(){
        val args = requireArguments()
        if(args != null){
            categoryName = args.getString("name").toString()
        }
        Toast.makeText(requireContext(),categoryName,Toast.LENGTH_SHORT).show()
    }

    fun setUpRecyclerView(){
        binding.recyclerview.apply {
            layoutManager=GridLayoutManager(requireContext(),2)
            adapter=categoryItemAdapter
        }
    }
}