package com.example.achievera.ui.view

import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.achievera.ui.theme.AccentColorGrad1
import com.example.achievera.ui.viewModel.INotesListViewModel
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import com.example.achievera.R
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.view.NoteEdit.items
import com.example.achievera.ui.view.NoteEdit.pagingItems
import com.example.achievera.ui.view.NoteListing.ColorConnect
import com.example.achievera.ui.view.NoteListing.nameChek
import com.example.achievera.ui.viewModel.NotesListEvent
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.achievera.ui.theme.AccentColorGrad1
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.view.NoteEdit.LineWithCheckBox
import com.example.achievera.ui.view.Screen
import com.google.gson.Gson

@Composable
fun TagEditContent(
    viewModel: INotesListViewModel,
    navController: NavController,
    searchState: MutableState<Boolean>,
    onClick:()->Unit
) {
    val height by animateDpAsState(
        targetValue = if (searchState.value) 100.dp else 60.dp,
        animationSpec = tween(durationMillis = 300)
    )
    val scrollState: LazyListState = rememberLazyListState()
    val tagsList by viewModel.FilterTags.collectAsState(initial = emptyList())
    var inserTagid:Long by rememberSaveable { mutableStateOf(0) }
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemIndex to scrollState.firstVisibleItemScrollOffset}
            .collect { (index, offset) ->
                if (index>0 || offset > 100) {
                    if (searchState.value) {
                        searchState.value = false
                    }
                } else {
                    if (!searchState.value) {
                        searchState.value = true
                    }
                }
            }
    }
    val eventFlow1 = viewModel.eventFlow.collectAsState(initial = null)
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
    Box() {
        Column {
            Button(
                onClick,
                modifier=Modifier.fillMaxWidth().height(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentColorGrad1),
            ) { Text("Добавить тег", color = BackgroundColor)}
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                state = scrollState
            ) {
                items(tagsList) { item ->
                    Column(){
                        val baseThisTagNotesList by viewModel.getNotesByTag(item.id).collectAsState(initial = emptyList())
                        val thisTagNotesList=baseThisTagNotesList + listOf(null)
                    Card(
                        onClick = {},
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
                        Image(
                            painter = painterResource(id = R.drawable.group_366),
                            contentDescription = "Описание иконки",
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterEnd)
                                .clickable {
                                    viewModel.DeleteTag(item)
                                },
                        )

                         }}
                        LazyRow(Modifier.fillMaxSize()) {
                            items(thisTagNotesList) { Note ->
                                if(Note!==null){
                                Card(
                                    onClick = { navController.navigate(Screen.EditNote.createRoute(false, Note.id)) },
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxHeight().width(150.dp),
                                    colors = CardDefaults.cardColors(containerColor = ColorConnect(Note.color))
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(
                                            horizontalArrangement= Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top,
                                            modifier = Modifier.fillMaxWidth())
                                        {
                                            Text(
                                                text = nameChek(Note.text.uppercase(), Note.name.uppercase()),
                                                maxLines = 2,
                                                modifier = Modifier
                                                    .padding(vertical = 4.dp)
                                                    .fillMaxWidth(0.8f),
                                                color = BackgroundColor,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Button(
                                                onClick = {},

                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .padding(top = 6.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.group_366),
                                                    contentDescription = "Описание иконки",
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = Note.text,
                                            maxLines = 7,
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            color = BackgroundColor
                                        )
                                    }
                                }}else {
                                    Box(){
                                    Image(
                                        painter = painterResource(R.drawable.group_471),
                                        contentDescription = "Image",
                                        modifier = Modifier.fillMaxSize()
                                            .padding(4.dp)
                                            .clickable {
                                                inserTagid = item.id
                                                Log.d("Тег", "Установили тег заметки для отправки в заметку: $item.id")
                                                val gson: Gson = Gson()
                                                val linesList1 = listOf(
                                                    LineWithCheckBox(
                                                        id = 0,
                                                        text = "",
                                                        isChecked = false,
                                                        hasCheckbox = false
                                                    ),
                                                )
                                                val serializedData: String = gson.toJson(linesList1)
                                                viewModel.insertNote(
                                                    name = "",
                                                    date = System.currentTimeMillis(),
                                                    dateEdited = System
                                                        .currentTimeMillis()
                                                        .toString(),
                                                    text = "",
                                                    reminder = 444,
                                                    color = "accentColorGrad1",
                                                    isHandwritten = false,
                                                            chekboxes = serializedData
                                                )
                                            },
                                        contentScale = ContentScale.Crop
                                    )
                                    }
                                }
                            }
                        }
                }
            }
        }
    }
}}

