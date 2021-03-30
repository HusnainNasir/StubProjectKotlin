package com.example.stubprojectkotlin.network


import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(loginHashMap: HashMap<String , Any>) = apiService.login(loginHashMap)

    suspend fun getMalls() = apiService.getMalls()

}