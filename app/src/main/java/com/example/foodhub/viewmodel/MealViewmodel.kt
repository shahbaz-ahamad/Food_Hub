package com.example.foodhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.datamodel.retrofitdatamodel.Category
import com.example.foodhub.datamodel.retrofitdatamodel.CategoryList
import com.example.foodhub.datamodel.retrofitdatamodel.Popular
import com.example.foodhub.datamodel.retrofitdatamodel.PopularList
import com.example.foodhub.retrofit.ApiService
import com.example.foodhub.sealedclass.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MealViewmodel @Inject constructor(
    private val apiService: ApiService
) :ViewModel(){

    private val _mealState = MutableStateFlow<Resources<List<Popular>>>(Resources.Unspecified())
    val mealState = _mealState.asStateFlow()

    private val _categoryState = MutableStateFlow<Resources<List<Category>>>(Resources.Unspecified())
    val categoryState = _categoryState.asStateFlow()

    init {
        fetchDataForImageSlider()
        fetchCategory()
    }
    fun fetchDataForImageSlider(){
        viewModelScope.launch {
            try {

                apiService.fetchRandomImageForSlider().enqueue(object : Callback<PopularList> {
                    override fun onResponse(call: Call<PopularList>, response: Response<PopularList>) {
                        if(response.body() != null){

                            val data = response.body()!!.meals
                            viewModelScope.launch {
                                _mealState.emit(Resources.Success(data))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PopularList>, t: Throwable) {
                        viewModelScope.launch {
                            _mealState.emit(Resources.Error(t.message.toString()))
                        }
                    }

                })
            }catch (e:Exception){
                _mealState.emit(Resources.Error(e.message.toString()))
            }
        }
    }

    fun fetchCategory(){
        viewModelScope.launch {
            _categoryState.emit(Resources.Loading())
            try {

                apiService.getcategory().enqueue(object : Callback<CategoryList>{
                    override fun onResponse(
                        call: Call<CategoryList>,
                        response: Response<CategoryList>
                    ) {

                        if(response.body() != null){
                            val data = response.body()!!.categories
                            viewModelScope.launch {
                                _categoryState.emit(Resources.Success(data))
                            }
                        }
                    }

                    override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                        viewModelScope.launch {
                            _categoryState.emit(Resources.Error(t.message.toString()))
                        }
                    }

                })
            }catch (e:Exception){
                _categoryState.emit(Resources.Error(e.message.toString()))
            }
        }
    }
}