package com.example.stubprojectkotlin.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stubprojectkotlin.db.dao.UserDao
import com.example.stubprojectkotlin.db.entites.User
import com.example.stubprojectkotlin.utils.PreferenceHelper
import com.example.stubprojectkotlin.utils.Resource
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: Repository,
    private val dao: UserDao,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    private val users = MutableLiveData<Resource<List<User>>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        users.postValue(Resource.error(exception.localizedMessage, null))
    }

    fun login(loginHashMap: HashMap<String , Any>) {
        viewModelScope.launch(exceptionHandler) {
            val allUsers = mainRepository.login(loginHashMap)

            val jsonObject = allUsers.asJsonObject

            val data = jsonObject.get("data")

            val authToken : String = data.asJsonObject.get("auth_token").asString

            preferenceHelper.authToken = authToken
//            users.postValue(Resource.success(allUsers.await()))
        }
    }


    fun getMalls() {
        viewModelScope.launch(exceptionHandler) {
            val allUsers = mainRepository.getMalls()

            val jsonObject = allUsers.asJsonObject

            val data = jsonObject.get("data")


//            users.postValue(Resource.success(allUsers.await()))
        }
    }

    fun getUsers () = users


}