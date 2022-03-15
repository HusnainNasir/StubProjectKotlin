package com.example.stubprojectkotlin.di.module

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.stubprojectkotlin.BuildConfig
import com.example.stubprojectkotlin.data_layer.remote.ApiService
import com.example.stubprojectkotlin.ui.main_activity.MainActivity

import com.example.stubprojectkotlin.utils.PreferenceHelper
import com.google.gson.Gson
import com.google.gson.JsonElement
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext

import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideGSON() = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context , preferenceHelper: PreferenceHelper) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthorizationInterceptor(context , preferenceHelper))
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()


    class AuthorizationInterceptor(var context: Context , private var preferenceHelper: PreferenceHelper) : Interceptor {
        private var response: Response? = null

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request: Request = chain.request()

            if (request.headers["Authorization"] == null || request.headers["Authorization"]!!.isEmpty()) {
                val token = "sushs"
               if(token.isNotEmpty()){
                   val headers =
                       request.headers.newBuilder().add("Authorization", token).build()
                   request = request.newBuilder().headers(headers).build()
               }
            }
            response = chain.proceed(request)

//
            return response as Response
        }
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}