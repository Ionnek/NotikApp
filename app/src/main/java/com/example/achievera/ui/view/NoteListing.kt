package com.example.achievera.ui.view

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.data.model.Tag
import androidx.compose.foundation.lazy.items
import com.example.achievera.ui.theme.AccentColorGrad1
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.theme.TestMultyScreensTheme
import com.example.achievera.ui.viewModel.INotesListViewModel
import com.example.achievera.ui.viewModel.MockNotesListViewModel

@Composable
fun ScreenNotesListing(NavController: NavController,VM: INotesListViewModel) {
    val pageNotes:LazyPagingItems<NotesDatabaseElement> =VM.notes.collectAsLazyPagingItems()
    val searchQuery=rememberSaveable { mutableStateOf("") }
    val searchState=rememberSaveable { mutableStateOf(true) }
    val scrollState= rememberLazyStaggeredGridState()
    val context = LocalContext.current
    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
        VM.setQuery(query)
    }
    val topBarHeight by animateDpAsState(
        targetValue = if (searchState.value) 120.dp else 80.dp,
        animationSpec = tween(durationMillis = 300) // Продолжительность анимации
    )
    val BottomBarHeight by animateDpAsState(
        targetValue = if (searchState.value) 70.dp else 0.dp,
        animationSpec = tween(durationMillis = 300) // Продолжительность анимации
    )
    val BottomFabOffsetHeight by animateDpAsState(
        targetValue = if (searchState.value) 0.dp else -25.dp,
        animationSpec = tween(durationMillis = 300) // Продолжительность анимации
    )
    Box(modifier=Modifier.fillMaxSize().background(color = BackgroundColor)) {
        Scaffold(
            topBar = {CustomTopBar({ println("Привет!") }, searchQuery.value,onSearchQueryChange = ::onSearchQueryChange,searchState.value,height = topBarHeight, tagList = GetTestTagList(), navController = NavController)},
            floatingActionButton = { BarFab(NavController, VM,OffsetHeight=BottomFabOffsetHeight) },
            floatingActionButtonPosition = FabPosition.End,
            // В Material3 нет параметра isFloatingActionButtonDocked, поэтому мы будем использовать смещение
            bottomBar = { ResponsiveBottomBar(VM,searchState.value,height=BottomBarHeight) },
            modifier = Modifier.background(color = BackgroundColor).fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BackgroundColor)
            ) {
                // BodyContent игнорирует innerPadding и занимает весь экран
                BodyContent(
                    modifier = Modifier
                        .fillMaxSize(), // Не учитывает innerPadding
                    itemsList = pageNotes,
                    navController = NavController,
                    scrollState = scrollState,
                    searchState = searchState
                )
            }
        }
    }
    BackHandler {
        if (context is Activity) {
            // Завершаем текущую активность и все связанные с ней
            context.finishAffinity()
        }
    }
}

