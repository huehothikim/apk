package com.cthinhdzs1tg.meomaybeooptimizer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.cthinhdzs1tg.meomaybeooptimizer.viewmodel.AppViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Screen for the "Resoultion Changer" feature. It allows users to select a
 * uniform scale to adjust screen resolution safely. It displays the computed
 * width, height and density and provides buttons to reset or apply the change.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResolutionChangerScreen(
    viewModel: AppViewModel,
    snackbarHostState: SnackbarHostState
) {
    // State for bottom sheet open/closed
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val selectedScale by viewModel.resolutionScale.collectAsState()
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    // Compute base pixel size
    val baseWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val baseHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    val baseDensityDpi = configuration.densityDpi
    // Compute new values using selected scale
    val computedWidthPx = ((baseWidthPx * selectedScale).roundToInt() / 2) * 2
    val computedHeightPx = ((baseHeightPx * selectedScale).roundToInt() / 2) * 2
    val computedDensity = (baseDensityDpi * selectedScale).roundToInt()
    val aspectRatioDiff = abs((computedWidthPx.toFloat() / computedHeightPx) - (baseWidthPx / baseHeightPx))
    val isAspectSafe = aspectRatioDiff <= 0.02f
    val isScaleSafe = selectedScale in 0.7f..1.3f

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Resoultion Changer",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Pick scale button
            Button(onClick = { coroutineScope.launch { sheetState.show() } }) {
                Text(text = "PICK SCALE: ${String.format("%.1fx", selectedScale)}")
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Display values
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Width: $computedWidthPx px", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Height: $computedHeightPx px", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Density: $computedDensity dpi", style = MaterialTheme.typography.bodyLarge)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Action buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { viewModel.resetResolution() }, modifier = Modifier.weight(1f)) {
                    Text(text = "RESET")
                }
                Button(
                    onClick = {
                        if (isAspectSafe && isScaleSafe) {
                            viewModel.applyResolution(computedWidthPx, computedHeightPx, computedDensity)
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Scale không an toàn, vui lòng chọn khác",
                                    duration = androidx.compose.material3.SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = isAspectSafe && isScaleSafe
                ) {
                    Text(text = "APPLY")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Scale an toàn, giữ đúng tỉ lệ màn hình.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        // Bottom sheet for selecting scales
        if (sheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = { coroutineScope.launch { sheetState.hide() } },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Chọn tỉ lệ (0.5x – 2.0x)", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    val scales = listOf(0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f, 2.0f)
                    scales.forEach { scale ->
                        Button(
                            onClick = {
                                viewModel.selectResolutionScale(scale)
                                coroutineScope.launch { sheetState.hide() }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(text = String.format("%.1fx", scale))
                        }
                    }
                }
            }
        }
    }
}