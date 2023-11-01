package com.example.foodhub.validation

import android.util.Patterns

fun validateEmail(email:String):RegistrationValidation{

    if(email.isEmpty()){
        return  RegistrationValidation.Failed("Email can't be empty")
    }

    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return RegistrationValidation.Failed("Wrong Email Format")
    }


    return RegistrationValidation.Success
}

fun validatePassword(password:String):RegistrationValidation{
    if(password.isEmpty()){
        return RegistrationValidation.Failed("Password can't be empty")
    }
    if(password.length <= 6){
        return RegistrationValidation.Failed("Password must contain atleast 6 letter")
    }

    return RegistrationValidation.Success
}

fun validateName(name:String):RegistrationValidation{

    if(name.isEmpty()){
        return RegistrationValidation.Failed("Named can't be empty")
    }

    return RegistrationValidation.Success
}