package com.sofit.test.services

import com.sofit.test.model.Country
import retrofit2.http.GET

/**
 * An Interface having a function to call api of fetch all countries.
 **/
interface ApiInterface {

    @GET("v2/all")
    suspend fun getCountries(): Country
}