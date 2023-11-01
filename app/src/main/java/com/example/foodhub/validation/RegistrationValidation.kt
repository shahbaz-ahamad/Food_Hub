package com.example.foodhub.validation

sealed class RegistrationValidation() {
    object Success : RegistrationValidation()
    data class Failed(val msg: String ):RegistrationValidation()
}

data class RegistrationField(
    val email : RegistrationValidation,
    val password : RegistrationValidation,
    val name : RegistrationValidation
)

data class LoginField(
    val email : RegistrationValidation,
    val password : RegistrationValidation
)