package com.example.stubprojectkotlin.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stubprojectkotlin.db.dao.UserDao
import com.example.stubprojectkotlin.db.entites.User
import com.example.stubprojectkotlin.utils.NetworkManager
import com.example.stubprojectkotlin.utils.PreferenceHelper
import com.example.stubprojectkotlin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: Repository,
    private val dao: UserDao,
    private val preferenceHelper: PreferenceHelper,
    private val networkManager: NetworkManager,
) : ViewModel() {

    private val users = MutableLiveData<Resource<List<User>>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        exception.localizedMessage?.let {
            users.postValue(Resource.error(it, null))
        }
    }

    fun login(loginHashMap: HashMap<String, Any>) {
        viewModelScope.launch(exceptionHandler) {

            if (networkManager.isWorking) {
                val allUsers = mainRepository.login(loginHashMap)

                val jsonObject = allUsers.asJsonObject

                val data = jsonObject.get("data")

                val authToken: String = data.asJsonObject.get("auth_token").asString

                val user = User()
                user.name = "Husnain"
                user.title = "Husnain"

                mainRepository.insertUser(user)

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

                val data = jsonObject.get("data")
            } else {
                users.postValue(Resource.internetConnectivity("Internet is not Working"))
            }


//            users.postValue(Resource.success(allUsers.await()))
        }
    }

    fun getUsers() = users


    suspend fun getUsersFromDB() = mainRepository.getUser()

}