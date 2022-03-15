package com.example.stubprojectkotlin.domain_layer

import com.google.gson.JsonElement

interface AppRepository {
    suspend fun login(loginHashMap: HashMap<String , Any>) : JsonElement
}