package com.example.stubprojectkotlin.data_layer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stubprojectkotlin.BaseViewModel
import com.example.stubprojectkotlin.utils.NetworkManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val appRepository: AppRepository,
    override var gson: Gson,
    application: Application
) : BaseViewModel(gson , application) {

    private val _login = MutableSharedFlow<JsonElement>()
    val login = _login


    fun login(loginHashMap: HashMap<String, Any>) {

        viewModelScope.launch(Dispatchers.IO) {

            if (networkManager.isWorking) {

                appRepository.login(loginHashMap).flowOn(Dispatchers.IO).catch { errorHandling(it) }
                    .collect {

                        val jsonObject = it.asJsonObject

                        val data = jsonObject?.get("data")

                        val authToken: String =
                            data?.asJsonObject?.get("auth_token")?.asString.toString()
                    }

            }
        }
    }


}