package com.sofit.test.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * To create singleton object of retrofit
 **/
object ApiClient {

    private val BASE_URL = "https://restcountries.com/"

    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }
}