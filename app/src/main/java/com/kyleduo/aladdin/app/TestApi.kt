package com.kyleduo.aladdin.app

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author kyleduo on 2021/6/30
 */
class TestApi : TestApiService {

    val api by lazy {
        ApiClientFactory.createService(TestApiService::class.java)
    }

    override suspend fun get(param1: String): Any {
        return api.get(param1)
    }
}


interface TestApiService {

    @GET("/get")
    suspend fun get(@Query("param1") param1: String): Any
}