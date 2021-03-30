package com.example.stubprojectkotlin.di.module

import android.content.Context
import com.example.stubprojectkotlin.utils.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun getPreferenceHelper(@ApplicationContext context: Context) : PreferenceHelper = PreferenceHelper(context)
}