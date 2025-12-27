package com.cthinhdzs1tg.meomaybeooptimizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.cthinhdzs1tg.meomaybeooptimizer.ui.theme.MeoMayBeOptimizerTheme
import com.cthinhdzs1tg.meomaybeooptimizer.ui.screens.AppNavigator
import com.cthinhdzs1tg.meomaybeooptimizer.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeoMayBeOptimizerTheme {
                val appViewModel: AppViewModel = viewModel()
                AppNavigator(viewModel = appViewModel)
            }
        }
    }
}