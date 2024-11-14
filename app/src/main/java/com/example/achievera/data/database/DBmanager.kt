package com.example.achievera.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.achievera.data.dao.linkedimagesDao
import com.example.achievera.data.dao.noteLinkDao
import com.example.achievera.data.dao.notesDao
import com.example.achievera.data.dao.tagsDao
import com.example.achievera.data.model.LinkedImage
import com.example.achievera.data.model.NoteLink
import com.example.achievera.data.model.NoteTagCrossRef
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.data.model.Tag
import com.example.achievera.di.Converters

@Database(entities = [NotesDatabaseElement::class, NoteLink::class, Tag::class, LinkedImage::class,NoteTagCrossRef::class,], version = 1)//аннотация для объявления базы данных
@TypeConverters(Converters::class)//к этой базе подключен такой конвертер типов (для сложных листов)
abstract class DBmanager: RoomDatabase() {
    abstract fun tagsDao(): tagsDao
    abstract fun linkedimagesDao(): linkedimagesDao
    abstract fun notesDao(): notesDao
    abstract fun noteLinkDao(): noteLinkDao
}