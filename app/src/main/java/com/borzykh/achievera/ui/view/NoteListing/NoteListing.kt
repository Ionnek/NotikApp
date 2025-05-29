/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view.NoteListing

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.borzykh.achievera.data.model.NotesDatabaseElement
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.borzykh.achievera.ui.theme.AccentColorGrad1
import com.borzykh.achievera.ui.theme.BackgroundColor
import com.borzykh.achievera.ui.theme.DarkBackgroundGrad1
import com.borzykh.achievera.ui.theme.DarkBackgroundGrad2
import com.borzykh.achievera.ui.view.NoteEdit.Custom2TagsMedalBottom
import com.borzykh.achievera.ui.view.Screen
import com.borzykh.achievera.ui.viewModel.INotesListViewModel

@Composable
fun ScreenNotesListing1(NavController: NavController,VM: INotesListViewModel) {
    val pageNotes:LazyPagingItems<NotesDatabaseElement> =VM.notes.collectAsLazyPagingItems()
    val searchQuery=rememberSaveable { mutableStateOf("") }
    val searchState=rememberSaveable { mutableStateOf(true) }
    val isTopBarMenuPressed= rememberSaveable{ mutableStateOf(false)}
    var isDialogShown by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
        VM.setQuery(query)
    }
        VM.updateTagsToActiveFilter()

    Box(modifier=Modifier.fillMaxSize().background(color = BackgroundColor)) {
        if(isTopBarMenuPressed.value) {
            Dialog({ isTopBarMenuPressed.value = !isTopBarMenuPressed.value }) {

                Box (modifier = Modifier.background(Color.Transparent)){
                    Surface(
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = AccentColorGrad1
                    ){
                Column() {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button({
                        NavController.navigate(Screen.TagEdit.route)
                    },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).border(
                            width = 4.dp,
                            color = BackgroundColor,
                            shape = CircleShape
                        ),
                        colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor)) {
                        Text("Tags", color = AccentColorGrad1)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button({
                        NavController.navigate(Screen.Settings.route)
                    },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).border(
                            width = 4.dp,
                            color = BackgroundColor,
                            shape = CircleShape
                        ),
                        colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor)) {
                        Text("Settings", color = AccentColorGrad1)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }}}
            }
        }
        Scaffold(
            topBar = {
                CustomTopBar(
                    {isTopBarMenuPressed.value=!isTopBarMenuPressed.value},
                    searchQuery=searchQuery.value,
                    onSearchQueryChange = ::onSearchQueryChange,
                    searchState = searchState,
                    vm = VM,
                    navController = NavController
                )
            },

            floatingActionButton = {
                BarFab1(
                NavController= NavController,
                searchState = searchState,
                VM=VM,
            )
            },
            floatingActionButtonPosition = FabPosition.End,
            modifier = Modifier.background(color = BackgroundColor).fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = horizontalGradient(
                        colors = listOf(
                            DarkBackgroundGrad1,
                            DarkBackgroundGrad2
                        )
                    ))
            ) {
                BodyContent(
                    modifier = Modifier
                        .fillMaxSize(),
                    itemsList = pageNotes,
                    navController = NavController,
                    searchState = searchState,
                    viewModel = VM
                )
            }
            if(isDialogShown) {
                Custom2TagsMedalBottom({isDialogShown = false},viewModel=VM,)
            }
        }
    }
    BackHandler {
        if (context is Activity) {
            context.finishAffinity()
        }
    }
}

