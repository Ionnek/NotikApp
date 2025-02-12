package com.example.achievera.ui.view.NoteEdit

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import com.example.achievera.ui.viewModel.INotesListViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

fun getListWIthEmptyLines():List<LineWithCheckBox>{
    return listOf(
        LineWithCheckBox(
            id = 0,
            text = "",
            isChecked = false,
            hasCheckbox = true
        ),
    )
}

data class LineWithCheckBox(
    val id: Int,
    var text: String,
    var isChecked: Boolean = false,
    var hasCheckbox: Boolean = false,
)

data class LineWithCheckBoxAndCursorDetect(
    val id: Int,
    var text: TextFieldValue,
    var isChecked: Boolean = false,
    var hasCheckbox: Boolean = false,
)

fun serialize(unWrappedLines: List<LineWithCheckBoxAndCursorDetect>): String {
    val gson = Gson()

    val wrappedLines = unWrappedLines.map { line ->
        LineWithCheckBox(
            id = line.id,
            text = line.text.text,
            isChecked = line.isChecked,
            hasCheckbox = line.hasCheckbox
        )
    }
    return gson.toJson(wrappedLines)
}
fun deserialize(text: String): List<LineWithCheckBoxAndCursorDetect> {
    val gson = Gson()
    val listType = object : TypeToken<List<LineWithCheckBox>>() {}.type

    val plainList: List<LineWithCheckBox> = try {
        gson.fromJson(text, listType) ?: emptyList()
    } catch (e: JsonSyntaxException) {
        return emptyList<LineWithCheckBoxAndCursorDetect>()
    }

    return plainList.map { line ->
        LineWithCheckBoxAndCursorDetect(
            id = line.id,
            text = TextFieldValue(line.text),
            isChecked = line.isChecked,
            hasCheckbox = line.hasCheckbox
        )
    }
}

fun addlinetochekbox(){

}
@Composable
fun addPhotoToNote(
    viewModel:INotesListViewModel,
    id:Long
){
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
    val request = PickVisualMediaRequest(PickVisualMedia.ImageOnly)
    photoPickerLauncher.launch(request)
}