package com.example.achievera.di

import android.content.Context
import androidx.room.Room
import com.example.achievera.data.dao.linkedimagesDao
import com.example.achievera.data.dao.noteLinkDao
import com.example.achievera.data.dao.notesDao
import com.example.achievera.data.dao.tagsDao
import com.example.achievera.data.database.DBmanager
import com.example.achievera.data.reposotory.LinkedImagesRepository
import com.example.achievera.data.reposotory.NotesRepository
import com.example.achievera.data.reposotory.TagsRepository
import com.example.achievera.domain.repositoryInterfaces.ILinkedImagesRepository
import com.example.achievera.domain.repositoryInterfaces.INotesRepository
import com.example.achievera.domain.repositoryInterfaces.ITagsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
//модуль hilt для создания экземпляров базы данных и DAO для всего приложения
@Module
@InstallIn(SingletonComponent::class)//означает, что экземпляр будет создан в единственном экземпляре
object DataModule {

    @Provides//аннотация для функции которая предоставляет зависимость
    @Singleton//аннотация для функции которая предоставляет единственный экземпляр
    fun provideDatabase(@ApplicationContext appContext: Context): DBmanager {
        return Room.databaseBuilder(//создает базу данных
            appContext,
            DBmanager::class.java,
            "app-database"
        ).build()
    }

    @Provides
    fun provideNotesDao(db: DBmanager): notesDao {//предоставляет экземпляр notesDao
        return db.notesDao()
    }
    @Provides
    fun provideNoteLinkDao(db: DBmanager): noteLinkDao {//предоставляет экземпляр noteLinkDao
        return db.noteLinkDao()
    }
    @Provides
    fun provideTagsDao(db: DBmanager): tagsDao {//предоставляет экземпляр tagsDao
        return db.tagsDao()
    }
    @Provides
    fun provideLinkedImagesDao(db: DBmanager): linkedimagesDao {//предоставляет экземпляр linkedimagesDao
        return db.linkedimagesDao()
    }
    @Provides
    @Singleton
    fun provideNotesRepository(notesDao: notesDao, noteLinkDao: noteLinkDao): INotesRepository {
        return NotesRepository(notesDao, noteLinkDao) // Пример: твой класс из Data слоя
    }

    @Provides
    @Singleton
    fun provideTagsRepository(tagsDao: tagsDao): ITagsRepository {
        return TagsRepository(tagsDao)
    }

    @Provides
    @Singleton
    fun provideLinkedImagesRepository(linkedimagesDao: linkedimagesDao): ILinkedImagesRepository {
        return LinkedImagesRepository(linkedimagesDao)
    }
}