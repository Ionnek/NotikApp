/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.borzykh.achievera.ui.theme.TestMultyScreensTheme
import com.borzykh.achievera.ui.view.NoteEdit.ScreenNoteEdit
import com.borzykh.achievera.ui.view.NoteListing.ScreenNotesListing1
import com.borzykh.achievera.ui.viewModel.NotesListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenV : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var noteId:Long= intent.getLongExtra("noteId",-1)
        enableEdgeToEdge()
        setContent {
            TestMultyScreensTheme {
                Surface() {
                    NavigationMain(noteId)
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
    object Settings :Screen("SettingsScreen")
    object TagEdit:Screen("TagEditScreen")

}

@Composable
fun NavigationMain(
    noteId:Long
) {
    val navController = rememberNavController()
    val viewModel: NotesListViewModel = hiltViewModel()
    LaunchedEffect(noteId) {
        if (noteId != -1L) {
            navController.navigate(
                Screen.EditNote.createRoute(isNew = false, id = noteId)
            ) {
                popUpTo(Screen.Home.route) { inclusive = false }
                launchSingleTop = true
            }
        }
    }
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
        composable(Screen.TagEdit.route){
            TagEditContent(viewModel=viewModel,navController=navController)
        }
        composable(Screen.Settings.route){
            SettingsScreen(viewModel = viewModel,navController=navController)
        }
    }
}
