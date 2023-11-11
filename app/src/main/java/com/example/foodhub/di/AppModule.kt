package com.example.foodhub.di

import com.example.foodhub.helper.Increase_Deacrease
import com.example.foodhub.retrofit.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseAuth() : FirebaseAuth =FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseStore(): FirebaseFirestore = FirebaseFirestore.getInstance()


    @Provides
    @Singleton
    fun ProvideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiServices(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideIncrease_Deacrease(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ) = Increase_Deacrease(firestore,firebaseAuth)


}