package com.example.stubprojectkotlin.data_layer.remote

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(@FieldMap loginHashMap: HashMap<String , Any>): JsonElement

    @FormUrlEncoded
    @POST("login")
    suspend fun refreshToken(@FieldMap loginHashMap: HashMap<String , Any>): Response<JsonElement>

}