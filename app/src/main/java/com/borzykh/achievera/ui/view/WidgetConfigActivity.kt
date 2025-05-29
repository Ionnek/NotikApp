/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.ui.theme.BackgroundColor
import com.borzykh.achievera.ui.theme.TestMultyScreensTheme
import com.borzykh.achievera.ui.view.NoteListing.ColorConnect
import com.borzykh.achievera.ui.view.NoteListing.nameChek
import com.borzykh.achievera.ui.viewModel.NotesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WidgetConfigActivity @Inject constructor(
): ComponentActivity(){
    @Inject lateinit var sharedPreferences: SharedPreferences
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
        setContent {
            TestMultyScreensTheme {
                val viewModel: NotesListViewModel = hiltViewModel()
                var scrollListState: LazyListState = rememberLazyListState()
                val configuration = LocalConfiguration.current
                val screenHeight = configuration.screenHeightDp.dp
                val itemsList: LazyPagingItems<NotesDatabaseElement> =viewModel.notes.collectAsLazyPagingItems()
                Surface() {
                    LazyColumn (
                        state = scrollListState,
                        modifier = Modifier
                            .padding(start = 0.dp, end = 0.dp)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = screenHeight*0.28f,
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 100.dp
                        ),
                    ) {
                        items(
                            count = itemsList.itemCount,

                            key = itemsList.itemKey { note -> note.id },

                            contentType = itemsList.itemContentType { "contentType" }
                        ) { index ->
                            val item = itemsList[index]
                            if(item != null){
                                Card(
                                    onClick = { onNoteSelected(item)},
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = ColorConnect(item.color))
                                ) {
                                    Column(modifier = Modifier.padding(8.dp).fillMaxWidth().wrapContentHeight()) {
                                        Text(
                                            text = nameChek(item.text, item.name),
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            maxLines = 1,
                                            color = BackgroundColor,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            maxLines = 2,
                                            text = item.text,
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            color = BackgroundColor
                                        )
                                    }
                                }
                            }else{
                                Text("Loading...", modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
    private fun onNoteSelected(note: NotesDatabaseElement) {
        val prefs = sharedPreferences
        with(prefs.edit()) {
            putLong("note_id_$appWidgetId", note.id)   // <-- только ID
            apply()
        }

        val appWidgetManager = AppWidgetManager.getInstance(this)
        MyNoteWidgetProvider.updateAppWidget(
            context = this,
            appWidgetManager = appWidgetManager,
            appWidgetId = appWidgetId,
            noteTitle = note.name,
            noteText = note.text,
            noteId = note.id
        )

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}