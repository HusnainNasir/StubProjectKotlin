package com.example.stubprojectkotlin.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stubprojectkotlin.db.ApplicationDatabase
import com.example.stubprojectkotlin.db.dao.UserDao
import com.example.stubprojectkotlin.db.entites.User
import com.example.stubprojectkotlin.utils.NetworkManager
import com.example.stubprojectkotlin.utils.PreferenceHelper
import com.example.stubprojectkotlin.utils.Resource
import com.example.stubprojectkotlin.utils.Status
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: Repository,
    private val applicationDatabase: ApplicationDatabase,
    private val preferenceHelper: PreferenceHelper,
    private val networkManager: NetworkManager,
) : ViewModel() {

    private val users = MutableLiveData<Resource<List<User>>>()
    private val malls = MutableLiveData<Resource<JsonElement>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        exception.localizedMessage?.let {
            malls.postValue(Resource.error(it, null))
        }
    }

    fun login(loginHashMap: HashMap<String, Any>) {

//        users.postValue(Resource.loading(null))
        viewModelScope.launch(exceptionHandler) {

            if (networkManager.isWorking) {
                val allUsers = mainRepository.login(loginHashMap)

                val jsonObject = allUsers.asJsonObject

                val data = jsonObject.get("data")

                val authToken: String = data.asJsonObject.get("auth_token").asString

                val user = User(0 , "Husnain" , "Nasir" , "9383")


                mainRepository.insertUser(user)

                applicationDatabase.userDao().insert(user)

                preferenceHelper.authToken = authToken
            } else {
                users.postValue(Resource.internetConnectivity("Internet is not Working"))
            }
//            users.postValue(Resource.success(allUsers.await()))
        }
    }


    fun getMalls() {
        viewModelScope.launch(exceptionHandler) {

            if (networkManager.isWorking) {
                val allUsers = mainRepository.getMalls()

                val jsonObject = allUsers.asJsonObject

                malls.postValue(Resource(Status.SUCCESS , allUsers , ""))

                val data = jsonObject.get("data")
            } else {
                malls.postValue(Resource.internetConnectivity("Internet is not Working"))
            }


//            users.postValue(Resource.success(allUsers.await()))
        }
    }

    fun getUsers() = users

    fun getMallsLive() = malls


//    suspend fun getUsersFromDB() = mainRepository.getUser()

}