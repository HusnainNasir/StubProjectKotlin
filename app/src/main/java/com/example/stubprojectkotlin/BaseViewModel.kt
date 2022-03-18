package com.example.stubprojectkotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stubprojectkotlin.data_layer.model.network_model.ErrorBody
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

open class BaseViewModel @Inject constructor(open var gson: Gson, application: Application) : AndroidViewModel(application){

    protected val loadingMutableState = Channel<Boolean>(Channel.BUFFERED)
    val loadingState = loadingMutableState.receiveAsFlow()
    protected val errorMutableState = Channel<String>(Channel.BUFFERED)
    val errorState = errorMutableState.receiveAsFlow()
    protected val internetMutableState = Channel<String>(Channel.BUFFERED)
    val internetState = internetMutableState.receiveAsFlow()

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->

        viewModelScope.launch {
            val errorBody : String =
                (exception as? HttpException)?.response()?.errorBody()?.string() ?: ""

            errorBody.isNotEmpty().let {
                if (it){
                    val errorBodyModel : ErrorBody = Gson().fromJson(errorBody , ErrorBody::class.java)
                    errorMutableState.send(errorBodyModel.error_description ?: errorBodyModel.message ?: errorBodyModel.error ?: exception.localizedMessage )
                }else{
                    errorMutableState.send(exception.localizedMessage)
                }
            }
        }
    }

    fun errorHandling(exception : Throwable){

        viewModelScope.launch {

            loadingMutableState.send(false)
            val errorBody : String =
                (exception as? HttpException)?.response()?.errorBody()?.string() ?: ""

            errorBody.isNotEmpty().let {
                if (it){
                    val errorBodyModel : ErrorBody = gson.fromJson(errorBody , ErrorBody::class.java)
                    errorMutableState.send(errorBodyModel.error_description ?: errorBodyModel.message ?: errorBodyModel.error ?: exception.localizedMessage )
                }else{
                    errorMutableState.send(exception.localizedMessage)
                }
            }
        }
    }
}