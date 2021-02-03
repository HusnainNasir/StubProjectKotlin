package com.example.stubprojectkotlin.network

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.stubprojectkotlin.network.RetrofitHelper.Companion.getInstance
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Repository(application: Application) {

    // MEMBERS
    private val apiServiceInterface: ApiService = getInstance(application)!!.apiInterface
    private val executor: Executor

    // COMPOSITE DISPOSABLE
    private val compositeDisposable = CompositeDisposable()

    // Error Live Data
    val onThrowableLiveData = MutableLiveData<Throwable>()

    init {
        executor = Executors.newSingleThreadExecutor()
    }
}