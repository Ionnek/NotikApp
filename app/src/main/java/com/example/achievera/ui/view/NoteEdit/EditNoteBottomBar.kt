package com.example.achievera.ui.view.NoteEdit

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.ui.theme.AccentColorGrad1
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.view.NoteListing.ColorConnect
import com.example.achievera.ui.viewModel.INotesListViewModel
import com.example.achievera.ui.viewModel.NoteEditViewModel

@Composable
fun EditNoteBotomBar(UsefulColorList: List<String>,
                     isNew: Boolean, note: NotesDatabaseElement?,
                     vm: NoteEditViewModel,
                     onColorChange: (String) -> Unit,
                     BaseColor:String,
                     onClick:()->Unit,
                     Ptpkr: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
                     onTextChekboxChange:(String)->Unit
) {
    var isColorSelectionRegime by remember { mutableStateOf(false) }
    val isImageActive = vm.isImageActive
    val isChekboxesActive = vm.isChekboxesActive
    val offsetBottomBar by animateDpAsState(
        targetValue = WindowInsets.ime.asPaddingValues().calculateBottomPadding(),
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = BackgroundColor)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)).offset(y = -offsetBottomBar),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = AccentColorGrad1),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically

        ) {
            if(!isColorSelectionRegime){
                Button(onClick = {isColorSelectionRegime=true}, modifier = Modifier.border(
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
                Button(onClick, modifier = Modifier.border(
                    width = 4.dp,
                    color = BackgroundColor,
                    shape = CircleShape
                ),
                    colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor)){
                    Text("Tags",color = AccentColorGrad1)
                }
                if(vm.images.collectAsState(initial = emptyList()).value.isEmpty()){
                Button({val request =
                    PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                    Ptpkr.launch(request)
                       },
                    modifier = Modifier.border(
                    width = 4.dp,
                    color = BackgroundColor,
                    shape = CircleShape
                ),
                    colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor)){
                    Text("Image",color = AccentColorGrad1)
                }}
                if(true){
                    Button({
                        vm.changeLineChekboxStatus()

                        if(vm.linesList.isEmpty()){
                            vm.addEmptyLine()
                        }
                        onTextChekboxChange(serialize(vm.linesList))
                    },
                        modifier = Modifier.border(
                            width = 4.dp,
                            color = BackgroundColor,
                            shape = CircleShape
                        ),
                        colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor)
                    ){
                        Text("Chekbx",color = AccentColorGrad1)
                    }}
            }
            else{
                LazyRow (){
                    items(UsefulColorList) { item ->
                        Button(onClick = {
                        onColorChange(item)
                        isColorSelectionRegime=false
                        if(!isNew){
                            vm.getNoteById(note!!.id)
                        }
                    },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorConnect(item),
                            contentColor = ColorConnect(item)
                        ),) { }
                    }
                }
            }
        }
    }
}