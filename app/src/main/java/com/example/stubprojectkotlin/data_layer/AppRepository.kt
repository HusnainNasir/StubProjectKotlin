package com.example.stubprojectkotlin.data_layer


import com.example.stubprojectkotlin.data_layer.remote.ApiService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepository @Inject constructor(private val apiService: ApiService){
   suspend fun login(loginHashMap: HashMap<String , Any>) = flow { emit(apiService.login(loginHashMap)) }
}