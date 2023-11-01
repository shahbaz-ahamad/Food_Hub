package com.example.foodhub.datamodel.retrofitdatamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopularList(
    val meals: List<Popular>
):Parcelable