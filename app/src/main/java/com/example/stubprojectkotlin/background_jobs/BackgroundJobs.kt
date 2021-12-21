package com.example.stubprojectkotlin.background_jobs

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class BackgroundJobs(context: Context)  {

    private lateinit var observerWorkId : UUID

    private val workManager by lazy {
        WorkManager.getInstance(context)
    }

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun createOneTimeWorkRequest() {
        val imageWorker = OneTimeWorkRequestBuilder<BackgroundService>()
            .setConstraints(constraints)
            .addTag("imageWork")
            .build()
        workManager.enqueueUniqueWork(
            "oneTimeImageDownload",
            ExistingWorkPolicy.KEEP,
            imageWorker
        )

        observerWorkId = imageWorker.id
//        observeWork(imageWorker.id)

    }

    fun createPeriodicWorkRequest() {
        val imageWorker = PeriodicWorkRequestBuilder<BackgroundService>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("imageWork")
            .build()
        workManager.enqueueUniquePeriodicWork(
            "periodicImageDownload",
            ExistingPeriodicWorkPolicy.KEEP,
            imageWorker
        )
//        observeWork(imageWorker.id)
    }

    private fun createDelayedWorkRequest() {
        val imageWorker = OneTimeWorkRequestBuilder<BackgroundService>()
            .setConstraints(constraints)
            .setInitialDelay(30, TimeUnit.SECONDS)
            .addTag("imageWork")
            .build()
        workManager.enqueueUniqueWork(
            "delayedImageDownload",
            ExistingWorkPolicy.KEEP,
            imageWorker
        )
//        observeWork(imageWorker.id)
    }



    fun getWorkInfoLiveData() = workManager.getWorkInfoByIdLiveData(observerWorkId)

//    private fun observeWork(id: UUID) {
//        workManager.getWorkInfoByIdLiveData(id)
//            .observe(this, { info ->
//                if (info != null && info.state.isFinished) {
//                    hideLottieAnimation()
//                    activityHomeBinding.downloadLayout.visibility = View.VISIBLE
//                    val uriResult = info.outputData.getString("IMAGE_URI")
//                    if (uriResult != null) {
//                        showDownloadedImage(uriResult.toUri())
//                    }
//                }
//            })
//    }

}