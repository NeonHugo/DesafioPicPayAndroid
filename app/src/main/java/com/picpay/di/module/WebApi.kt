package com.picpay.di.module

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface WebApi {
    @GET("users")
    fun getPeoples(): Call<ResponseBody>

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("transaction")
    fun execTransaction(@Body body: RequestBody): Call<ResponseBody>
}
