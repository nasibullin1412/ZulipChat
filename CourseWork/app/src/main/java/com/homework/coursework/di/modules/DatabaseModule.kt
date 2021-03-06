package com.homework.coursework.di.modules

import com.homework.coursework.data.frameworks.database.AppDatabase
import com.homework.coursework.data.frameworks.database.dao.*
import dagger.Lazy
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(): AppDatabase = AppDatabase.instance

    @Singleton
    @Provides
    fun provideMessageDao(database: Lazy<AppDatabase>): MessageDao = database.get().messageDao()

    @Singleton
    @Provides
    fun provideMessageToUserCrossRefDao(database: Lazy<AppDatabase>): MessageToUserCrossRefDao {
        return database.get().messageToUserCrossRefDao()
    }

    @Singleton
    @Provides
    fun provideStreamDao(database: Lazy<AppDatabase>): StreamDao = database.get().streamDao()

    @Singleton
    @Provides
    fun provideUserDao(database: Lazy<AppDatabase>): UserDao = database.get().userDao()

    @Singleton
    @Provides
    fun providesApiKeyDao(database: Lazy<AppDatabase>): AuthDao = database.get().apiKeyDao()

    @Singleton
    @Provides
    fun providesCurrProfileDao(database: Lazy<AppDatabase>): CurrentProfileDao =
        database.get().currentProfileDao()
}
