/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view.NoteListing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.borzykh.achievera.ui.theme.BackgroundColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import com.borzykh.achievera.data.model.NotesDatabaseElement
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.borzykh.achievera.ui.view.NoteEdit.items
import com.borzykh.achievera.ui.view.Screen
import com.borzykh.achievera.ui.viewModel.INotesListViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BodyContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    itemsList: LazyPagingItems<NotesDatabaseElement>,
    scrollState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    scrollListState: LazyListState = rememberLazyListState(),
    searchState: MutableState<Boolean>,
    viewModel: INotesListViewModel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val offset = screenHeight*0.28f

    Box(modifier = modifier.fillMaxSize()) {
        if(viewModel.MainListType==1){
        LazyVerticalStaggeredGrid(
            state = scrollState,
            columns = StaggeredGridCells.Fixed(viewModel.columnCountForMainList),
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = offset,
                start = 8.dp,
                end = 8.dp,
                bottom = 100.dp
            ),
        ) {
            items(itemsList) { item ->
                Card(
                    onClick = { navController.navigate(Screen.EditNote.createRoute(false, item.id)) },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight(),
                    colors = CardDefaults.cardColors(containerColor = ColorConnect(item.color))
                ) {
                    Column(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                        Text(
                            text = nameChek(item.text, item.name),
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = BackgroundColor,
                            maxLines = 2,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.text,
                            maxLines = 10,
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = BackgroundColor
                        )
                    }
                }
            }
        }
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
        }else if(viewModel.MainListType==2){
            LazyColumn (
                state = scrollListState,
                modifier = Modifier
                    .padding(start = 0.dp, end = 0.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    top = screenHeight*0.28f,
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 100.dp
                ),
            ) {
                items(
                    count = itemsList.itemCount,
                    key = itemsList.itemKey { note -> note.id },
                    contentType = itemsList.itemContentType { "contentType" }
                ) { index ->
                    val item = itemsList[index]
                    if(item != null){
                    Card(
                        onClick = { navController.navigate(Screen.EditNote.createRoute(false, item.id)) },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = ColorConnect(item.color))
                    ) {
                        Column(modifier = Modifier.padding(8.dp).fillMaxWidth().wrapContentHeight()) {
                            Text(
                                text = nameChek(item.text, item.name),
                                modifier = Modifier.padding(vertical = 4.dp),
                                maxLines = 1,
                                color = BackgroundColor,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                maxLines = 2,
                                text = item.text,
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = BackgroundColor
                            )
                        }
                    }}else{
                        Text("Loading...", modifier = Modifier.padding(8.dp))
                    }
                }
            }
            LaunchedEffect(scrollListState) {
                snapshotFlow { scrollListState.firstVisibleItemIndex to scrollListState.firstVisibleItemScrollOffset}
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
        }else{}



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.20f)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
                .pointerInteropFilter {
                    false
                }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.20f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f)
                        )
                    )
                )
                .pointerInteropFilter {
                    false
                }
        )
    }
}