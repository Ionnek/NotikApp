/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view.NoteEdit

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.ui.theme.AccentColorGrad1
import com.borzykh.achievera.ui.theme.BackgroundColor
import com.borzykh.achievera.ui.theme.CardColorPurplegrad1
import com.borzykh.achievera.ui.view.NoteListing.ColorConnect
import com.borzykh.achievera.ui.view.Screen
import com.borzykh.achievera.ui.viewModel.NoteEditViewModel

@Composable
fun EditNoteTopBar(
    navController: NavController,
    isNew: Boolean,
    id: Long,
    viewModel: NoteEditViewModel,
    note: NotesDatabaseElement?,
    textName: String,
    chekboxLines:String,
    onTextNameChange: (String) -> Unit,
    onEditName:(Boolean)->Unit,
    BaseColor: String
) {
    val nameInteraction = remember { MutableInteractionSource() }
    val isNameFocused by nameInteraction.collectIsFocusedAsState()

    LaunchedEffect(isNameFocused) {
        onEditName(isNameFocused)
    }
    val imeVisible =
        WindowInsets.ime.getBottom(LocalDensity.current) > 0
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.Transparent),
    )
    {
        if (minWidth < 350.dp) {
            Column() {
                AnimatedVisibility(
                    visible = !imeVisible,
                    enter = slideInVertically { -it } + fadeIn(),
                    exit = slideOutVertically { -it } + fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(top = 50.dp)
                            .background(color = Color.Transparent),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (viewModel.currentText != "") {
                                    viewModel.updateNote(
                                        id = id,
                                        name = textName,
                                        date = System.currentTimeMillis(),
                                        dateEdited = System.currentTimeMillis().toString(),
                                        text = viewModel.currentText,
                                        reminder = 444,
                                        color = BaseColor,
                                        isHandwritten = false,
                                        checkboxes = chekboxLines
                                    )
                                    val text = viewModel.currentText
                                    Log.d("note", "note inserted $text")
                                    navController.navigate(Screen.Home.route)

                                } else {
                                    Log.d("note", "note deleted $note.id")
                                    if (!isNew) {
                                        viewModel.deleteNote(note!!)
                                    }
                                    navController.navigate(Screen.Home.route)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BackgroundColor,
                                contentColor = BackgroundColor
                            ),

                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .border(
                                    width = 4.dp,
                                    color = ColorConnect(BaseColor),
                                    shape = CircleShape
                                ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 8.dp,
                                pressedElevation = 12.dp,
                                disabledElevation = 0.dp
                            )
                        ) {
                            Text("<-", color = ColorConnect(BaseColor))
                        }
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
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .border(
                                        width = 4.dp,
                                        color = ColorConnect(BaseColor),
                                        shape = CircleShape
                                    ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 8.dp,
                                    pressedElevation = 12.dp,
                                    disabledElevation = 0.dp
                                )
                            ) {
                                Text("Удалить", color = ColorConnect(BaseColor))
                            }
                        }

                    }
                }
                Box(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = textName,
                    onValueChange = onTextNameChange,
                    interactionSource = nameInteraction,
                    singleLine = true,
                    modifier = Modifier
                        .height(60.dp)
                        .padding(end = 20.dp, start = 20.dp),
                    placeholder = { Text("Name",) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = ColorConnect(BaseColor),
                        unfocusedBorderColor = BackgroundColor,
                        focusedContainerColor = BackgroundColor,
                        focusedBorderColor = BackgroundColor,
                        focusedTextColor = ColorConnect(BaseColor),
                        unfocusedTextColor = BackgroundColor,
                        unfocusedPlaceholderColor = BackgroundColor,
                        focusedPlaceholderColor = ColorConnect(BaseColor),
                        selectionColors = TextSelectionColors(handleColor = CardColorPurplegrad1, backgroundColor = CardColorPurplegrad1),
                        cursorColor = CardColorPurplegrad1
                    ),
                    shape = OutlinedTextFieldDefaults.shape
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(top = 50.dp)
                    .background(color = Color.Transparent),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        saveNote(
                            BaseColor = BaseColor,
                            id=id,
                            textName = textName,
                            isNew = isNew,
                            note = note!!,
                            viewModel = viewModel,
                        )
                        navController.navigate(Screen.Home.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundColor,
                        contentColor = BackgroundColor
                    ),

                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .border(
                            width = 4.dp,
                            color = ColorConnect(BaseColor),
                            shape = CircleShape
                        ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Text("<-", color = ColorConnect(BaseColor))
                }
                var padding: Dp = 0.dp
                if (isNew) {
                    padding = 16.dp
                } else {
                    padding = 0.dp
                }
                OutlinedTextField(
                    value = textName,
                    onValueChange = onTextNameChange,
                    singleLine = true,
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f)
                        .padding(end = padding),
                    placeholder = { Text("Name",) },
                    interactionSource = nameInteraction,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = ColorConnect(BaseColor),
                        unfocusedBorderColor = BackgroundColor,
                        focusedContainerColor = BackgroundColor,
                        focusedBorderColor = BackgroundColor,
                        focusedTextColor = ColorConnect(BaseColor),
                        unfocusedTextColor = BackgroundColor,
                        unfocusedPlaceholderColor = BackgroundColor,
                        focusedPlaceholderColor = ColorConnect(BaseColor),
                        cursorColor = ColorConnect(BaseColor)
                    ),
                    shape = OutlinedTextFieldDefaults.shape
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
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .border(
                                width = 4.dp,
                                color = ColorConnect(BaseColor),
                                shape = CircleShape
                            ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 12.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Text("Delete", color = ColorConnect(BaseColor))
                    }
                }

            }
        }
    }
}
@Preview
@Composable
fun prev(){
    ScreenNoteEdit(
        id = 0,
        isNew1 = false,
        navController = rememberNavController()
    )
}