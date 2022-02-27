package com.sofit.test.repositories

import com.sofit.test.model.Country
import com.sofit.test.services.RetrofitUtility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class CountryRepository {

    fun getCountries(): Flow<Country> {
        return flow {
            emit(RetrofitUtility().getAPIService().getCountries())
        }
    }
}