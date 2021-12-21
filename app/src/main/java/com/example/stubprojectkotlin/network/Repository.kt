package com.example.stubprojectkotlin.network


import com.example.stubprojectkotlin.db.dao.UserDao
import com.example.stubprojectkotlin.db.entites.User
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService  , private val userDao: UserDao) {

    suspend fun login(loginHashMap: HashMap<String , Any>) = apiService.login(loginHashMap)

    suspend fun getMalls() = apiService.getMalls()

//    suspend fun getUser() = userDao.getUser()

    suspend fun insertUser(user: User) = userDao.insert(user)

}