package com.example.achievera.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.achievera.data.model.LinkedImage

@Dao
interface linkedimagesDao
{
    @Query("SELECT * FROM linked_images WHERE note_id = :noteId")//запрос к базе данных, который выбирает все записи из таблицы linked_images, где значение поля noteId соответствует заданному noteId
    suspend fun getLinkedImages(noteId: Long): List<LinkedImage>

    @Insert//вставляет новую запись в таблицу linked_images с заданным noteId и imageUri
    suspend fun insertLinkedImage(linkedImage: LinkedImage)

    @Query("DELETE FROM linked_images WHERE id = :id")//удаляет запись из таблицы linked_images по заданному id
    suspend fun deleteLinkedImage(id: Long)

    @Query("DELETE FROM linked_images WHERE note_id = :noteId")//удаляет все записи из таблицы linked_images, которые соответствуют заданному noteId
    suspend fun deleteLinkedImagesByNoteId(noteId: Long)
}