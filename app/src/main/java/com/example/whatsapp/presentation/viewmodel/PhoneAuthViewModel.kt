package com.example.whatsapp.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.whatsapp.models.PhoneAuthUser
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase
): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Ideal)
    val authState = _authState.asStateFlow()

    private val userRef = database.reference.child("users")

    fun sendVerificationCode(phoneNumber:String, activity: Activity){
        _authState.value = AuthState.Loading

        val option = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)
                Log.d("PhoneAuth", "onCodeSent triggered. verification ID: $id")
                _authState.value = AuthState.CodeSent(verificationId = id)
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                siginWithCredential(credential, context= activity)
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.e("PhoneAuth", "Verification failed: ${exception.message}")
                _authState.value = AuthState.Error(exception.message ?: "Verification failed")
            }

        }
        val phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(option)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }


    private fun siginWithCredential(credential: PhoneAuthCredential, context: Context){
        _authState.value = AuthState.Loading

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    val phoneAuthUser = PhoneAuthUser(
                        userId = user?.uid?:"",
                        phoneNumber = user?.phoneNumber?:"",
                        "",
                        "",
                        ""
                    )

                    markUserAsSigned(context)
                    _authState.value = AuthState.Success(phoneAuthUser)

                    fetchUserProfile(user?.uid?: "")
                } else{
                    _authState.value = AuthState.Error(task.exception?.message?: "Sign-in failed")
                }
            }
    }
    private fun markUserAsSigned(context: Context){
        val sharedPreferences= context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isSignedIn", true).apply()
    }

    private fun fetchUserProfile(userId: String){
        val userRef = userRef.child(userId)
        userRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()){
                val userProfile = snapshot.getValue(PhoneAuthUser :: class.java)
                if(userProfile != null){
                    _authState.value = AuthState.Success(userProfile)
                }
            }
        }.addOnFailureListener {
            _authState.value = AuthState.Error("Failed to fetch user profile")
        }
    }

    fun verifyCode(otp:String, context: Context){
        val currentAuthState = _authState.value

        if(currentAuthState !is AuthState.CodeSent || currentAuthState.verificationId.isEmpty()){
            Log.e("PhoneAuth", "Attempting to verify OTP without a valid verification ID")

            _authState.value = AuthState.Error("Verification not started or invalid ID")
            return
        }

        val credential =  PhoneAuthProvider.getCredential(currentAuthState.verificationId, otp)
        siginWithCredential(credential, context)
    }
    fun saveUserProfile(userId: String, name: String, status: String, profileImage : Bitmap?){

        val database = FirebaseDatabase.getInstance().reference

        val encodedImage = profileImage?.let { convertBitmapToBase64(it)}
        val userProfile = PhoneAuthUser(
            userId = userId,
            name = name,
            status = status,
            phoneNumber = Firebase.auth.currentUser?.phoneNumber?:"",
            profilePicture = encodedImage
        )

        database.child("users").child(userId).setValue(userProfile)
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String{

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray,Base64.DEFAULT)
    }

    fun resetAuthState(){
        _authState.value = AuthState.Ideal
    }

    fun signOut(activity: Activity){
        firebaseAuth.signOut()
        val sharedPreferences = activity.getSharedPreferences("app_prefs", Activity.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isSigned", false).apply()
    }
}

sealed class AuthState{
    object Ideal: AuthState()
    object Loading: AuthState()
    data class CodeSent(val verificationId: String): AuthState()
    data class Success(val user: PhoneAuthUser): AuthState()
    data class Error(val message:String): AuthState()
}


