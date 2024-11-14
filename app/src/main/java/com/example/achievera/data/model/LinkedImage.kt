package com.example.achievera.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "linked_images")
data class LinkedImage(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "note_id") val noteId: Long,
    @ColumnInfo(name = "image_uri") val imageUri: String
)
