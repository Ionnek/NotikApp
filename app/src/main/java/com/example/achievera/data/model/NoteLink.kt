package com.example.achievera.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_links")
data class NoteLink(
    @PrimaryKey(autoGenerate = true) val noteId: Long,
    @ColumnInfo(name = "linked_note_id") val linkedNoteId: Long
)