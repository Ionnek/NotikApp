package com.example.achievera.ui.view.NoteEdit

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.unit.dp
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.view.NoteListing.ColorConnect
import com.example.achievera.ui.viewModel.INotesListViewModel

@Composable
fun InlineCheckboxField_DualLayer(
    innerPadding: PaddingValues,
    BaseColor: String,
    text: String,
    id: Long,
    onValueChange: (String) -> Unit,
    onTextChekboxChange: (String) -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize().background(color = BackgroundColor)) {
        Column(
            modifier = Modifier.defaultMinSize(minHeight = 200.dp)
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .background(color = ColorConnect(BaseColor)),
        ) {
            TextField(
                value = text,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth().height(600.dp).defaultMinSize(minHeight = 600.dp)
                    .padding(0.dp),
                placeholder = { Text("Введите текст заметки") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = ColorConnect(
                        BaseColor
                    ),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = ColorConnect(BaseColor),
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = BackgroundColor,
                    unfocusedTextColor = BackgroundColor,
                    unfocusedPlaceholderColor = BackgroundColor,
                    focusedPlaceholderColor = BackgroundColor,
                    cursorColor = BackgroundColor
                ),
            )
        }
    }
}