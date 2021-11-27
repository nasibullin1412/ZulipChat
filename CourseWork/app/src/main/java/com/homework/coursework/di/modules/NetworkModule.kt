package com.homework.coursework.di.modules

import com.homework.coursework.data.frameworks.network.ApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Singleton

@Module
class NetworkModule {
    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun bindApiService(): ApiService = ApiService.instance
}