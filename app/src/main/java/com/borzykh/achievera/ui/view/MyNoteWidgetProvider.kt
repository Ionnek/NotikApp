/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.RemoteViews
import com.borzykh.achievera.R
import com.borzykh.achievera.domain.usecases.GetNoteByIdUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MyNoteWidgetProvider:AppWidgetProvider() {
    @Inject lateinit var GetNoteByIdUseCase: GetNoteByIdUseCase
    @Inject lateinit var sharedPreferences: SharedPreferences
    companion object{
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                            appWidgetId: Int, noteTitle: String, noteText: String, noteId:Long){
            val views = RemoteViews(context.packageName, R.layout.my_note_widget)
            val noteId=noteId
            views.setTextViewText(R.id.nameText, noteTitle)
            views.setTextViewText(R.id.descText, noteText)

            val intent = Intent(context, MainScreenV::class.java)
            intent.putExtra("noteId",noteId)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { id ->
            val noteId = sharedPreferences.getLong("note_id_$id", -1L)
            if (noteId != -1L) {
                CoroutineScope(Dispatchers.IO).launch {
                    val note = GetNoteByIdUseCase(noteId) ?: return@launch
                    withContext(Dispatchers.Main) {
                        updateAppWidget(context, manager, id, note.name, note.text, note.id)
                    }
                }
            }
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        val prefs = context?.getSharedPreferences("MyNoteWidgetPrefs", Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        if (appWidgetIds != null && editor != null) {
            for (appWidgetId in appWidgetIds) {
                editor.remove("note_id_$appWidgetId")
            }
        }
        if (editor != null) {
            editor.apply()
        }
    }
}