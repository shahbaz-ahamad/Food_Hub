package com.example.foodhub.datamodel.retrofitdatamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Popular(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
):Parcelable