package com.example.achievera.ui.view.NoteEdit

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.view.NoteListing.ColorConnect
import com.example.achievera.ui.viewModel.NoteEditViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTagsMedalBottom(onDialogShownChange: () -> Unit,viewModel: NoteEditViewModel,id:Long){
    val sheetState = rememberModalBottomSheetState()
    val tagsList by viewModel.NoteTags.collectAsState(initial = emptyList())
    var tagText by rememberSaveable { mutableStateOf("") }
    val usefulColorList = listOf("Blue", "Green", "Red", "Yellow", "DarkPurple", "Purple")
    var tagColor by rememberSaveable { mutableStateOf("") }
    ModalBottomSheet(onDialogShownChange, sheetState = sheetState, containerColor = AccentColorGrad1 ) {
        Box (){
            Column {
                LazyRow( modifier = Modifier.height(40.dp)) {
                    items(tagsList){ item->
                        Card(
                            onClick = {if(!item.isActived){
                                viewModel.InsertCurrentNoteTag(id,item.id)
                            }else{
                                viewModel.DeleteCurrentNoteTag(id,item.id)
                            } },
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxHeight(),
                            colors = CardDefaults.cardColors(containerColor =
                            if(item.isActived){
                                BackgroundColor
                            }
                            else{
                                ColorConnect(item.color)
                            }
                            ),
                        ) {
                            Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                Text(
                                    text = item.name,
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = if(item.isActived){ColorConnect(item.color)}else{
                                        BackgroundColor
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }}
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
                            cursorColor = AccentColorGrad1),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if(tagText!="") {
                            viewModel.InsertTag(id,tagText, tagColor, false)
                            tagText=""
                        }
                    },modifier = Modifier.height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BackgroundColor,
                            contentColor = BackgroundColor
                        )) {
                        Text("ADD", color = AccentColorGrad1)
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
fun Custom2TagsMedalBottom(onDialogShownChange: () -> Unit,viewModel: INotesListViewModel){
    val sheetState = rememberModalBottomSheetState()
    var tagText by rememberSaveable { mutableStateOf("") }
    val usefulColorList = listOf("Blue", "Green", "Red", "Yellow", "DarkPurple", "Purple")
    var tagColor by rememberSaveable { mutableStateOf("") }
    ModalBottomSheet(onDialogShownChange, sheetState = sheetState, containerColor = AccentColorGrad1 ) {
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
                            cursorColor = AccentColorGrad1),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if(tagText!="") {
                            viewModel.InsertStandaloneTag(tagText, tagColor, false)
                            tagText=""
                        }
                    },modifier = Modifier.height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BackgroundColor,
                            contentColor = BackgroundColor
                        )) {
                        Text("ADD", color = AccentColorGrad1)
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