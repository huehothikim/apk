package com.cthinhdzs1tg.meomaybeooptimizer.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cthinhdzs1tg.meomaybeooptimizer.model.Screen
import com.cthinhdzs1tg.meomaybeooptimizer.viewmodel.AppViewModel

/**
 * Root composable that displays the current screen based on [viewModel.currentScreen].
 */
@Composable
fun AppNavigator(viewModel: AppViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val snackbarHostState = viewModel.snackbarHostState
    Crossfade(targetState = currentScreen) { screen ->
        when (screen) {
            is Screen.Main -> MainScreen(viewModel, snackbarHostState)
            is Screen.ResolutionChanger -> ResolutionChangerScreen(viewModel, snackbarHostState)
        }
    }
}