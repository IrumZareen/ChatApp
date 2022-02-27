package com.sofit.test.model

/**
 * A Generic Class to handle the states of api calling
 */
data class DataState<T>(
    val message : String? = null,
    val loading : Boolean = false,
    val data : T? =null,
    val handleError : Throwable? =null
){

    companion object{

        fun <T> loading(
            isLoading : Boolean
        ) : DataState<T> {
            return DataState(
                message = null,
                loading = isLoading,
                data = null
            )
        }

        fun <T> data(
            message: String? = null,
            data : T? = null
        ) : DataState<T> {
            return DataState(
                message = message,
                loading = false,
                data = data
            )
        }


        fun <T> handleError(
            throwable: Throwable?=null
        ) : DataState<T> {
            return DataState(
                message = null,
                loading = false,
                data = null,
                handleError = throwable
            )
        }

    }


}