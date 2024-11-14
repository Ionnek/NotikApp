package com.example.achievera.ui.view

import androidx.compose.ui.graphics.Color
import com.example.achievera.ui.theme.AccentColorGrad1
import com.example.achievera.ui.theme.CardColorBlueGrad1
import com.example.achievera.ui.theme.CardColorDarkPurpleGrad1
import com.example.achievera.ui.theme.CardColorGreenGrad1
import com.example.achievera.ui.theme.CardColorPurplegrad1
import com.example.achievera.ui.theme.CardColorRedGrad1
import com.example.achievera.ui.theme.CardColorYellowGrad1

fun MakeShort(text: String): String = if (text.length > 20) {
    text.substring(0, 20) + "..."
} else {
    text
}
fun nameChek(text: String,name:String): String {
    if (name==""){
        return MakeShort(text)
    }else
        return MakeShort(name)

}

fun ColorConnect(NoteColor:String): Color {
    var Color: Color
    when (NoteColor) {
        "Purple" -> Color= CardColorPurplegrad1
        "Red" -> Color= CardColorRedGrad1
        "Green" -> Color= CardColorGreenGrad1
        "Blue" -> Color= CardColorBlueGrad1
        "DarkPurple" -> Color= CardColorDarkPurpleGrad1
        "Yellow"-> Color= CardColorYellowGrad1
        else -> {
            Color= AccentColorGrad1
        }
    }
    return Color
}
