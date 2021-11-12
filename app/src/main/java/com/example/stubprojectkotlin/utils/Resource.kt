package com.example.stubprojectkotlin.utils

data class Resource<out T>(val status: Status, val data: T?, val message: String? = null ,
val isLoading : Boolean? = null) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(isLoading: Boolean): Resource<T> {
            return Resource(Status.LOADING, null , null , isLoading)
        }

        fun <T> internetConnectivity(msg: String): Resource<T> {
            return Resource(Status.INTERNET_CONNECTIVITY, null, msg)
        }

    }

}