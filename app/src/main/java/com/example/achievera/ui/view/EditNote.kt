package com.example.achievera.ui.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.ui.theme.AccentColorGrad1
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.theme.TestMultyScreensTheme
import com.example.achievera.ui.viewModel.INotesListViewModel
import com.example.achievera.ui.viewModel.MockNotesListViewModel

@Composable
fun ScreenNoteEdit(navController: NavController, isNew1: Boolean, id: Long, viewModel: INotesListViewModel) {
    var isNew:Boolean= isNew1
    var text by rememberSaveable { mutableStateOf("") }
    var textName by rememberSaveable { mutableStateOf("") }
    // Загружаем заметку при изменении id или isNew
    LaunchedEffect(key1 = id, key2 = isNew) {
        if (!isNew) {
            viewModel.getNoteById(id)
        }
    }
    val UsefulColorList= listOf("Blue","Green","Red","Yellow","DarkPurple","Purple")
    // Наблюдаем за заметкой из ViewModel
    val note by viewModel.note.observeAsState()
    var BaseColor by rememberSaveable { mutableStateOf("purple")}
    if(isNew){
    BaseColor ="purple"}
    // Когда заметка загружена, устанавливаем текст
    LaunchedEffect(note) {
        if (note != null && !isNew) {
            text = note!!.text
            textName=note!!.name
            BaseColor =note!!.color
        }
    }
    Scaffold (
        topBar = { EditNoteTopBar(navController = navController,
            isNew = isNew,
            id = id,
            viewModel = viewModel,
            note = note,
            text = text,
            textName = textName,
            onTextNameChange = { textName = it },
            BaseColor=BaseColor)
        },
        //=================================
        bottomBar = { EditNoteBotomBar(
            UsefulColorList=UsefulColorList,
            isNew = isNew,
            vm = viewModel,
            note = note,
            text = text,
            textName = textName,
            onColorChange = {BaseColor=it },
            BaseColor = BaseColor
            )
        }
    ){
        innerPadding ->
        Column(modifier = Modifier.fillMaxSize().background(color = BackgroundColor).padding(innerPadding)) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth().fillMaxHeight(0.9f)
                    .padding(16.dp),
                placeholder = { Text("Введите текст заметки") },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = ColorConnect(BaseColor), unfocusedIndicatorColor = Color.Transparent, focusedContainerColor = ColorConnect(BaseColor), focusedIndicatorColor = Color.Transparent, focusedTextColor = BackgroundColor, unfocusedTextColor = BackgroundColor, unfocusedPlaceholderColor = BackgroundColor)
            )
            Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()){
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(0.8f).fillMaxHeight().padding(16.dp),
                    value = text,
                    onValueChange = { text = it },
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = ColorConnect(BaseColor), unfocusedIndicatorColor = Color.Transparent, focusedContainerColor = ColorConnect(BaseColor), focusedIndicatorColor = Color.Transparent, focusedTextColor = BackgroundColor, unfocusedTextColor = BackgroundColor, unfocusedPlaceholderColor = BackgroundColor)
                )
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(top = 16.dp,end = 16.dp, bottom = 16.dp).border(
                        width = 4.dp,
                        color = AccentColorGrad1,
                        shape = CircleShape // Устанавливаем круглую форму границы
                    ),
                    colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor,contentColor = BackgroundColor)
                ){}
            }
        }
    }
    BackHandler {
        if(textName!=""||text!="") {
            if (isNew) {
                viewModel.insertNote(
                    name = textName, // Замените на реальное имя
                    date = System.currentTimeMillis(), // Текущая дата в миллисекундах
                    dateEdited = "GFGFG", // Замените на реальную дату редактирования
                    text = text,
                    reminder = 444, // Замените на реальное значение или сделайте переменной
                    color = BaseColor, // Пример цвета или сделайте переменной
                    isHandwritten = false
                )
            } else {
                viewModel.updateNote(
                    id = id,
                    name = textName, // Замените на реальное имя
                    date = System.currentTimeMillis(),
                    dateEdited = "FDFDF", // Замените на реальную дату редактирования
                    text = text,
                    reminder = 444, // Замените на реальное значение или сделайте переменной
                    color = BaseColor, // Пример цвета или сделайте переменной
                    isHandwritten = false
                )
            }
            navController.navigate(Screen.Home.route)
        }else
            if(!isNew){viewModel.deleteNote(note!!)}
        navController.navigate(Screen.Home.route)
    }
}

