/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.borzykh.achievera.ui.theme.BackgroundColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.ui.unit.sp
import com.borzykh.achievera.ui.theme.AccentColorGrad1
import com.borzykh.achievera.ui.viewModel.INotesListViewModel

@Composable
fun SettingsScreen(
    viewModel: INotesListViewModel,
    navController: NavController
){
    val columnCount = mapOf(
        2 to "2",
        3 to "3",
        4 to "4"
    )
    val listType =mapOf(1 to "Grid",2 to "List")

    var expandedTypeSelector by remember { mutableStateOf(false) }
    var expandedColumnSelector by remember { mutableStateOf(false) }
    var selectedColumnText by remember {
        mutableStateOf(columnCount[viewModel.columnCountForMainList] ?: "Unknown")
    }
    var selectedTypeText by remember {
        mutableStateOf(listType[viewModel.MainListType] ?: "Unknown")
    }
    Scaffold (){innerPadding ->
        Column(modifier = Modifier.fillMaxSize().background(BackgroundColor)) {
            Spacer(modifier=Modifier.height(50.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Button(
                    onClick = {
                        navController.navigate(Screen.Home.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundColor,
                        contentColor = BackgroundColor
                    ),

                    modifier = Modifier.padding(horizontal = 16.dp).border(
                        width = 4.dp,
                        color = AccentColorGrad1,
                        shape = CircleShape
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Text("<-", color = AccentColorGrad1)
                }
                Text(text= "Settings", color = AccentColorGrad1, fontSize = 25.sp)
            }
            Column {
        // Box нужен для якоря меню
        Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopStart).padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Text("Main list type", color = AccentColorGrad1)
            Button (
                { expandedTypeSelector = !expandedTypeSelector },
                modifier = Modifier.border(
                    width = 4.dp,
                    color = AccentColorGrad1,
                    shape = CircleShape
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = AccentColorGrad1
                )
            ){ Text(selectedTypeText, color = AccentColorGrad1)
                DropdownMenu(
                    expanded = expandedTypeSelector,
                    onDismissRequest = { expandedTypeSelector = false },
                    containerColor = AccentColorGrad1
                ) {
                    listType.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.value) },
                            onClick = {
                                selectedTypeText = item.value
                                expandedTypeSelector = false
                                val type = when(item.value) {
                                    "Grid" -> 1
                                    "List" -> 2
                                    else -> 1
                                }
                                viewModel.setListType(type)
                            }
                        )
                    }
                }}
            }}
            Spacer(modifier = Modifier.height(10.dp))
                if(selectedTypeText=="Grid"){
            Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopStart).padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement
                .SpaceBetween){
                Text("List columns count", color = AccentColorGrad1)
                Button (
                    { expandedColumnSelector = !expandedColumnSelector },
                    modifier = Modifier.border(
                        width = 4.dp,
                        color = AccentColorGrad1,
                        shape = CircleShape
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = AccentColorGrad1
                    )
                ){ Text(selectedColumnText, color = AccentColorGrad1)
                    DropdownMenu(
                        expanded = expandedColumnSelector,
                        onDismissRequest = { expandedColumnSelector = false },
                        containerColor = AccentColorGrad1
                    ) {
                        columnCount.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.value) },
                                onClick = {
                                    selectedColumnText = item.value
                                    expandedColumnSelector = false
                                    val columns = when(item.value) {
                                        "2" -> 2
                                        "3" -> 3
                                        "4" -> 4
                                        else -> 2
                                    }
                                    viewModel.setColumnCount(columns)
                                }
                            )
                        }
                    }}
                }}}}
        }
    }
}