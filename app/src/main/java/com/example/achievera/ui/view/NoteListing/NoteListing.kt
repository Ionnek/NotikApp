package com.example.achievera.ui.view.NoteListing

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.achievera.data.model.NotesDatabaseElement
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.window.Dialog
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.theme.DarkBackgroundGrad1
import com.example.achievera.ui.theme.DarkBackgroundGrad2
import com.example.achievera.ui.theme.TestMultyScreensTheme
import com.example.achievera.ui.view.NoteEdit.Custom2TagsMedalBottom
import com.example.achievera.ui.view.NoteEdit.CustomTagsMedalBottom
import com.example.achievera.ui.view.SettingsPopUp
import com.example.achievera.ui.view.TagEditContent
import com.example.achievera.ui.viewModel.INotesListViewModel
import com.example.achievera.ui.viewModel.MockNotesListViewModel

@Composable
fun ScreenNotesListing1(NavController: NavController,VM: INotesListViewModel) {
    val MenuState=rememberSaveable { mutableStateOf(VM.MenoState) }
    val pageNotes:LazyPagingItems<NotesDatabaseElement> =VM.notes.collectAsLazyPagingItems()
    val tagsList by VM.FilterTags.collectAsState(initial = emptyList())
    val searchQuery=rememberSaveable { mutableStateOf("") }
    val searchState=rememberSaveable { mutableStateOf(true) }
    val isTopBarMenuPressed= rememberSaveable{ mutableStateOf(false)}
    var isDialogShown by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    fun MenuStateChange(menuState:Int){
        VM.MenoState=menuState
        MenuState.value=menuState
    }
    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
        VM.setQuery(query)
    }
        VM.updateTagsToActiveFilter()

    Box(modifier=Modifier.fillMaxSize().background(color = BackgroundColor)) {
        if(isTopBarMenuPressed.value){
            Dialog({isTopBarMenuPressed.value=!isTopBarMenuPressed.value}){
                SettingsPopUp(
                )
            }
        }
        Scaffold(

            topBar = {if(MenuState.value==1){
                CustomTopBar1(
                {isTopBarMenuPressed.value=!isTopBarMenuPressed.value},
                searchQuery=searchQuery.value,
                onSearchQueryChange = ::onSearchQueryChange,
                tagList = tagsList,
                VM = VM,
                searchState = searchState,
                )
            }},

            floatingActionButton = {if(MenuState.value==1){
                BarFab1(
                NavController= NavController,
                searchState = searchState,
                VM=VM,
            )}
            },
            floatingActionButtonPosition = FabPosition.End,

            bottomBar = { ResponsiveBottomBar1(
                searchState = searchState,
                onMenuStateChange = { MenuStateChange(it)},
                MenuState = MenuState.value
            ) },
            modifier = Modifier.background(color = BackgroundColor).fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = horizontalGradient(
                        colors = listOf(
                            DarkBackgroundGrad1,
                            DarkBackgroundGrad2
                        )
                    ))
            ) {
                if(MenuState.value==1){
                BodyContent1(
                    modifier = Modifier
                        .fillMaxSize(),
                    itemsList = pageNotes,
                    navController = NavController,
                    searchState = searchState,
                    tagList = tagsList

                )}else if(MenuState.value==2){
                    TagEditContent(
                        viewModel = VM,navController = NavController,
                        searchState = searchState,
                        {isDialogShown = true})
                }else{}
            }
            if(isDialogShown) {
                Custom2TagsMedalBottom({isDialogShown = false},viewModel=VM,)
            }
        }
    }
    BackHandler {
        if (context is Activity) {
            context.finishAffinity()
        }
    }
}

@Preview
@Composable
fun myappprev1(){
    val navController = rememberNavController()
    TestMultyScreensTheme {
        Surface {
            ScreenNotesListingPreivew1(navController)
        }
    }
}
@Composable
fun ScreenNotesListingPreivew1(NavController: NavController){
    val scrollState = rememberLazyStaggeredGridState()
    val VMM = MockNotesListViewModel()

    Scaffold(
        topBar = {
            CustomTopBar1({ println("Привет!")},
                "",
                {},
                tagList =  GetTestTagList(),
                VM = VMM,
                searchState = rememberSaveable { mutableStateOf(true) }
            ) },
        floatingActionButton = { BarFab1(NavController,VMM,searchState = rememberSaveable { mutableStateOf(true)}) },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = { ResponsiveBottomBar1(searchState = rememberSaveable { mutableStateOf(true) },{},1)},
        modifier = Modifier
            .fillMaxSize().background(color = BackgroundColor),
    ) { innerPadding ->
        BodyContent1(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize().background(color = BackgroundColor),
            itemsList = VMM.notes.collectAsLazyPagingItems(),
            navController= NavController,
            searchState = rememberSaveable { mutableStateOf(true) },
            tagList = GetTestTagList()
        )
        }
    }