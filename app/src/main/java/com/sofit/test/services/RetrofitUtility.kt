package com.sofit.test.services

class RetrofitUtility {
    /**
     * This class will return retrofit instance for api calling
     **/
    fun getAPIService(): ApiInterface {
        return ApiClient.getClient().create(ApiInterface::class.java)
    }
}