@Composable
fun CustomTopBar(
    onMenuClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isVisible: Boolean,
    height: Dp,
    tagList: MutableList<Tag>,
    navController: NavController
) {
    // Получаем конфигурацию экрана
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Определяем желаемый процент высоты TopBar
    val topBarHeightPercentage = 0.17f // 15% от высоты экрана
    val topBarHeight = height


    Column() {
        Box(
            modifier = Modifier.fillMaxWidth().height(topBarHeight)
                .background(color = Color.Transparent)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
        ) {
            // Кастомная верхняя панель
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(topBarHeight)
                    .padding().background(color = Color.Transparent),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                // Первая строка: заголовок и кнопка меню
                var HeadingHeight: Dp = topBarHeight - 80.dp
                if (topBarHeight != 80.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(HeadingHeight)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NOTES",
                            style = MaterialTheme.typography.titleLarge,
                            color = AccentColorGrad1
                        )
                        IconButton(onClick = onMenuClick) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Меню",
                                tint = AccentColorGrad1
                            )
                        }
                    }
                }
                // Вторая строка: строка поиска
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Поиск", color = Color.Gray) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Поиск",
                            tint = AccentColorGrad1
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = AccentColorGrad1,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = AccentColorGrad1,
                        focusedIndicatorColor = Color.Transparent,
                        focusedTextColor = BackgroundColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
        LazyRow( modifier = Modifier.height(topBarHeight-75.dp)) {
            if(topBarHeight-75.dp!=0.dp){
            items(tagList){ item->
            Card(
                onClick = {},
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = ColorConnect(item.color)),
            ) {
                Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                    Text(
                        text = item.name,
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = BackgroundColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            }
            }}
        }
    }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BodyContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    itemsList: LazyPagingItems<NotesDatabaseElement>,
    scrollState:LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    searchState: MutableState<Boolean>
) {
    // Получаем density вне LaunchedEffect
    val density = LocalDensity.current

    Box(modifier = modifier.fillMaxSize()) {
        // Основной контент — сетка заметок
        LazyVerticalStaggeredGrid(
            state = scrollState, // Используем переданный scrollState
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = 175.dp, // Верхний отступ
                start = 8.dp,
                end = 8.dp,
                bottom = 100.dp // Нижний отступ, если нужен
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
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = nameChek(item.text, item.name),
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = BackgroundColor,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.text,
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = BackgroundColor
                        )
                    }
                }
            }
        }

        // Наблюдаем за состоянием прокрутки и обновляем searchState
        // Отслеживаем прокрутку и управляем состоянием searchState
        LaunchedEffect(scrollState) {
            snapshotFlow { scrollState.firstVisibleItemIndex to scrollState.firstVisibleItemScrollOffset}
                .collect { (index, offset) ->
                    if (index>0 || offset > 100) {
                        // Если прокручено хоть немного вниз, скрываем TopBar
                        if (searchState.value) {
                            searchState.value = false
                        }
                    } else {
                        // Если прокрутка вернулась в начало, показываем TopBar
                        if (!searchState.value) {
                            searchState.value = true
                        }
                    }
                }
        }


        // Верхний градиент
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.20f) // 10% высоты
                .align(Alignment.TopCenter) // Позиционирование в верхней части
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f), // Начало градиента (0%)
                            Color.Transparent // Конец градиента (10%)
                        )
                    )
                )
                .pointerInteropFilter {
                    // Возвращаем false, чтобы события проходили дальше
                    false
                }
        )

        // Нижний градиент
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.20f) // 10% высоты
                .align(Alignment.BottomCenter) // Позиционирование в нижней части
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, // Начало градиента (90%)
                            Color.Black.copy(alpha = 0.3f) // Конец градиента (100%)
                        )
                    )
                )
                .pointerInteropFilter {
                    // Возвращаем false, чтобы события проходили дальше
                    false
                }
        )
    }
}

@Composable
fun ResponsiveBottomBar(VM: INotesListViewModel,
                        isVisible: Boolean,
                        height: Dp) {
    // Получаем конфигурацию экрана
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Вычисляем размеры как процент от ширины и высоты экрана
    val buttonWidth: Dp = screenWidth * 0.10f // 12% от ширины экрана
    val buttonHeight: Dp = height * 0.5f // 6% от высоты экрана
    val paddingHorizontal: Dp = screenWidth * 0.06f // 3% от ширины экрана
    val bottomBarHeight: Dp = height // Увеличиваем высоту для места под FAB
    Box(modifier=Modifier.fillMaxWidth().height(bottomBarHeight).background(color = BackgroundColor).clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))){
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(bottomBarHeight).background(color = BackgroundColor), containerColor = BackgroundColor,
        content = {
            // Основные кнопки в BottomAppBar
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = paddingHorizontal),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButtonWithText(buttonWidth, buttonHeight)
                CustomButtonWithText(buttonWidth, buttonHeight)
                CustomButtonWithText(buttonWidth, buttonHeight)
                CustomButtonWithText(buttonWidth, buttonHeight)
            }
        }
    )}
}

