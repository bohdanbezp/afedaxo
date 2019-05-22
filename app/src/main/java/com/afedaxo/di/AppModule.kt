package com.afedaxo.di

import android.content.Context
import androidx.room.Room
import com.afedaxo.AfedaxoApp
import com.afedaxo.interactor.OcrInteractor
import com.afedaxo.model.repository.FilesRepository
import com.afedaxo.model.repository.SessionsRepository
import com.afedaxo.model.repository.UserRepository
import com.afedaxo.model.room.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module class AppModule(val app: AfedaxoApp) {
    @Provides
    @Singleton
    fun provideApp() = app

    @Provides
    fun provideContext(): Context {
        return app.baseContext
    }

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_database"
        ).fallbackToDestructiveMigration().build();
    }

    @Provides
    @Singleton
    fun provideOcrInteractor(context: Context): OcrInteractor {
        return OcrInteractor(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(context: Context): UserRepository {
        return UserRepository(context)
    }

    @Provides
    @Singleton
    fun provideSessionsRepository(appDatabase: AppDatabase,
                                  filesRepository: FilesRepository): SessionsRepository {
        return SessionsRepository(appDatabase, filesRepository)
    }

    @Provides
    @Singleton
    fun provideFilesRepository(context: Context): FilesRepository {
        return FilesRepository(context)
    }
}