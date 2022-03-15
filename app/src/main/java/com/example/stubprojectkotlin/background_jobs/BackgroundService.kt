package com.example.stubprojectkotlin.background_jobs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.stubprojectkotlin.BuildConfig.BASE_URL
import com.example.stubprojectkotlin.R
import com.example.stubprojectkotlin.data_layer.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BackgroundService (
    private val context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        createNotification()
        val malls = withContext(Dispatchers.IO) {
            createNotification()
            delay(1000)

            val retrofit  = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(OkHttpClient.Builder().build())
                .build()

            val apiService = retrofit.create(ApiService::class.java)
//            apiService.getMalls()
        }

        Log.d("malls" , malls.toString())

        return Result.success(workDataOf("mallsData" to malls.toString()))
    }


    private fun createNotification(title: String = "Stub Project ", description: String = "Background task ") {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("101", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "101")
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(1, notificationBuilder.build())
    }
}