/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.borzykh.achievera.data.dao.linkedimagesDao
import com.borzykh.achievera.data.dao.noteLinkDao
import com.borzykh.achievera.data.dao.notesDao
import com.borzykh.achievera.data.dao.tagsDao
import com.borzykh.achievera.data.database.DBmanager
import com.borzykh.achievera.data.reposotory.LinkedImagesRepository
import com.borzykh.achievera.data.reposotory.NotesRepository
import com.borzykh.achievera.data.reposotory.TagsRepository
import com.borzykh.achievera.domain.repositoryInterfaces.ILinkedImagesRepository
import com.borzykh.achievera.domain.repositoryInterfaces.INotesRepository
import com.borzykh.achievera.domain.repositoryInterfaces.ITagsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): DBmanager {
        return Room.databaseBuilder(
            appContext,
            DBmanager::class.java,
            "app-database"
        ).addMigrations(DBmanager.MIGRATION_1_2,DBmanager.MIGRATION_2_3,DBmanager.MIGRATION_3_4).build()
    }
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideNotesDao(db: DBmanager): notesDao {
        return db.notesDao()
    }
    @Provides
    fun provideNoteLinkDao(db: DBmanager): noteLinkDao {
        return db.noteLinkDao()
    }
    @Provides
    fun provideTagsDao(db: DBmanager): tagsDao {
        return db.tagsDao()
    }
    @Provides
    fun provideLinkedImagesDao(db: DBmanager): linkedimagesDao {
        return db.linkedimagesDao()
    }
    @Provides
    @Singleton
    fun provideNotesRepository(notesDao: notesDao, noteLinkDao: noteLinkDao): INotesRepository {
        return NotesRepository(notesDao, noteLinkDao)
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