@Composable
fun EditNoteBotomBar(UsefulColorList: List<String>,isNew: Boolean,note: NotesDatabaseElement?,vm: INotesListViewModel,text: String,textName: String,onColorChange: (String) -> Unit,BaseColor:String) {
    var isColorSelectionRegime by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = BackgroundColor)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = AccentColorGrad1),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically

        ) {
            if(!isColorSelectionRegime){
                Button(onClick = {isColorSelectionRegime=true}, modifier = Modifier.border(
                    width = 4.dp,
                    color = BackgroundColor,
                    shape = CircleShape // Устанавливаем круглую форму границы
                ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (note != null) {
                        ColorConnect(BaseColor)
                        } else {
                            ColorConnect(BaseColor)
                        }, // Цвет фона кнопки
                        contentColor = if (note != null) {
                            ColorConnect(BaseColor)
                        } else {
                            ColorConnect(BaseColor)
                        }      // Цвет содержимого кнопки (текст, иконки)
                    ),
                ) { }
            }
            else{
                LazyRow (){
                    items(UsefulColorList) { item ->Button(onClick = {
                        onColorChange(item)
                        isColorSelectionRegime=false
                        if(!isNew){
                            vm.getNoteById(note!!.id)
                        }
                    },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorConnect(item), // Цвет фона кнопки
                            contentColor = ColorConnect(item)      // Цвет содержимого кнопки (текст, иконки)
                        ),) { }}
                }
            }
        }
    }
}
@Composable
fun EditNoteTopBar(
    navController: NavController,
    isNew: Boolean,
    id: Long,
    viewModel: INotesListViewModel,
    note: NotesDatabaseElement?,
    text: String,
    textName: String,
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
                        if (textName != "" || text != "") {
                            if (isNew) {
                                viewModel.insertNote(
                                    name = textName, // Замените на реальное имя
                                    date = System.currentTimeMillis(), // Текущая дата в миллисекундах
                                    dateEdited = "GFGFG", // Замените на реальную дату редактирования
                                    text = text,
                                    reminder = 444, // Замените на реальное значение или сделайте переменной
                                    color = BaseColor, // Пример цвета или сделайте переменной
                                    isHandwritten = false
                                )
                            } else {
                                viewModel.updateNote(
                                    id = id,
                                    name = textName, // Замените на реальное имя
                                    date = System.currentTimeMillis(),
                                    dateEdited = "FDFDF", // Замените на реальную дату редактирования
                                    text = text,
                                    reminder = 444, // Замените на реальное значение или сделайте переменной
                                    color = BaseColor, // Пример цвета или сделайте переменной
                                    isHandwritten = false
                                )
                            }
                            navController.navigate(Screen.Home.route)
                        } else{
                            if(!isNew){viewModel.deleteNote(note!!)}}
                        navController.navigate(Screen.Home.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundColor, // Цвет фона кнопки
                        contentColor = BackgroundColor      // Цвет содержимого кнопки (текст, иконки)
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
                        unfocusedPlaceholderColor = BackgroundColor
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
                            containerColor = BackgroundColor, // Цвет фона кнопки
                            contentColor = BackgroundColor      // Цвет содержимого кнопки (текст, иконки)
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

@Preview
@Composable
fun prev(){
    val navController = rememberNavController()
    val VM= MockNotesListViewModel()
    TestMultyScreensTheme {
        Surface {
            ScreenNoteEdit(navController,false,0,VM)
        }
    }
}