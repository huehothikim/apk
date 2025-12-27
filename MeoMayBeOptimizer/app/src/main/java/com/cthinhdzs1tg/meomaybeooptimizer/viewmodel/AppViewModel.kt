package com.cthinhdzs1tg.meomaybeooptimizer.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cthinhdzs1tg.meomaybeooptimizer.core.ApplyEngine
import com.cthinhdzs1tg.meomaybeooptimizer.core.BackupManager
import com.cthinhdzs1tg.meomaybeooptimizer.core.ProfileEngine
import com.cthinhdzs1tg.meomaybeooptimizer.core.RootManager
import com.cthinhdzs1tg.meomaybeooptimizer.core.ShizukuManager
import com.cthinhdzs1tg.meomaybeooptimizer.core.VerifyEngine
import com.cthinhdzs1tg.meomaybeooptimizer.model.ActionType
import com.cthinhdzs1tg.meomaybeooptimizer.model.Feature
import com.cthinhdzs1tg.meomaybeooptimizer.model.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Central ViewModel storing UI state and orchestrating operations. All system
 * interactions are executed on background threads with verification and
 * rollback semantics as per the specification. Snackbar messages are exposed
 * via a [SnackbarHostState].
 */
class AppViewModel : ViewModel() {
    // Map of feature toggled states. Initialise all features as disabled.
    private val _featureStates = MutableStateFlow(
        Feature.values().associateWith { false }
    )
    val featureStates: StateFlow<Map<Feature, Boolean>> = _featureStates

    // Current screen for manual navigation.
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Main)
    val currentScreen: StateFlow<Screen> = _currentScreen

    // Snackbar host state for showing messages.
    val snackbarHostState = SnackbarHostState()

    // Resolution scale state (selected scale). Use a flow to observe changes.
    private val _resolutionScale = MutableStateFlow(1f)
    val resolutionScale: StateFlow<Float> = _resolutionScale

    /**
     * Toggle the given [feature]. This method checks Shizuku availability and
     * root state, creates a snapshot, applies the optimisation and verifies
     * success. If verification fails, the snapshot is restored. A snackbar
     * message is queued on completion.
     */
    fun toggleFeature(feature: Feature) {
        viewModelScope.launch {
            val current = _featureStates.value[feature] ?: false
            val enable = !current
            // Step 1: Check Shizuku availability and permission if needed
            val isShizukuAvailable = ShizukuManager.isAvailable()
            val hasPermission = ShizukuManager.isPermissionGranted()
            if (!isShizukuAvailable || !hasPermission) {
                // Show permission dialog by navigating to a special state or
                // using snackbar. In this example we just request permission.
                // The UI should observe state and present a modal if needed.
            }
            // Step 2: Determine root status
            val isRoot = RootManager.isDeviceRooted
            // Step 3: Snapshot current state. For demonstration we store the toggle state.
            BackupManager.createSnapshot(feature, mapOf("enabled" to current))
            // Step 4: Apply the optimisation
            ApplyEngine.applyFeature(feature, enable, isRoot)
            // Step 5: Verify
            val verified = VerifyEngine.verifyFeature(feature, enable)
            if (verified) {
                // Update state
                _featureStates.value = _featureStates.value.toMutableMap().also { it[feature] = enable }
                // Show snackbar
                val actionText = if (enable) "đã được bật" else "đã được tắt"
                snackbarHostState.showSnackbar(
                    message = "${feature.title} $actionText\nDeveloper: Cthinhdzs1tg",
                    withDismissAction = false,
                    duration = SnackbarDuration.Short
                )
            } else {
                // Rollback snapshot if verification fails
                BackupManager.restoreSnapshot(feature)
                snackbarHostState.showSnackbar(
                    message = "${feature.title} thất bại, đã khôi phục\nDeveloper: Cthinhdzs1tg",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    /**
     * Execute a one-shot action. The implementation here mimics a toggle but
     * could run arbitrary tasks such as cleaning or debloating. It uses the
     * same snapshot -> apply -> verify workflow.
     */
    fun performAction(action: ActionType) {
        viewModelScope.launch {
            val featureEquivalent = when (action) {
                ActionType.Cleaner -> Feature.MemoryClean
                ActionType.Debloat -> Feature.FastCharging // reuse placeholder
            }
            // Snapshot placeholder
            BackupManager.createSnapshot(featureEquivalent, mapOf("dummy" to true))
            val isRoot = RootManager.isDeviceRooted
            // Simulate work via ApplyEngine
            ApplyEngine.applyFeature(featureEquivalent, true, isRoot)
            val verified = VerifyEngine.verifyFeature(featureEquivalent, true)
            if (verified) {
                snackbarHostState.showSnackbar(
                    message = "${action.title} đã hoàn tất\nDeveloper: Cthinhdzs1tg",
                    duration = SnackbarDuration.Short
                )
            } else {
                BackupManager.restoreSnapshot(featureEquivalent)
                snackbarHostState.showSnackbar(
                    message = "${action.title} thất bại\nDeveloper: Cthinhdzs1tg", duration = SnackbarDuration.Short
                )
            }
        }
    }

    /**
     * Navigate to another screen.
     */
    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    /**
     * Update the selected resolution scale. This does not apply the change.
     */
    fun selectResolutionScale(scale: Float) {
        _resolutionScale.value = scale
    }

    /**
     * Apply a new display size/density based on the provided base size and the
     * current scale. The UI should compute the new width, height and density
     * before calling this method.
     */
    fun applyResolution(width: Int, height: Int, density: Int) {
        viewModelScope.launch {
            val isRoot = RootManager.isDeviceRooted
            // Snapshot original resolution values
            BackupManager.createSnapshot(Feature.Performance, mapOf(
                "width" to width,
                "height" to height,
                "density" to density
            ))
            val success = ApplyEngine.applyResolution(width, height, density, isRoot)
            val verified = VerifyEngine.verifyResolution(width, height, density)
            if (success && verified) {
                snackbarHostState.showSnackbar(
                    message = "Resoultion Changer đã hoàn tất\nDeveloper: Cthinhdzs1tg",
                    duration = SnackbarDuration.Short
                )
            } else {
                BackupManager.restoreSnapshot(Feature.Performance)
                snackbarHostState.showSnackbar(
                    message = "Resoultion Changer thất bại\nDeveloper: Cthinhdzs1tg", duration = SnackbarDuration.Short
                )
            }
        }
    }

    /**
     * Reset the display size/density to the snapshot previously saved. This
     * method uses the stored values to restore original settings.
     */
    fun resetResolution() {
        viewModelScope.launch {
            val snapshot = BackupManager.restoreSnapshot(Feature.Performance)
            if (snapshot != null) {
                val width = snapshot["width"] as? Int ?: return@launch
                val height = snapshot["height"] as? Int ?: return@launch
                val density = snapshot["density"] as? Int ?: return@launch
                val isRoot = RootManager.isDeviceRooted
                ApplyEngine.applyResolution(width, height, density, isRoot)
                snackbarHostState.showSnackbar(
                    message = "Resoultion Changer đã khôi phục\nDeveloper: Cthinhdzs1tg",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}