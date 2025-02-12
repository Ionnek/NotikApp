package com.example.achievera.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.achievera.ui.theme.TestMultyScreensTheme
import com.example.achievera.ui.view.NoteEdit.ScreenNoteEdit
import com.example.achievera.ui.view.NoteListing.ScreenNotesListing1
import com.example.achievera.ui.viewModel.NotesListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenV : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestMultyScreensTheme {
                Surface() {
                    NavigationMain()
                }
            }
        }
    }
}
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object EditNote : Screen("EditNote/{isNew}/{id}") {
        fun createRoute(isNew: Boolean, id: Long) = "EditNote/$isNew/$id"
    }
}

@Composable
fun NavigationMain() {
    val navController = rememberNavController()
    val viewModel: NotesListViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { ScreenNotesListing1(navController,viewModel) }
        composable(
            route = Screen.EditNote.route,
            arguments = listOf(
                navArgument("isNew") { type = NavType.BoolType },
                navArgument("id") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val isNew = backStackEntry.arguments?.getBoolean("isNew") ?: false
            val id = backStackEntry.arguments?.getLong("id") ?: 0
            ScreenNoteEdit(navController, isNew, id)
        }
    }
}
