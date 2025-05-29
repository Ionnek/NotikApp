/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view.NoteEdit

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.ui.theme.AccentColorGrad1
import com.borzykh.achievera.ui.theme.BackgroundColor
import com.borzykh.achievera.ui.view.HandleAppLeaving
import com.borzykh.achievera.ui.view.Screen
import com.borzykh.achievera.ui.viewModel.NoteEditViewModel

@Composable
fun ScreenNoteEdit(
    navController: NavController,
    isNew1: Boolean,
    id: Long,
) {
    val focusManager       = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val viewModel: NoteEditViewModel = hiltViewModel()
    viewModel.setNoteId(id)
    Log.d("NotesListViewModel", "Note opened с id: $id")
    viewModel.updateActiveTags(id)
    var isNew: Boolean = isNew1
    var addCheckboxFunc by remember { mutableStateOf<() -> Unit>({}) }
    var paddingTop by remember { mutableStateOf(0)}
    var text by rememberSaveable { mutableStateOf("") }
    var textName by rememberSaveable { mutableStateOf("") }
    var isDialogShown by rememberSaveable { mutableStateOf(false) }
    var isNameEditing by rememberSaveable { mutableStateOf(false) }
    val UsefulColorList = listOf("Blue", "Green", "Red", "Yellow", "DarkPurple", "Purple")
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
                viewModel.InsertNewPhoto(0, noteId = id, it.toString())

                val takeFlags: Int =
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION

                if (it.scheme == "content") {
                    try {
                        context.contentResolver.takePersistableUriPermission(it, takeFlags)
                    } catch (e: SecurityException) {
                        Log.e("PhotoPicker", "Error saving URI permiss: ${e.message}")
                    }
                } else {
                    Log.w("PhotoPicker", "URI not supported persistable permissions")
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
            viewModel.onTextChange(note!!.text)
            if (note!!.chekboxes.isNotEmpty()) {
            }else{}
        }
        Log.d("Note", "text arrived: $note.text")
        Log.d("Note", "name arrived: $note.text")
        Log.d("Note", "color arrived: $note.text")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AccentColorGrad1)
    ) {
        Scaffold(
            topBar = {
                EditNoteTopBar(
                    navController = navController,
                    isNew = isNew,
                    id = id,
                    viewModel = viewModel,
                    note = note,
                    textName = textName,
                    onTextNameChange = { textName = it },
                    BaseColor = BaseColor,
                    onEditName={isNameEditing= it },
                    chekboxLines = "chekboxesLines"
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
                    onChekClick = addCheckboxFunc
                )
            },
            contentWindowInsets = WindowInsets.safeDrawing
                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),

            ) { innerPadding ->
            paddingTop=innerPadding.calculateTopPadding().value.toInt()
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .background(color = BackgroundColor)) {
                    Box(
                        Modifier.height(innerPadding.calculateTopPadding()).fillMaxWidth().background(color = BackgroundColor)
                    ) {}
                    imageCarousel(id = id, viewModel = viewModel, Ptpkr = photoPickerLauncher)
                    Box(
                        Modifier.height(10.dp).fillMaxWidth().background(color = BackgroundColor)
                    ) {}
                    addCheckboxFunc = InlineCheckboxField_DualLayer(
                        BaseColor = BaseColor,
                        vm = viewModel
                    )
                    Box(
                        Modifier.height(120.dp).fillMaxWidth().background(color = BackgroundColor)
                    ) {}


                    if (isDialogShown) {
                        CustomTagsMedalBottom(
                            { isDialogShown = false },
                            viewModel = viewModel,
                            id = id
                        )
                    }
                }
    }
    BackHandler {
        saveNote(
            BaseColor = BaseColor,
            id=id,
            textName = textName,
            isNew = isNew,
            note = note!!,
            viewModel = viewModel,
        )
        navController.navigate(Screen.Home.route)
    }
    HandleAppLeaving(
            onDestroy = {saveNote(
                BaseColor = BaseColor,
                id=id,
                textName = textName,
                isNew = isNew,
                note = note!!,
                viewModel = viewModel,
            )},
            onStop = {saveNote(
                BaseColor = BaseColor,
                id=id,
                textName = textName,
                isNew = isNew,
                note = note!!,
                viewModel = viewModel,
            )},
            onPause = {saveNote(
                BaseColor = BaseColor,
                id=id,
                textName = textName,
                isNew = isNew,
                note = note!!,
                viewModel = viewModel,
            ) },
        )

        if (isNameEditing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingTop.dp)
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        focusManager.clearFocus(force = true)
                        keyboardController?.hide()
                        isNameEditing = false
                    }
            )
        }
}

}
fun saveNote(
    viewModel: NoteEditViewModel,
             id: Long,textName:String,
             BaseColor:String,
             isNew: Boolean,
             note:NotesDatabaseElement){
    if (viewModel.currentText != "") {
        viewModel.updateNote(
            id = id,
            name = textName,
            date = System.currentTimeMillis(),
            dateEdited = "FDFDF",
            text = viewModel.currentText,
            reminder = 444,
            color = BaseColor,
            isHandwritten = false,
            checkboxes = "chekboxesLines"
        )
    } else
        if (!isNew) {
            viewModel.deleteNote(note)
        }
}
