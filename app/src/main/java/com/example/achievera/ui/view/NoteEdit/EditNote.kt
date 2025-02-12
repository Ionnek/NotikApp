package com.example.achievera.ui.view.NoteEdit

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.theme.TestMultyScreensTheme
import com.example.achievera.ui.view.NoteListing.ColorConnect
import com.example.achievera.ui.view.Screen
import com.example.achievera.ui.viewModel.INotesListViewModel
import com.example.achievera.ui.viewModel.MockNotesListViewModel
import com.example.achievera.ui.viewModel.NoteEditViewModel
import com.example.achievera.ui.viewModel.NotesListViewModel

@Composable
fun ScreenNoteEdit(
    navController: NavController,
    isNew1: Boolean,
    id: Long,
) {
    val viewModel: NoteEditViewModel = hiltViewModel()
    viewModel.setNoteId(id)
    val isImageActive = viewModel.isImageActive
    Log.d("NotesListViewModel", "Открывается заметка с id: $id")
    viewModel.updateActiveTags(id)
    var isNew: Boolean = isNew1
    var text by rememberSaveable { mutableStateOf("") }
    var chekboxesLines by rememberSaveable { mutableStateOf("") }
    var textName by rememberSaveable { mutableStateOf("") }
    var isDialogShown by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val UsefulColorList = listOf("Blue", "Green", "Red", "Yellow", "DarkPurple", "Purple")
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val note by viewModel.note.observeAsState()
    var BaseColor by rememberSaveable { mutableStateOf("purple") }
    if (isNew) {
        BaseColor = "purple"
    }
    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia(),
        onResult = { uri: Uri? ->
            uri?.let {
                // Сохранение URI в ViewModel или другом хранилище
                viewModel.InsertNewPhoto(0, noteId = id, it.toString())

                // Флаги только для чтения и записи
                val takeFlags: Int =
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION

                // Сохранение разрешений на URI
                if (it.scheme == "content") {
                    try {
                        context.contentResolver.takePersistableUriPermission(it, takeFlags)
                    } catch (e: SecurityException) {
                        Log.e("PhotoPicker", "Ошибка при сохранении URI разрешений: ${e.message}")
                    }
                } else {
                    Log.w("PhotoPicker", "URI не поддерживает persistable разрешения")
                }
            }
        }

    )
    LaunchedEffect(key1 = id) {
        viewModel.getNoteById(id)
        viewModel.GetNotePhotos(id)
    }
    LaunchedEffect(note) {
        if (note != null && !isNew) {
            text = note!!.text
            textName = note!!.name
            BaseColor = note!!.color
            chekboxesLines=note!!.chekboxes
            if (note!!.chekboxes.isNotEmpty()) {
                viewModel.assignLinesList(note!!.chekboxes)
            }else{viewModel.addEmptyLine()}
        }
        Log.d("Текст", "пришел текст: $note.text")
        Log.d("Имя", "пришел текст: $note.text")
        Log.d("Цвет", "пришел текст: $note.text")
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Scaffold(
            topBar = {
                EditNoteTopBar(
                    navController = navController,
                    isNew = isNew,
                    id = id,
                    viewModel = viewModel,
                    note = note,
                    text = text,
                    textName = textName,
                    onTextNameChange = { textName = it },
                    BaseColor = BaseColor,
                    chekboxLines = chekboxesLines
                )
            },
            //=================================
            bottomBar = {

                EditNoteBotomBar(
                    UsefulColorList = UsefulColorList,
                    isNew = isNew,
                    vm = viewModel,
                    note = note,
                    onColorChange = { BaseColor = it },
                    BaseColor = BaseColor,
                    onClick = { isDialogShown = !isDialogShown },
                    Ptpkr = photoPickerLauncher,
                    onTextChekboxChange = {
                        chekboxesLines = it
                        viewModel.assignLinesList(chekboxesLines)
                    },
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()).background(color = BackgroundColor)) {
                Box(Modifier.height(20.dp).fillMaxWidth().background(color = BackgroundColor)){}
                imageCarousel(id = id, viewModel = viewModel,Ptpkr=photoPickerLauncher)
                if (viewModel.linesList.isNotEmpty()){
                    Box(Modifier.height(20.dp).fillMaxWidth().background(color = BackgroundColor)){}
                ChekboxLinesEditor(
                    onTextChekboxChange = {
                        chekboxesLines = it
                    },
                    baseColor = BaseColor,
                    viewModel=viewModel,
                    screenHeight=screenHeight,
                    screenWidth=screenWidth
                )}
                Box(Modifier.height(200.dp).fillMaxWidth().background(color = BackgroundColor)){}
                /*InlineCheckboxField_DualLayer(
                    text = text,
                    onValueChange = { text = it },
                    BaseColor = BaseColor,
                    id = id,
                    innerPadding = innerPadding,
                    onTextChekboxChange = {
                        chekboxesLines = it
                    },
                )*/
            }

            if (isDialogShown) {
                CustomTagsMedalBottom({ isDialogShown = false }, viewModel = viewModel, id = id)
            }
        }
    }
    BackHandler {
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
                    checkboxes = chekboxesLines
                )

            navController.navigate(Screen.Home.route)
        } else
            if (!isNew) {
                viewModel.deleteNote(note!!)
            }
        navController.navigate(Screen.Home.route)
    }
}

@Preview
@Composable
fun prev() {
    val navController = rememberNavController()
    val VM = MockNotesListViewModel()
    TestMultyScreensTheme {
        Surface {
            ScreenNoteEdit(navController, false, 0)
        }
    }
}