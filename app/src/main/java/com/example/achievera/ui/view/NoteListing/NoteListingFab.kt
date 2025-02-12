package com.example.achievera.ui.view.NoteListing

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.achievera.ui.theme.AccentColorGrad1
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.view.NoteEdit.LineWithCheckBox
import com.example.achievera.ui.view.Screen
import com.example.achievera.ui.viewModel.INotesListViewModel
import com.example.achievera.ui.viewModel.NotesListEvent
import com.google.gson.Gson

@Composable
fun BarFab1(NavController: NavController,
            VM: INotesListViewModel,
            searchState: MutableState<Boolean>
){
    val OffsetHeight by animateDpAsState(
        targetValue = if (searchState.value) 0.dp else -25.dp,
        animationSpec = tween(durationMillis = 300)
    )
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val eventFlow = VM.eventFlow.collectAsState(initial = null)
    LaunchedEffect(eventFlow.value) {
        eventFlow.value?.let { event ->
            when (event) {
                is NotesListEvent.NoteInserted -> {
                    NavController.navigate(Screen.EditNote.createRoute(false, event.noteId))
                }
            }
        }
    }

    val fabSize: Dp = screenWidth * 0.20f


    val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')


    fun getRandomString(length: Int): String {
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    FloatingActionButton(
        onClick = {
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
            VM.insertNote(
                name = "",
                date = System.currentTimeMillis(),
                dateEdited = System.currentTimeMillis().toString(),
                text = "",
                reminder = 444,
                color = "accentColorGrad1",
                isHandwritten = false,
                chekboxes = serializedData
            )
        },
        modifier = Modifier
            .size(fabSize)
            .offset(y = (OffsetHeight),x = -10.dp)
            .border(
                width = 4.dp,
                color = AccentColorGrad1,
                shape = CircleShape
            ),
        containerColor = BackgroundColor,
        shape = CircleShape
    ) {
        Icon(
            modifier = Modifier
                .size(fabSize*0.5f),
            imageVector = Icons.Default.Add,
            contentDescription = "Добавить",
            tint = AccentColorGrad1,
        )
    }
}