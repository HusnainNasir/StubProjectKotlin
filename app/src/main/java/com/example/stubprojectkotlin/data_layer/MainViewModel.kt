package com.example.stubprojectkotlin.data_layer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stubprojectkotlin.domain_layer.login_use_case.LoginUseCase
import com.example.stubprojectkotlin.utils.NetworkManager
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _login = MutableSharedFlow<JsonElement>()
    val login = _login

    private fun errorHandling(exception: Throwable) {

        viewModelScope.launch {

            val errorBody: String =
                (exception as? HttpException)?.response()?.errorBody()?.string() ?: ""

//            errorBody.isNotEmpty().let {
//                if (it){
//                    val errorBodyModel : ErrorBody = Gson().fromJson(errorBody , ErrorBody::class.java)
//                    errorMutableState.send(errorBodyModel.error_description ?: errorBodyModel.message ?: errorBodyModel.error ?: exception.localizedMessage )
//                }else{
//                    errorMutableState.send(exception.localizedMessage)
//                }
//            }
        }
    }

    fun login(loginHashMap: HashMap<String, Any>) {

        viewModelScope.launch(Dispatchers.IO) {

            if (networkManager.isWorking) {

                loginUseCase(loginHashMap).flowOn(Dispatchers.IO).catch { errorHandling(it) }
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