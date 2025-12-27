package com.cthinhdzs1tg.meomaybeooptimizer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatterySaver
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TouchApp
// painterResource is unused after we switched to vector icons
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cthinhdzs1tg.meomaybeooptimizer.core.ShizukuManager
import com.cthinhdzs1tg.meomaybeooptimizer.model.ActionType
import com.cthinhdzs1tg.meomaybeooptimizer.model.Feature
import com.cthinhdzs1tg.meomaybeooptimizer.model.Screen
import com.cthinhdzs1tg.meomaybeooptimizer.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AppViewModel,
    snackbarHostState: SnackbarHostState
) {
    val featureStates by viewModel.featureStates.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showPermissionDialog by remember { mutableStateOf(false) }
    // Check Shizuku permission state; if missing, show modal later.
    LaunchedEffect(Unit) {
        if (!ShizukuManager.isPermissionGranted()) {
            showPermissionDialog = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header with title and subtitle
            Text(
                text = "MeoMayBe Optimizer",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp)
            )
            Text(
                text = "Developer: Cthinhdzs1tg",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )
            // Action buttons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionCard(actionType = ActionType.Cleaner) {
                    viewModel.performAction(ActionType.Cleaner)
                }
                ActionCard(actionType = ActionType.Debloat) {
                    viewModel.performAction(ActionType.Debloat)
                }
            }
            // Spacer
            Spacer(modifier = Modifier.height(8.dp))
            // Toggle list and resolution card
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Toggles
                items(Feature.values()) { feature ->
                    ToggleCard(
                        feature = feature,
                        isEnabled = featureStates[feature] ?: false,
                        onToggle = { viewModel.toggleFeature(feature) }
                    )
                }
                // Card to navigate to Resolution Changer
                item {
                    Card(
                        onClick = { viewModel.navigateTo(Screen.ResolutionChanger) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AspectRatio,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Resoultion Changer",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Scale gọn, không méo màn",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                // Footer
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Developer: Cthinhdzs1tg",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        // Permission dialog
        if (showPermissionDialog) {
            ShizukuPermissionDialog(
                onGrant = {
                    // Attempt to request permission. We'll use request code 1000.
                    ShizukuManager.requestPermission(1000)
                    showPermissionDialog = false
                },
                onDismiss = {
                    showPermissionDialog = false
                }
            )
        }
    }
}

@Composable
private fun ActionCard(actionType: ActionType, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.weight(1f)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = actionType.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = actionType.subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ToggleCard(feature: Feature, isEnabled: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Use built-in icons for each feature; map features to icons
            val icon = when (feature) {
                Feature.Performance -> Icons.Filled.Speed
                Feature.TouchResponsiveness -> Icons.Filled.TouchApp
                Feature.MemoryClean -> Icons.Filled.Memory
                Feature.BatterySaver -> Icons.Filled.BatterySaver
                Feature.FastCharging -> Icons.Filled.BatteryChargingFull
                Feature.NetworkOptimiser -> Icons.Filled.NetworkCheck
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = feature.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = feature.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = { onToggle(it) },
                thumbContent = null,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun ShizukuPermissionDialog(onGrant: () -> Unit, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Cần quyền Shizuku", style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Text(text = "Ứng dụng cần Shizuku để tối ưu hệ thống an toàn (không cần root).", style = MaterialTheme.typography.bodyLarge)
        },
        confirmButton = {
            Button(onClick = onGrant) { Text(text = "Cấp quyền") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text(text = "Thoát") }
        }
    )
}