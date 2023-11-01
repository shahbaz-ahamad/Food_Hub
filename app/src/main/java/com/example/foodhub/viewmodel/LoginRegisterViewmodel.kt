package com.example.foodhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.datamodel.User
import com.example.foodhub.sealedclass.Resources
import com.example.foodhub.validation.LoginField
import com.example.foodhub.validation.RegistrationField
import com.example.foodhub.validation.RegistrationValidation
import com.example.foodhub.validation.validateEmail
import com.example.foodhub.validation.validateName
import com.example.foodhub.validation.validatePassword
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
):ViewModel() {

    //for the signUp
    private val _authstate = MutableStateFlow<Resources<User>>(Resources.Unspecified())
    val authState = _authstate.asStateFlow()

    //for the login(email and password) && also for the google sign in
    private val _login = MutableStateFlow<Resources<FirebaseUser?>>(Resources.Unspecified())
    val login =_login.asStateFlow()

    //for the validation of register
    //channel is also used for the checking the state
    private val registrationValidationChannel = Channel<RegistrationField>()
    val registrationValidationFlow : Flow<RegistrationField> = registrationValidationChannel.receiveAsFlow()

    //for the validation of the login
    private val _loginValidationChannel = Channel<LoginField>()
    val loginValidationChannel : Flow<LoginField> = _loginValidationChannel.receiveAsFlow()

    //for the password reset
    private val _resetPassword = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val resetPassword = _resetPassword.asStateFlow()

    init {
        if (firebaseAuth.currentUser!= null){
            _login.value = Resources.Success(firebaseAuth.currentUser)
        }
    }

    fun registerUser(email:String , password : String, userDetails:User){

        viewModelScope.launch {

            if(checkRegistration(email,password,userDetails.name.toString())) {
                _authstate.emit(Resources.Loading())
                try {
                    //register the newUser
                    val authResult =
                        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                    //to set the display name
                    authResult.user?.updateProfile(
                        UserProfileChangeRequest.Builder().setDisplayName(userDetails.name).build()
                    )?.await()
                    val user = authResult.user!!

                    //add the data to the firestore
                    val reference = firestore.collection("FoodHub_Users").document(user.uid)
                        .collection("User_List").document()
                    reference.set(userDetails)
                        .addOnSuccessListener {
                            viewModelScope.launch {
                                _authstate.emit(Resources.Success(userDetails))
                            }

                        }
                        .addOnFailureListener {
                            viewModelScope.launch {
                                _authstate.emit(Resources.Error(it.message.toString()))
                            }

                        }

                } catch (e: Exception) {
                    _authstate.emit(Resources.Error(e.message.toString()))
                }
            }else{

                val registrationFieldState =RegistrationField(validateEmail(email), validatePassword(password),
                    validateName(userDetails.name.toString()))
                registrationValidationChannel.send(registrationFieldState)


            }

        }
    }

    fun SignIn(email: String,password: String){
        viewModelScope.launch {
            if(checkLoginValidation(email,password)) {
                _login.emit(Resources.Loading())
                try {

                    val authResult =
                        firebaseAuth.signInWithEmailAndPassword(email, password).await()
                    val user = authResult.user
                    _login.value = Resources.Success(user)

                } catch (e: Exception) {
                    viewModelScope.launch {
                        _login.emit(Resources.Error(e.message.toString()))
                    }
                }
            }else{
                val loginFieldState =LoginField(validateEmail(email), validatePassword(password))
                _loginValidationChannel.send(loginFieldState)

            }
        }
    }

    fun checkRegistration(email: String,password: String,name:String) : Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidate = validatePassword(password)
        val nameValidate = validateName(name)

        val shouldRegister = emailValidation is RegistrationValidation.Success
                && passwordValidate is RegistrationValidation.Success
                && nameValidate is RegistrationValidation.Success

        return shouldRegister
    }

    fun checkLoginValidation(email: String,password: String) : Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidate = validatePassword(password)

        val shouldLogin = emailValidation is RegistrationValidation.Success
                && passwordValidate is RegistrationValidation.Success

        return shouldLogin
    }

    fun googleSignIn(idToken : String){
        viewModelScope.launch {
            _login.emit(Resources.Loading())
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                val user = authResult.user
                // Check if this is a new user
                if (authResult.additionalUserInfo?.isNewUser == true) {
                    // Handle the new user scenario (e.g., store user data in Firestore)
                    //add the data to the firestore
                    val userDetails = User(user?.displayName,user?.email,user?.photoUrl.toString())
                    if(user?.uid != null) {
                        val reference = firestore.collection("FoodHub_Users").document(user.uid)
                            .collection("User_List").document()
                        reference.set(userDetails)
                            .addOnSuccessListener {
                                viewModelScope.launch {
                                    _authstate.emit(Resources.Success(userDetails))
                                }

                            }
                            .addOnFailureListener {
                                viewModelScope.launch {
                                    _authstate.emit(Resources.Error(it.message.toString()))
                                }

                            }
                    }
                }

                _login.value = Resources.Success(user)

            }catch (e : Exception){

                viewModelScope.launch {
                    _login.emit(Resources.Error(e.message.toString()))
                }

            }
        }
    }

    fun password_reset(email: String){
        viewModelScope.launch {
            _resetPassword.emit(Resources.Loading())

            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                _resetPassword.emit(Resources.Success("Sent Email"))
            }catch (e : Exception){
                _resetPassword.emit(Resources.Error(e.message.toString()))
            }
        }
    }
}