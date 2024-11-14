package com.example.achievera.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.ui.theme.BackgroundColor
import com.example.achievera.ui.theme.TestMultyScreensTheme
import com.example.achievera.ui.viewModel.MockNotesListViewModel

