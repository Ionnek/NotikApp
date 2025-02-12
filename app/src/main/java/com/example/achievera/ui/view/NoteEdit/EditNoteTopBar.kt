package com.example.achievera.ui.view.NoteEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.ui.theme.AccentColorGrad1
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.view.Screen
import com.example.achievera.ui.viewModel.INotesListViewModel
import com.example.achievera.ui.viewModel.NoteEditViewModel

@Composable
fun EditNoteTopBar(
    navController: NavController,
    isNew: Boolean,
    id: Long,
    viewModel: NoteEditViewModel,
    note: NotesDatabaseElement?,
    text: String,
    textName: String,
    chekboxLines:String,
    onTextNameChange: (String) -> Unit,
    BaseColor: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = BackgroundColor)
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = AccentColorGrad1),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Button(
                onClick = {
                    if (textName != "" || text != ""||!viewModel.linesListEmtyChek()) {
                            viewModel.updateNote(
                                id = id,
                                name = textName,
                                date = System.currentTimeMillis(),
                                dateEdited = "FDFDF",
                                text = viewModel.LinesToGoodTexts(),
                                reminder = 444,
                                color = BaseColor,
                                isHandwritten = false,
                                checkboxes=chekboxLines
                            )

                        navController.navigate(Screen.Home.route)
                    } else{
                        if(!isNew){viewModel.deleteNote(note!!)}}
                    navController.navigate(Screen.Home.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BackgroundColor,
                    contentColor = BackgroundColor
                ),
                modifier = Modifier.padding(horizontal = 16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text("<-", color = AccentColorGrad1)
            }
            var padding: Dp =0.dp
            if(isNew){padding = 16.dp}else{padding=0.dp}
            TextField(
                value = textName,
                onValueChange = onTextNameChange,
                singleLine = true,
                modifier = Modifier
                    .weight(1f).padding(end = padding).clip(RoundedCornerShape(16.dp)),
                placeholder = { Text("Название",) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AccentColorGrad1,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = BackgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = AccentColorGrad1,
                    unfocusedTextColor = BackgroundColor,
                    unfocusedPlaceholderColor = BackgroundColor,
                    focusedPlaceholderColor = AccentColorGrad1,
                    cursorColor = AccentColorGrad1

                )

            )
            if (!isNew) {
                Button(
                    onClick = {
                        if (!isNew) {
                            viewModel.deleteNote(note!!)
                        }
                        navController.navigate(Screen.Home.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundColor,
                        contentColor = BackgroundColor
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Text("Удалить", color = AccentColorGrad1)
                }
            }

        }
    }
}
