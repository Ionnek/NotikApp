/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view.NoteEdit

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.ui.theme.AccentColorGrad1
import com.borzykh.achievera.ui.theme.BackgroundColor
import com.borzykh.achievera.ui.view.NoteListing.ColorConnect
import com.borzykh.achievera.ui.viewModel.NoteEditViewModel

@Composable
fun EditNoteBotomBar(UsefulColorList: List<String>,
                     isNew: Boolean, note: NotesDatabaseElement?,
                     vm: NoteEditViewModel,
                     onColorChange: (String) -> Unit,
                     BaseColor:String,
                     onClick:()->Unit,
                     onChekClick:()->Unit,
                     Ptpkr: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
) {
    var isColorSelectionRegime by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AccentColorGrad1)
                .navigationBarsPadding()
                .imePadding()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isColorSelectionRegime) {
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Button(
                        onClick = { isColorSelectionRegime = true },
                        modifier = Modifier.border(
                            width = 4.dp,
                            color = BackgroundColor,
                            shape = CircleShape
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
                            }
                        ),
                    ) { Text("Color", color = BackgroundColor) }
                    Button(
                        onClick, modifier = Modifier.border(
                            width = 4.dp,
                            color = BackgroundColor,
                            shape = CircleShape
                        ),
                        colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor)
                    ) {
                        Text("Tags", color = AccentColorGrad1)
                    }
                    if (vm.images.collectAsState(initial = emptyList()).value.isEmpty()) {
                        Button(
                            {
                                val request =
                                    PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                                Ptpkr.launch(request)
                            },
                            modifier = Modifier.border(
                                width = 4.dp,
                                color = BackgroundColor,
                                shape = CircleShape
                            ),
                            colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor)
                        ) {
                            Text("Image", color = AccentColorGrad1)
                        }
                    }
                    if (true) {
                        Button(
                            onClick = {
                                onChekClick()
                            },
                            modifier = Modifier.border(
                                width = 4.dp,
                                color = BackgroundColor,
                                shape = CircleShape
                            ),
                            colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor)
                        ) {
                            Text("Chekbx", color = AccentColorGrad1)
                        }
                    }
                }
            } else {
                LazyRow() {
                    items(UsefulColorList) { item ->
                        Button(
                            onClick = {
                                onColorChange(item)
                                isColorSelectionRegime = false
                                if (!isNew) {
                                    vm.getNoteById(note!!.id)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ColorConnect(item),
                                contentColor = ColorConnect(item)
                            ),
                        ) { }
                    }
                }
            }
        }

}