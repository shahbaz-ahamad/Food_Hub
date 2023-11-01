package com.example.foodhub.datamodel

data class User(
    val name :String? ="",
    val email: String?= "",
    val imagePath: String? = ""
){
    constructor() : this("","","")
}
