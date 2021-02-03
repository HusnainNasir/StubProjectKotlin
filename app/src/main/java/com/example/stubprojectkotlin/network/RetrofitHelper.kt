package com.example.stubprojectkotlin.network

import android.app.Application
import android.content.Context
import com.example.stubprojectkotlin.MyApplication.Companion.context
import com.example.stubprojectkotlin.utils.Constants
import com.example.stubprojectkotlin.utils.PreferenceHelper
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class RetrofitHelper private constructor() {
    val apiInterface: ApiService

    class AuthorizationInterceptor : Interceptor {
        var response: Response? = null
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request: Request = chain.request()

            val preferenceUtil = PreferenceHelper.preferenceInstance(context)
            if (request.headers["Authorization"] == null || request.headers["Authorization"]!!.isEmpty()) {
                val token = getAuthorizationHeader(context)
                if (token.isNotEmpty()) {
                    val headers = request.headers.newBuilder().add("Authorization", "Bearer $token").build()
                    request = request.newBuilder().headers(headers).build()
                }
            }
            response = chain.proceed(request)

//            if(attempt<=2)
//                if (response.code()== 401  && preferenceUtil.getFOR_REFRESH_USER_NAME() != null) {
//                    response.close();
//                    attempt++;
//                    Log.d("Username: ",preferenceUtil.getFOR_REFRESH_USER_NAME());
//                    Log.d("Password: ",preferenceUtil.getFOR_REFRESH_PASSWORD());
//                    ApiServiceInterface tokenApi = getRetrofit().create(ApiServiceInterface.class);
//                    retrofit2.Response<LoginData> loginResponse = tokenApi.login(preferenceUtil.getFOR_REFRESH_USER_NAME() , preferenceUtil.getFOR_REFRESH_PASSWORD() , "password").execute();
//                    if (loginResponse.isSuccessful()) {
//                        LoginData loginResponseObject = loginResponse.body();
//                        try {
//                            request = request.newBuilder()
//                                    .header("Authorization", "Bearer " + loginResponseObject.getAccessToken()).build();
//                            response = chain.proceed(request);
//                            preferenceUtil.setAuthToken(loginResponseObject.getTokenType() + " " + loginResponseObject.getAccessToken());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        attempt=3; // so don't try for next attempt, already handled
//                    } else {
//                        attempt = 3;
//                        BaseResponse responseBody = new Gson().fromJson(loginResponse.errorBody().string(),BaseResponse.class);
//                        new Handler(Looper.getMainLooper()).post(()->{
//                            Toast.makeText(context, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
//                        });
//                        // goto Login as we cannot refresh token
//
//                    }
//
//                }
//
            return response as Response
        } //
    }

    private val retrofit: Retrofit
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(AuthorizationInterceptor())
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClientBuilder.build())
                .build()
        }

    companion object {
        private var instance: RetrofitHelper? = null
        @JvmStatic
        fun getInstance(application: Application): RetrofitHelper? {
            if (instance == null) {
                instance = RetrofitHelper()
            }
            return instance
        }

        fun getAuthorizationHeader(context: Context): String {
            return PreferenceHelper.preferenceInstance(context)?.authToken.toString()
        }
    }

    //    private  int attempt=0;
    init {
        val retrofit = retrofit
        apiInterface = retrofit.create(ApiService::class.java)
    }
}