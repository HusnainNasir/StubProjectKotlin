package com.example.stubprojectkotlin.domain_layer.login_use_case

import com.example.stubprojectkotlin.data_layer.AppRepositoryImpl
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(var appRepository: AppRepositoryImpl) {

    operator fun invoke(loginHashMap: HashMap<String , Any>) : Flow<JsonElement> = flow {
        appRepository.login(loginHashMap)
    }
}