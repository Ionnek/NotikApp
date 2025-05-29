/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.borzykh.achievera.ui.theme.AccentColorGrad1
import com.borzykh.achievera.ui.viewModel.INotesListViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.borzykh.achievera.ui.theme.BackgroundColor
import com.borzykh.achievera.ui.view.NoteListing.ColorConnect
import com.borzykh.achievera.ui.viewModel.NotesListEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import com.borzykh.achievera.data.model.Tag
import com.borzykh.achievera.ui.view.NoteEdit.Custom2TagsMedalBottom
import com.borzykh.achievera.ui.view.NoteEdit.Custom3TagsMedalBottom

@Composable
fun TagEditContent(
    viewModel: INotesListViewModel,
    navController: NavController,
) {
    val scrollState: LazyListState = rememberLazyListState()
    var editState:Boolean by rememberSaveable { mutableStateOf(false) }
    var editTargetNoteState:Boolean by rememberSaveable { mutableStateOf(false) }
    val tags by viewModel.tags.collectAsState()
    var inserTagid:Long by rememberSaveable { mutableStateOf(0) }
    val eventFlow1 = viewModel.eventFlow.collectAsState(initial = null)
    var currentTag:Tag? by remember { mutableStateOf(null)}

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val fabSize: Dp = screenWidth * 0.20f

    Log.d("Новая заметка", "Поймали поток событий: $eventFlow1")
    LaunchedEffect(eventFlow1.value) {
        Log.d("Новая заметка", "Поймали событие: ${eventFlow1.value}")
        eventFlow1.value?.let { event ->
            when (event) {
                is NotesListEvent.NoteInserted -> {
                    Log.d("Новая заметка", "Готовы вставить тег: $inserTagid в заметку: ${event.noteId}")
                    viewModel.InsertCurrentNoteTag(noteId = event.noteId, tagId = inserTagid)

                    navController.navigate(Screen.EditNote.createRoute(false, event.noteId))
                }
            }
        }
    }
    if(editTargetNoteState==true&& currentTag != null){
        Custom3TagsMedalBottom(
            viewModel = viewModel,
            onDialogShownChange = {editTargetNoteState=false},
            tag = currentTag!!
        )

    }
    if(editState==true){
        Custom2TagsMedalBottom(
            viewModel = viewModel,
            onDialogShownChange = {editState=false}
        )

    }

    Scaffold (
        floatingActionButton ={
    FloatingActionButton(
        { editState=true },
        modifier = Modifier
            .size(fabSize)
            .fillMaxWidth()
            .height(50.dp)
            .offset(y = (0.dp),x = 0.dp)
            .border(
                width = 4.dp,
                color = AccentColorGrad1,
                shape = CircleShape
            ),
        containerColor = BackgroundColor,
        shape = CircleShape,
    ){
        Icon(
            modifier = Modifier
                .size(fabSize*0.5f),
            imageVector = Icons.Default.Add,
            contentDescription = "Добавить",
            tint = AccentColorGrad1,
        )
    }},
        modifier = Modifier.background(color = BackgroundColor).fillMaxSize(),
        floatingActionButtonPosition = FabPosition.End)
    {  innerPadding ->
    Box(modifier=Modifier.background(BackgroundColor)) {
        Column {
            Spacer(modifier=Modifier.height(50.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Button(
                    onClick = {
                            navController.navigate(Screen.Home.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundColor,
                        contentColor = BackgroundColor
                    ),

                    modifier = Modifier.padding(horizontal = 16.dp).border(
                        width = 4.dp,
                        color = AccentColorGrad1,
                        shape = CircleShape
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Text("<-", color = AccentColorGrad1)
                }
                Text(text= "List of tags", color = AccentColorGrad1, fontSize = 25.sp)
            }
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                state = scrollState,
                contentPadding = PaddingValues(
                    top = 0.dp,
                    start = 0.dp,
                    end = 0.dp,
                    bottom = 100.dp
            )) {
                items(tags) { item ->
                    Column(){
                        val baseThisTagNotesList by viewModel.getNotesByTag(item.id).collectAsState(initial = emptyList())
                        val thisTagNotesList=baseThisTagNotesList + listOf(null)
                        Card(
                        onClick = {currentTag=item
                            editTargetNoteState=true},
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxHeight(),
                        colors = CardDefaults.cardColors(
                            containerColor =
                            if (item.isActived) {
                                BackgroundColor
                            } else {
                                ColorConnect(item.color)
                            }
                        ),
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp)
                                .fillMaxSize(),
                        ) {
                            Text(
                                text = item.name,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .align(Alignment.CenterStart),
                                color = if (item.isActived) {
                                    ColorConnect(item.color)
                                } else {
                                    BackgroundColor
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    }
                }
            }
        }
    }
    }
}

