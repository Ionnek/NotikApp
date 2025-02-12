package com.example.achievera.ui.view.NoteListing

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.achievera.R
import com.example.achievera.data.model.Tag
import com.example.achievera.ui.theme.AccentColorGrad1
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.theme.CardColorBlueGrad1
import com.example.achievera.ui.theme.CardColorDarkPurpleGrad1
import com.example.achievera.ui.theme.CardColorGreenGrad1
import com.example.achievera.ui.theme.CardColorPurplegrad1
import com.example.achievera.ui.theme.CardColorRedGrad1
import com.example.achievera.ui.theme.CardColorYellowGrad1
import com.example.achievera.ui.viewModel.INotesListViewModel





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
@Composable
fun CustomButtonWithText(
    onMenuStateChange: (menuState:Int) -> Unit,
    number:Int,
    state:Int,){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var number=number
    var state=state
    val fabSize: Dp = screenWidth * 0.10f

        Image(
            painter = painterResource(
                if(state==number){R.drawable.group_391}
                else{R.drawable.group_367}
            ),
            contentDescription = "Описание иконки",
            modifier = Modifier
                .size(fabSize)
                .clickable{onMenuStateChange(number)},
            contentScale = ContentScale.Fit// Иконка заполняет всю кнопку // Позиционируем в верхний правый угол
        )
}

fun GetTestTagList(): MutableList<Tag> {
    val tagList = mutableListOf<Tag>()
    tagList.add(Tag(1, "Test", "Purple",false))
    tagList.add(Tag(2, "Test2", "Red",false))
    tagList.add(Tag(3, "Test3", "Green",false))
    tagList.add(Tag(4, "Test4", "Blue",false))
    tagList.add(Tag(5, "Test5", "DarkPurple",false))
    tagList.add(Tag(6, "Test6", "Yellow",false))
    tagList.add(Tag(7, "Test7", "Purple",false))
    tagList.add(Tag(8, "Test8", "Red",false))
    return tagList
}
