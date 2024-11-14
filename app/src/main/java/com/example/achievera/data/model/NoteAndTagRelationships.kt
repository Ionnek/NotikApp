package com.example.achievera.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NoteWithTags(
    @Embedded val note: NotesDatabaseElement,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val tags: List<Tag>
)

data class TagWithNotes(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val notes: List<NotesDatabaseElement>
)
