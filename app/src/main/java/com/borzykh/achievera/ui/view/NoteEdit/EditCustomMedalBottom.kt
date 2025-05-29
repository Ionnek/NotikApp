/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view.NoteEdit

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
import com.borzykh.achievera.ui.theme.AccentColorGrad1
import com.borzykh.achievera.ui.viewModel.INotesListViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.borzykh.achievera.data.model.Tag
import com.borzykh.achievera.ui.theme.BackgroundColor
import com.borzykh.achievera.ui.theme.CardColorPurplegrad1
import com.borzykh.achievera.ui.view.NoteListing.ColorConnect
import com.borzykh.achievera.ui.viewModel.NoteEditViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTagsMedalBottom(onDialogShownChange: () -> Unit,viewModel: NoteEditViewModel,id:Long){
    val tagsList by viewModel.NoteTags.collectAsState(initial = emptyList())
    val sheetState = rememberModalBottomSheetState()
    CustomTagsMedalBottomStateless(
        sheetState =sheetState,
        onDialogShownChange = onDialogShownChange,
        tagsList=tagsList,
        onTagInsertClick={tagId -> viewModel.InsertCurrentNoteTag(id, tagId)},
        onTagDeleteClick={tagId -> viewModel.DeleteCurrentNoteTag(id, tagId)},
        onNewTagClick = {tagText,tagColor,isActive-> viewModel.InsertTag(id,tagText, tagColor, isActive)}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTagsMedalBottomStateless(
    sheetState: SheetState,
    onDialogShownChange: () -> Unit,
    tagsList:List<Tag>,
    onTagInsertClick:(Long)->Unit,
    onTagDeleteClick:(Long)->Unit,
    onNewTagClick:(String,String,Boolean)->Unit
){
    var tagText by rememberSaveable { mutableStateOf("") }
    val usefulColorList = listOf("Blue", "Green", "Red", "Yellow", "DarkPurple", "Purple")
    var tagColor by rememberSaveable { mutableStateOf("") }
    ModalBottomSheet(onDismissRequest = onDialogShownChange, sheetState = sheetState, containerColor = AccentColorGrad1 ) {
        Box(modifier = Modifier.wrapContentSize()) {
            Column {
                LazyRow(modifier = Modifier.height(40.dp)) {
                    items(tagsList) { item ->
                        Card(
                            onClick = {
                                if (!item.isActived) {
                                    onTagInsertClick(item.id)
                                } else {
                                    onTagDeleteClick(item.id)
                                }
                            },
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
                            Column(
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .fillMaxSize(), verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = item.name,
                                    modifier = Modifier.padding(vertical = 4.dp),
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
                BoxWithConstraints {
                    if(minWidth>350.dp){
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = tagText,
                        onValueChange = { tagText = it },
                        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                        placeholder = { Text("Введите тэг") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = BackgroundColor,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = BackgroundColor,
                            focusedIndicatorColor = AccentColorGrad1,
                            focusedTextColor = AccentColorGrad1,
                            unfocusedTextColor = AccentColorGrad1,
                            unfocusedPlaceholderColor = AccentColorGrad1,
                            focusedPlaceholderColor = AccentColorGrad1,
                            cursorColor = CardColorPurplegrad1,
                            selectionColors = TextSelectionColors(handleColor = CardColorPurplegrad1, backgroundColor = CardColorPurplegrad1),
                        ),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (tagText != "") {
                                onNewTagClick(tagText, tagColor, false)
                                tagText = ""
                            }
                        }, modifier = Modifier.height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BackgroundColor,
                            contentColor = BackgroundColor
                        )
                    ) {
                        Text("ADD", color = AccentColorGrad1)
                    }}
                }else{
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextField(
                                value = tagText,
                                onValueChange = { tagText = it },
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                                placeholder = { Text("Write tag") },
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = BackgroundColor,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedContainerColor = BackgroundColor,
                                    focusedIndicatorColor = AccentColorGrad1,
                                    focusedTextColor = AccentColorGrad1,
                                    unfocusedTextColor = AccentColorGrad1,
                                    unfocusedPlaceholderColor = AccentColorGrad1,
                                    focusedPlaceholderColor = AccentColorGrad1,
                                    selectionColors = TextSelectionColors(handleColor = CardColorPurplegrad1, backgroundColor = CardColorPurplegrad1),
                                    cursorColor = CardColorPurplegrad1
                                ),
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    if (tagText != "") {
                                        onNewTagClick(tagText, tagColor, false)
                                        tagText = ""
                                    }
                                }, modifier = Modifier.height(60.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BackgroundColor,
                                    contentColor = BackgroundColor
                                )
                            ) {
                                Text("ADD", color = AccentColorGrad1)
                            }}
                    }
                }

                LazyRow(
                    content = {
                        items(usefulColorList) { item ->
                            Button(
                                onClick = {
                                    tagColor = item
                                },

                                enabled = tagColor != item,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ColorConnect(item),
                                    contentColor = Color.Black,
                                    disabledContentColor = Color.White,
                                    disabledContainerColor = ColorConnect(item)
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(40.dp)
                            ) {
                                Text(text = item)
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Custom2TagsMedalBottom(onDialogShownChange: () -> Unit,viewModel: INotesListViewModel) {
    val sheetState = rememberModalBottomSheetState()
    var tagText by rememberSaveable { mutableStateOf("") }
    val usefulColorList = listOf("Blue", "Green", "Red", "Yellow", "DarkPurple", "Purple")
    var tagColor by rememberSaveable { mutableStateOf("") }
    ModalBottomSheet(
        onDialogShownChange,
        sheetState = sheetState,
        containerColor = AccentColorGrad1
    ) {
        Box(modifier = Modifier.wrapContentSize()) {
            Column {
                BoxWithConstraints {
                    if (minWidth>350.dp) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = tagText,
                                onValueChange = { tagText = it },
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                                placeholder = { Text("Write tag") },
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = BackgroundColor,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedContainerColor = BackgroundColor,
                                    focusedIndicatorColor = AccentColorGrad1,
                                    focusedTextColor = AccentColorGrad1,
                                    unfocusedTextColor = AccentColorGrad1,
                                    unfocusedPlaceholderColor = AccentColorGrad1,
                                    focusedPlaceholderColor = AccentColorGrad1,
                                    selectionColors = TextSelectionColors(handleColor = CardColorPurplegrad1, backgroundColor = CardColorPurplegrad1),
                                    cursorColor = CardColorPurplegrad1
                                ),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (tagText != "") {
                                        viewModel.InsertStandaloneTag(tagText, tagColor, false)
                                        tagText = ""
                                    }
                                }, modifier = Modifier.height(60.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BackgroundColor,
                                    contentColor = BackgroundColor
                                )
                            ) {
                                Text("ADD", color = AccentColorGrad1)
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextField(
                                value = tagText,
                                onValueChange = { tagText = it },
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                                placeholder = { Text("Write tag") },
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = BackgroundColor,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedContainerColor = BackgroundColor,
                                    focusedIndicatorColor = AccentColorGrad1,
                                    focusedTextColor = AccentColorGrad1,
                                    unfocusedTextColor = AccentColorGrad1,
                                    unfocusedPlaceholderColor = AccentColorGrad1,
                                    focusedPlaceholderColor = AccentColorGrad1,
                                    selectionColors = TextSelectionColors(handleColor = CardColorPurplegrad1, backgroundColor = CardColorPurplegrad1),
                                    cursorColor = CardColorPurplegrad1
                                ),
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    if (tagText != "") {
                                        viewModel.InsertStandaloneTag(tagText, tagColor, false)
                                        tagText = ""
                                    }
                                }, modifier = Modifier.height(60.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BackgroundColor,
                                    contentColor = BackgroundColor
                                )
                            ) {
                                Text("ADD", color = AccentColorGrad1)
                            }
                        }
                    }
                }
                        LazyRow(
                            content = {
                                items(usefulColorList) { item ->
                                    Button(
                                        onClick = {
                                            tagColor = item
                                        },
                                        enabled = tagColor != item,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = ColorConnect(item),
                                            contentColor = Color.Black,
                                            disabledContentColor = Color.White,
                                            disabledContainerColor = ColorConnect(item)
                                        ),
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .height(40.dp)
                                    ) {
                                        Text(text = item)
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                        )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Custom3TagsMedalBottom(
        onDialogShownChange: () -> Unit,
        viewModel: INotesListViewModel,
        tag: Tag
) {
        val sheetState = rememberModalBottomSheetState()
        var tagText by rememberSaveable { mutableStateOf(tag.name) }
        val usefulColorList = listOf("Blue", "Green", "Red", "Yellow", "DarkPurple", "Purple")
        var tagColor by rememberSaveable { mutableStateOf(tag.color) }
        ModalBottomSheet(
            onDialogShownChange,
            sheetState = sheetState,
            containerColor = AccentColorGrad1
        ) {
            Box() {
                Column {
                    BoxWithConstraints {
                        if (minWidth>350.dp) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    value = tagText,
                                    onValueChange = { tagText = it },
                                    modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                                    placeholder = { Text("Введите тэг") },
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = BackgroundColor,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedContainerColor = BackgroundColor,
                                        focusedIndicatorColor = AccentColorGrad1,
                                        focusedTextColor = AccentColorGrad1,
                                        unfocusedTextColor = AccentColorGrad1,
                                        unfocusedPlaceholderColor = AccentColorGrad1,
                                        focusedPlaceholderColor = AccentColorGrad1,
                                        selectionColors = TextSelectionColors(handleColor = CardColorPurplegrad1, backgroundColor = CardColorPurplegrad1),
                                        cursorColor = CardColorPurplegrad1
                                    ),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (tagText != "") {
                                            viewModel.updateTag(tag, tagText, tagColor)
                                        }
                                    }, modifier = Modifier.height(60.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = BackgroundColor,
                                        contentColor = BackgroundColor
                                    )
                                ) {
                                    Text("Edit", color = AccentColorGrad1)
                                }
                            }
                        }else{
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextField(
                                    value = tagText,
                                    onValueChange = { tagText = it },
                                    modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                                    placeholder = { Text("Введите тэг") },
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = BackgroundColor,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedContainerColor = BackgroundColor,
                                        focusedIndicatorColor = AccentColorGrad1,
                                        focusedTextColor = AccentColorGrad1,
                                        unfocusedTextColor = AccentColorGrad1,
                                        unfocusedPlaceholderColor = AccentColorGrad1,
                                        focusedPlaceholderColor = AccentColorGrad1,
                                        selectionColors = TextSelectionColors(handleColor = CardColorPurplegrad1, backgroundColor = CardColorPurplegrad1),
                                        cursorColor = CardColorPurplegrad1
                                    ),
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        if (tagText != "") {
                                            viewModel.updateTag(tag, tagText, tagColor)
                                        }
                                    }, modifier = Modifier.height(60.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = BackgroundColor,
                                        contentColor = BackgroundColor
                                    )
                                ) {
                                    Text("Edit", color = AccentColorGrad1)
                                }
                            }
                        }
                    }

                    LazyRow(
                        content = {
                            items(usefulColorList) { item ->
                                Button(
                                    onClick = {
                                        tagColor = item
                                    },
                                    enabled = tagColor != item,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ColorConnect(item),
                                        contentColor = Color.Black,
                                        disabledContentColor = Color.White,
                                        disabledContainerColor = ColorConnect(item)
                                    ),
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .height(40.dp)
                                ) {
                                    Text(text = item)
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun prevTagModal(){
    var tagText by rememberSaveable { mutableStateOf("") }
    val usefulColorList = listOf("Blue", "Green", "Red", "Yellow", "DarkPurple", "Purple")
    var tagColor by rememberSaveable { mutableStateOf("") }
    Box (){
        Column {
            Row(modifier = Modifier.padding(10.dp),verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = tagText,
                    onValueChange = { tagText = it },
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                    placeholder = { Text("Введите тэг") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = BackgroundColor,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = BackgroundColor,
                        focusedIndicatorColor = AccentColorGrad1,
                        focusedTextColor = AccentColorGrad1,
                        unfocusedTextColor = AccentColorGrad1,
                        unfocusedPlaceholderColor = AccentColorGrad1,
                        focusedPlaceholderColor = AccentColorGrad1,
                        selectionColors = TextSelectionColors(handleColor = CardColorPurplegrad1, backgroundColor = CardColorPurplegrad1),
                        cursorColor = CardColorPurplegrad1),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if(tagText!="") {
                    }
                },modifier = Modifier.height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundColor,
                        contentColor = BackgroundColor
                    )) {
                    Text("Edit", color = AccentColorGrad1)
                }
            }

            LazyRow(
                content = {
                    items(usefulColorList) { item ->
                        Button(
                            onClick = {
                                tagColor = item
                            },
                            enabled = tagColor != item,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ColorConnect(item),
                                contentColor = Color.Black,
                                disabledContentColor = Color.White,
                                disabledContainerColor = ColorConnect(item)
                            ),
                            modifier = Modifier
                                .padding(8.dp)
                                .height(40.dp)
                        ) {
                            Text(text = item)
                        }
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}