package com.example.myapplication.data.di

import android.app.Application
import androidx.room.Room
import com.example.myapplication.data.repository.StepRepository
import com.example.myapplication.data.room.StepDao
import com.example.myapplication.data.room.StepDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideStepDataBase(app: Application): StepDatabase {
        return Room.databaseBuilder(
            app,
            StepDatabase::class.java,
            "StepDatabase"
        )
            .build()
    }

    @Provides
    fun provideStepDao(stepDatabase: StepDatabase): StepDao = stepDatabase.dao

    @Provides
    @Singleton
    fun provideMyRepository(stepdb: StepDatabase) : StepRepository {
        return StepRepository(stepdb.dao)
    }
}