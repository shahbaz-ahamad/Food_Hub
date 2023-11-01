package com.example.foodhub.retrofit

import com.example.foodhub.datamodel.retrofitdatamodel.CategoryList
import com.example.foodhub.datamodel.retrofitdatamodel.EachCategoryItemList
import com.example.foodhub.datamodel.retrofitdatamodel.PopularList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("filter.php?c=Seafood")
    fun fetchRandomImageForSlider(): Call<PopularList>

    @GET("categories.php")
    fun getcategory(): Call<CategoryList>

    @GET("filter.php?c=Seafood")
    fun getCategoryItem(@Query("c") categoryName: String): Call<EachCategoryItemList>
}
