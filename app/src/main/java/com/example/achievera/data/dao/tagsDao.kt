package com.example.achievera.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.achievera.data.model.Tag
//интерфейс для доступа к базе данных, содержащей информацию о тэгах
@Dao
interface tagsDao {
    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<Tag>
    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getTagById(id: Long): Tag
    @Query("INSERT INTO tags (name, color) VALUES (:name, :color)")
    suspend fun insertTag(name: String, color: String)
    @Query("UPDATE tags SET name = :name, color = :color WHERE id = :id")
    suspend fun updateTag(id: Long, name: String, color: String)
    @Query("DELETE FROM tags WHERE id = :id")
    suspend fun deleteTag(id: Long)
}