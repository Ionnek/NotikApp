/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.data.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NotesDatabaseElement(
    @PrimaryKey(autoGenerate = true)  var id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "date_edited") var dateEdited: String,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "Reminder") var reminder: Long,
    @ColumnInfo(name = "color") var color: String,
    @ColumnInfo(name = "is_handwritten") var isHandwritten: Boolean,
    @ColumnInfo(name = "chekboxes") var chekboxes: String
)