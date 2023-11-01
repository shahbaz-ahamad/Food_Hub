package com.example.foodhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodhub.databinding.ActivityLoginBinding
import com.example.foodhub.dialogue.setUpBottomNavigationDialog
import com.example.foodhub.sealedclass.Resources
import com.example.foodhub.validation.RegistrationValidation
import com.example.foodhub.viewmodel.LoginRegisterViewmodel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewmodel by viewModels<LoginRegisterViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.dontHaveAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SignupActivity::class.java))
            finish()
        }

        //onbserver the login state
        observeLogin()

        //observe login validation
        observeValidation()

        //onserve the google signin
        observegoogleSignIn()

        //observePasswordReset
        observePasswordReset()
        //for login
        binding.buttonLogin.setOnClickListener {
            val email =binding.editTextEmail.text.trim().toString()
            val password = binding.editTextPassword.text.trim().toString()
            viewmodel.SignIn(email,password)
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

         googleSignInClient = GoogleSignIn.getClient(this, gso)
        //google Login
        binding.googleLoginButton.setOnClickListener {
            startGoogleSignIn()
        }


        //for the password reset
        binding.forgotPassword.setOnClickListener {
            setUpBottomNavigationDialog{email ->
                viewmodel.password_reset(email)
            }
        }
    }

    private fun observegoogleSignIn() {
        lifecycleScope.launchWhenStarted {
            viewmodel.login.collectLatest {
                when(it){
                    is Resources.Loading ->{
                        binding.progressIndicator.visibility= View.VISIBLE
                    }
                    is Resources.Success ->{
                        binding.progressIndicator.visibility= View.INVISIBLE
                        //navigate to the login activity
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }
                    is Resources.Error -> {
                        binding.progressIndicator.visibility= View.INVISIBLE
                        Toast.makeText(this@LoginActivity,it.message.toString(), Toast.LENGTH_SHORT).show()
                    }else ->Unit
                }
            }
        }
    }

    fun startGoogleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // Handle the result of the Google Sign-In process
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken // This is the Google ID token
                if (idToken != null) {
                    // Call your ViewModel's signInWithGoogle function with the ID token
                    viewmodel.googleSignIn(idToken)
                }
            } catch (e: ApiException) {
                // Handle error
                Toast.makeText(this@LoginActivity,e.message.toString(),Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun observeValidation() {
        lifecycleScope.launchWhenStarted {
            viewmodel.loginValidationChannel.collectLatest {
                if(it.email is RegistrationValidation.Failed){
                    binding.editTextEmail.apply {
                        requestFocus()
                        error=it.email.msg
                    }

                }
                if(it.password is RegistrationValidation.Failed){
                    binding.editTextPassword.apply {
                        requestFocus()
                        error=it.password.msg
                    }
                }
            }

        }
    }

    private fun observeLogin() {
        lifecycleScope.launchWhenStarted {
            viewmodel.login.collectLatest {
                when(it){
                    is Resources.Loading ->{
                        binding.progressIndicator.visibility= View.VISIBLE
                    }
                    is Resources.Success ->{
                        binding.progressIndicator.visibility= View.INVISIBLE
                        //navigate to the login activity
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }
                    is Resources.Error -> {
                        binding.progressIndicator.visibility= View.INVISIBLE
                        Toast.makeText(this@LoginActivity,it.message.toString(), Toast.LENGTH_SHORT).show()
                    }else ->Unit
                }
            }
        }
    }

    private fun observePasswordReset() {
        lifecycleScope.launchWhenStarted {
            viewmodel.resetPassword.collectLatest {
                when(it){
                    is Resources.Loading ->{
                        binding.progressIndicator.visibility= View.VISIBLE
                    }
                    is Resources.Success ->{
                        binding.progressIndicator.visibility= View.INVISIBLE
                        Toast.makeText(this@LoginActivity,it.data.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Resources.Error -> {
                        binding.progressIndicator.visibility= View.INVISIBLE
                        Toast.makeText(this@LoginActivity,it.message.toString(), Toast.LENGTH_SHORT).show()
                    }else ->Unit
                }
            }
        }
    }


    companion object{
        private val RC_SIGN_IN = 123 // Request code for Google Sign-In
    }

}