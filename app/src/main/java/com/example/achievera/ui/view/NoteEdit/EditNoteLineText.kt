package com.example.achievera.ui.view.NoteEdit

import android.icu.text.ListFormatter.Width
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.achievera.R
import com.example.achievera.ui.theme.AccentColorGrad1
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.theme.TestMultyScreensTheme
import com.example.achievera.ui.view.NoteListing.ColorConnect
import com.example.achievera.ui.viewModel.MockNotesListViewModel
import com.example.achievera.ui.viewModel.NoteEditViewModel
import com.google.gson.Gson

@Composable
fun ChekboxLinesEditor(
    onTextChekboxChange: (String) -> Unit,
    baseColor: String,
    viewModel: NoteEditViewModel,
    screenHeight:Dp,
    screenWidth: Dp
) {
    var selectedLineId by rememberSaveable { mutableStateOf<Int?>(viewModel.selectedLine) }
    var newFocusIndex by remember { mutableStateOf<Int?>(null) }
    val focusRequesters = remember(viewModel.linesList) {
        viewModel.linesList.map { FocusRequester() }
    }
    LaunchedEffect(newFocusIndex) {
        newFocusIndex?.let { index ->
            // Если индекс валиден, переводим фокус
            focusRequesters.getOrNull(index)?.requestFocus()
            // Сбрасываем значение, чтобы эффект не срабатывал повторно
            newFocusIndex = null
        }
    }
    if(viewModel.linesList.isNotEmpty()){
        viewModel.toggleChekBoxesActive(true)
    Column(
        modifier = Modifier
            .fillMaxWidth().height(screenHeight*0.8f)
            .padding(start = 18.dp, end = 18.dp)
            .background(color = ColorConnect(baseColor))
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .drawBehind {
                    // Задаём интервал между линиями (например, 30.dp)
                    val lineSpacing = 24.dp.toPx()
                    // Вычисляем, сколько линий нужно отрисовать по высоте
                    val lineCount = (size.height / lineSpacing).toInt()
                    for (i in 0..lineCount) {
                        val y = i * lineSpacing
                        drawLine(
                            color = BackgroundColor,
                            start = Offset(size.width*0.05f, y),
                            end = Offset(size.width*0.95f, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
        ) {
            viewModel.linesList.forEachIndexed { index, line ->
                LineRow(
                    lineItem = line,
                    onLineClick = { clickedId ->
                        selectedLineId = clickedId
                        viewModel.updateSelectedLine(clickedId)
                        Log.d("Линия", "выбрана линия: $clickedId")
                    },
                    onCheckboxToggle = { isChecked ->
                        viewModel.assignReadyLinesList(viewModel.linesList.map {
                            if (it.id == line.id) it.copy(isChecked = isChecked) else it
                        })
                        onTextChekboxChange(serialize(viewModel.linesList))
                    },
                    onTextChanged = { newText ->
                        viewModel.assignReadyLinesList( viewModel.linesList.map {
                            if (it.id == line.id) it.copy(text = TextFieldValue(newText.text,newText.selection,newText.composition)) else it
                        })
                        onTextChekboxChange(serialize(viewModel.linesList))
                    },
                    baseColor = baseColor,
                    isLast = index == viewModel.linesList.lastIndex,
                    focusRequester = focusRequesters[index],
                    onNext = {
                        val newLines = viewModel.linesList.toMutableList()
                        val newLine = LineWithCheckBoxAndCursorDetect(
                            id = newLines.size + 1,
                            text = TextFieldValue(line.text.getTextAfterSelection(200)),
                            isChecked = false,
                            hasCheckbox = line.hasCheckbox
                        )

                        // Вставляем новую строку сразу после текущей
                        val insertIndex = if (index < newLines.size) index + 1 else newLines.size
                        newLines[index] = line.copy(
                            text = TextFieldValue(text=line.text.getTextBeforeSelection(200).toString(),
                                selection = TextRange(line.text.text.length))
                        )
                        newLines.add(insertIndex, newLine)
                        viewModel.assignReadyLinesList(newLines)
                        onTextChekboxChange(serialize(newLines))

                        // Запоминаем индекс для фокуса на новой строке
                        newFocusIndex = insertIndex
                    },
                    onDelete = {
                        viewModel.removeLine(line.id)
                        onTextChekboxChange(serialize(viewModel.linesList))
                    },
                    onDeleteChexBox = {
                        viewModel.changeLineChekboxStatus()
                        onTextChekboxChange(serialize(viewModel.linesList))
                    },
                    onBackspaceAtStart = {
                        if (index > 0) {
                            // Создаем изменяемую копию списка строк
                            val newLines = viewModel.linesList.toMutableList()
                            // Берем предыдущую строку
                            val previousLine = newLines[index - 1]
                            // Объединяем текст предыдущей строки с текстом текущей строки
                            val mergedTextValue = previousLine.text.text + line.text.text

                            // Создаем новый TextFieldValue, можно установить курсор в конец объединённого текста
                            val mergedText = TextFieldValue(
                                text = mergedTextValue,
                                selection = TextRange(previousLine.text.text.length)
                            )

                            // Обновляем предыдущую строку с объединённым текстом
                            newLines[index - 1] = previousLine.copy(text = mergedText)
                            // Удаляем текущую строку
                            newLines.removeAt(index)

                            // Обновляем список строк в ViewModel
                            viewModel.assignReadyLinesList(newLines)
                            // Передаем новый сериализованный текст (если требуется)
                            onTextChekboxChange(serialize(newLines))
                            // Переводим фокус на предыдущую строку
                            focusRequesters[index - 1].requestFocus()
                        }
                    },
                    screenHeight = screenHeight,
                    screenWidth = screenWidth
                )
            }
        }
    }}else{viewModel.toggleChekBoxesActive(false)}
}

@Composable
fun LineRow(
    lineItem: LineWithCheckBoxAndCursorDetect,

    onLineClick: (Int) -> Unit,
    onCheckboxToggle: (Boolean) -> Unit,
    onTextChanged: (TextFieldValue) -> Unit,
    baseColor: String,
    onBackspaceAtStart: () -> Unit,
    isLast: Boolean,
    focusRequester: FocusRequester,
    onNext: () -> Unit,
    onDelete: () -> Unit,
    onDeleteChexBox:() -> Unit,
    screenHeight: Dp,
    screenWidth: Dp
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        if (lineItem.hasCheckbox) {
            Checkbox(
                checked = lineItem.isChecked,
                onCheckedChange = { onCheckboxToggle(it) },
                modifier = Modifier
                    .padding(start = 6.dp, end = 6.dp)
                    .size(screenWidth*0.05f),
                colors = CheckboxColors(
                    checkedCheckmarkColor = BackgroundColor,
                    uncheckedCheckmarkColor = Color.Transparent,
                    checkedBoxColor = Color.Transparent,
                    uncheckedBoxColor = Color.Transparent,
                    disabledCheckedBoxColor = AccentColorGrad1,
                    disabledUncheckedBoxColor = AccentColorGrad1,
                    disabledIndeterminateBoxColor = AccentColorGrad1,
                    checkedBorderColor = BackgroundColor,
                    uncheckedBorderColor = BackgroundColor,
                    disabledBorderColor = AccentColorGrad1,
                    disabledUncheckedBorderColor = AccentColorGrad1,
                    disabledIndeterminateBorderColor = AccentColorGrad1,
                ),

            )


        }else{Box(modifier = Modifier
            .padding(start = 6.dp, end = 6.dp)
            .size(screenWidth*0.02f)
            .background(color = Color.Transparent)
            ,)
        }

        val imeAction = if (isLast) ImeAction.Done else ImeAction.Next
        val customTextSelectionColors = TextSelectionColors(
            handleColor = BackgroundColor, // Цвет ручек выделения
            backgroundColor = BackgroundColor.copy(alpha = 0.4f) // Цвет фона выделения
        )
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            value = lineItem.text,
            onValueChange = onTextChanged,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        onLineClick(lineItem.id)
                    }
                }// Перехватываем нажатие Enter до того, как система добавит новую строку:
                .onPreviewKeyEvent { keyEvent ->
                    // Проверяем, что это нажатие (KeyDown) именно на Enter (или аналог)
                    if (
                        keyEvent.type == KeyEventType.KeyDown &&
                        (keyEvent.key == Key.Enter || keyEvent.key == Key.NumPadEnter)
                    ) {
                        onNext()
                        true // Ставим true, чтобы событие "съесть" и не вставлять \n
                    } else {
                        false
                    }

                    if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                        if (lineItem.text.selection.start == 0 && lineItem.text.selection.end == 0) {
                            if (lineItem.hasCheckbox) {
                                onDeleteChexBox()
                            } else {
                                onBackspaceAtStart()
                            }
                            true  // Сообщаем, что событие обработано
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
                .background(color = Color.Transparent),

            textStyle = if (lineItem.isChecked && lineItem.hasCheckbox) {
                LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)

            } else {
                LocalTextStyle.current
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext()
                },
                onDone = {
                    onNext()
                },
            ),

        )}
        Box(
            modifier = Modifier
                .padding(start = 6.dp, end = 6.dp)
                .size(screenWidth*0.05f)
                .background(color = Color.Transparent)
        )
    }
}

@Preview
@Composable
fun prev1() {
    val navController = rememberNavController()
    val VM = MockNotesListViewModel()
    TestMultyScreensTheme {
        Surface {
            ScreenNoteEdit(navController, false, 0)
        }
    }
}
