package com.example.stubprojectkotlin.data_layer


import com.example.stubprojectkotlin.data_layer.remote.ApiService
import com.example.stubprojectkotlin.domain_layer.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(private val apiService: ApiService) : AppRepository {
    override suspend fun login(loginHashMap: HashMap<String , Any>) = apiService.login(loginHashMap)
}