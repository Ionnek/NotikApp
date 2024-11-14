package com.example.achievera.di

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter//конвертирует список строк в строку, разделённую запятыми
    fun fromString(value: String?): MutableList<String> {
        val listType = object : TypeToken<MutableList<String>>() {}.type//создает тип списка для десериализации
        return Gson().fromJson(value, listType)//использует Gson для десериализации строки в список
    }

    @TypeConverter//конвертирует список строк в строку, разделённую запятыми
    fun fromMutableList(list: MutableList<String>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter//конвертирует строку в список Long
    fun fromLongList(value: String?): List<Long> {
        val listType = object : TypeToken<List<Long>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter//конвертирует список Long в строку
    fun fromLongListToString(list: List<Long>?): String {
        return Gson().toJson(list)
    }
}