@Composable
fun BarFab(NavController: NavController, VM: INotesListViewModel,OffsetHeight: Dp){
    // Получаем конфигурацию экрана
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Вычисляем размеры как процент от ширины и высоты экрана
    val fabSize: Dp = screenWidth * 0.20f // 12% от ширины экрана

    // Определяем набор символов для случайной строки
    val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    // Функция для генерации случайной строки заданной длины
    fun getRandomString(length: Int): String {
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    FloatingActionButton(
        onClick = {NavController.navigate(Screen.EditNote.createRoute(true, 0))},
        modifier = Modifier
            .size(fabSize)
            .offset(y = (OffsetHeight),x = -10.dp) // Смещаем FAB вверх на 11% от ширины экрана
            .border(
                width = 4.dp,
                color = AccentColorGrad1,
                shape = CircleShape // Устанавливаем круглую форму границы
            ),
        containerColor = BackgroundColor,
        shape = CircleShape, // Устанавливаем круглую форму FAB
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Добавить",
            tint = AccentColorGrad1,
        )
    }
}

@Preview
@Composable
fun myappprev(){
    val navController = rememberNavController()
    TestMultyScreensTheme {
        Surface {
            ScreenNotesListingPreivew(navController)
        }
    }
}
@Composable
fun ScreenNotesListingPreivew(NavController: NavController){
    val scrollState = rememberLazyStaggeredGridState()
    val sampleItems = listOf(
        NotesDatabaseElement(id = 1, name = "Note 1", text = "This is the first note.", date = 2222, dateEdited = "2222", reminder = 32323, color = "v", isHandwritten = false),
        NotesDatabaseElement(id = 1, name = "Note 1", text = "This is the first note.", date = 2222, dateEdited = "2222", reminder = 32323, color = "v", isHandwritten = false)
    )
    val VMM = MockNotesListViewModel()

    Scaffold(
        topBar = { CustomTopBar({ println("Привет!")}, "", {}, height = 130.dp, isVisible = true, tagList =  GetTestTagList(),navController = NavController) },
        floatingActionButton = { BarFab(NavController,VMM,OffsetHeight = 55.dp) },
        floatingActionButtonPosition = FabPosition.Center,
        // В Material3 нет параметра isFloatingActionButtonDocked, поэтому мы будем использовать смещение
        bottomBar = { ResponsiveBottomBar(VMM,true,height = 100.dp) },
        modifier = Modifier
            .fillMaxSize().background(color = BackgroundColor),
    ) { innerPadding ->
        // Контент между верхней и нижней панелью
        BodyContent(
            modifier = Modifier
                .padding(innerPadding) // padding из Scaffold для учета topBar и bottomBar
                .fillMaxSize().background(color = BackgroundColor),
            itemsList = VMM.notes.collectAsLazyPagingItems(),
            navController=NavController,
            scrollState = scrollState,
            searchState = rememberSaveable { mutableStateOf(true) }
        )
    }
}
@Composable
fun CustomButtonWithText(buttonWidth: Dp, buttonHeight: Dp){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Вычисляем размеры как процент от ширины и высоты экрана
    val fabSize: Dp = screenWidth * 0.10f // 12% от ширины экрана

    Button(
            onClick = {},
        modifier = Modifier
            .height(fabSize).width(fabSize) // Смещаем FAB вверх на 11% от ширины экрана
            .border(
                width = 4.dp,
                color = AccentColorGrad1,
                shape = CircleShape // Устанавливаем круглую форму границы
            ),
        colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor),
        shape = CircleShape, // Устанавливаем круглую форму FAB
    ) {
        Icon(
            modifier = Modifier
                .height(fabSize).width(fabSize),
            imageVector = Icons.Default.Add,
            contentDescription = "Добавить",
            tint = AccentColorGrad1,
        )
    }
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