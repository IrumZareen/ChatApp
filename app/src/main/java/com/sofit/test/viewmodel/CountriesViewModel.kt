package com.sofit.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofit.test.model.Country
import com.sofit.test.model.DataState
import com.sofit.test.repositories.CountryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * A ViewModel class to fetch the countries
 **/

class CountriesViewModel : ViewModel() {

    private var _countriesDataState = MutableStateFlow(DataState.data<Country>(null, null))
    val countriesDataState: StateFlow<DataState<Country>> = _countriesDataState

    fun getCountries() {
        viewModelScope.launch {
            _countriesDataState.value = DataState.loading(true)
            CountryRepository().getCountries()
                .catch { e ->
                    _countriesDataState.value = DataState.handleError(e)
                }
                .collect {
                    _countriesDataState.value = DataState.data(null, it)
                }

        }
    }
}