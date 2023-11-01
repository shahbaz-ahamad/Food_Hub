package com.example.foodhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.foodhub.databinding.ActivityLoginBinding
import com.example.foodhub.databinding.ActivitySignupBinding
import com.example.foodhub.datamodel.User
import com.example.foodhub.sealedclass.Resources
import com.example.foodhub.validation.RegistrationValidation
import com.example.foodhub.viewmodel.LoginRegisterViewmodel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private val binding : ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    private val viewmodel by viewModels<LoginRegisterViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //observe the registration
        observerRegistration()
        observeRegistrationValidation()
        binding.alreadyHaveAccout.setOnClickListener {
            startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
            finish()
        }


        binding.registerButton.setOnClickListener {
            val name = binding.editTextName.text.trim().toString()
            val email = binding.editTextEmail.text.trim().toString()
            val password = binding.editextPassword.text.trim().toString()
            viewmodel.registerUser(email,password, User(name,email,""))
        }
    }

    private fun observerRegistration() {
        lifecycleScope.launchWhenStarted {
            viewmodel.authState.collectLatest {
                when(it){

                    is Resources.Loading ->{
                        binding.progressIndicator.visibility= View.VISIBLE
                    }
                    is Resources.Success ->{
                        binding.progressIndicator.visibility= View.INVISIBLE
                        //navigate to the login activity
                        Toast.makeText(this@SignupActivity,"Registration Successfull",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
                        finish()
                    }
                    is Resources.Error -> {
                        binding.progressIndicator.visibility= View.INVISIBLE
                        Toast.makeText(this@SignupActivity,it.message.toString(),Toast.LENGTH_SHORT).show()
                    }else ->Unit
                }
            }
        }
    }

    fun observeRegistrationValidation(){
        lifecycleScope.launch {

            viewmodel.registrationValidationFlow.collectLatest{
                if(it.email is RegistrationValidation.Failed){
                    binding.editTextEmail.apply {
                        requestFocus()
                        error=it.email.msg
                    }

                }

                if(it.password is RegistrationValidation.Failed){
                    binding.editextPassword.apply {
                        requestFocus()
                        error=it.password.msg
                    }
                }

                if(it.name is RegistrationValidation.Failed){
                    binding.editTextName.apply {
                        requestFocus()
                        error=it.name.msg
                    }
                }
            }
        }
    }
}