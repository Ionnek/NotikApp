/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view.NoteListing

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.borzykh.achievera.ui.theme.AccentColorGrad1
import com.borzykh.achievera.ui.theme.BackgroundColor
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.borzykh.achievera.ui.theme.CardColorPurplegrad1
import com.borzykh.achievera.ui.view.Screen
import com.borzykh.achievera.ui.viewModel.INotesListViewModel
import com.borzykh.achievera.ui.viewModel.MockNotesListViewModel

@Composable
fun CustomTopBar(
    onMenuClick: () -> Unit,
    navController: NavController,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchState: MutableState<Boolean>,
    vm:INotesListViewModel
) {
    val FilterTags by vm.FilterTags.collectAsState(initial = emptyList())
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val maxHeight=screenHeight*0.05f
    val minHeight=0.dp
    var baseHeight = remember { Animatable(initialValue = minHeight.value) }
    LaunchedEffect(Unit) {
        baseHeight.animateTo(maxHeight.value, animationSpec = tween(500))

    }
    LaunchedEffect(searchState.value) {
        if (!searchState.value) {baseHeight.animateTo(minHeight.value)} else baseHeight.animateTo(maxHeight.value)

    }
    Column() {
        Box(modifier =Modifier.height(screenHeight*0.05f).fillMaxWidth())
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentSize()
                .background(color = Color.Transparent),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (baseHeight.value.dp != minHeight) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(baseHeight.value.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NOTES",
                        style = MaterialTheme.typography.titleLarge,
                        color = AccentColorGrad1,
                    )
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = AccentColorGrad1
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .height(screenHeight*0.01f)
            )
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search", color = Color.Gray) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = BackgroundColor
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AccentColorGrad1,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = AccentColorGrad1,
                    focusedIndicatorColor = Color.Transparent,
                    selectionColors = TextSelectionColors(handleColor = CardColorPurplegrad1, backgroundColor = CardColorPurplegrad1),
                    focusedTextColor = BackgroundColor,
                    cursorColor = CardColorPurplegrad1
                ),
                modifier = Modifier
                    .fillMaxWidth().height(maxHeight*2f)
                    .padding(start = 16.dp, end = 16.dp)
                    .defaultMinSize(minHeight = 0.dp),
                shape = RoundedCornerShape(16.dp)
            )
            Box(
                    modifier = Modifier
                        .height(screenHeight*0.01f)
                    )
            if(FilterTags.isNotEmpty()){
                LazyRow( modifier = Modifier.height(baseHeight.value.dp)){
                    if(baseHeight.value.dp!=minHeight){
                        items(FilterTags,key = { it.id }){ item->
                            Box(modifier = Modifier.width(5.dp))
                            Card(
                                onClick = {
                                    if(item.isActived){
                                        vm.removeTagFromQuery(item.id)
                                    }else{
                                        vm.addTagToQuery(item.id)
                                    }
                                    vm.updateTagsToActiveFilter()
                                },
                                modifier = Modifier
                                    .fillMaxHeight(),
                                colors = CardDefaults.cardColors(containerColor = if(!item.isActived){ColorConnect(item.color)}else{
                                    BackgroundColor}),
                            ) {
                                Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                    Text(
                                        text = item.name,
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        color = if(!item.isActived){BackgroundColor}else{
                                            ColorConnect(item.color)},
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        softWrap = true,
                                    )
                                }
                            }
                        }
                    }
            }}else{
                Row( modifier = Modifier.height(baseHeight.value.dp).wrapContentWidth()){
                Box(modifier = Modifier.width(5.dp))
                Card(
                    onClick = {
                        navController.navigate(Screen.TagEdit.route)
                    },
                    modifier = Modifier
                        .fillMaxHeight(),
                    colors = CardDefaults.cardColors(containerColor = AccentColorGrad1)){
                    Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "add tag",
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = BackgroundColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            softWrap = true,
                        )
                    }
                }}
            }
        }
    }
}
