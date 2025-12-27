package com.cthinhdzs1tg.meomaybeooptimizer.core

import com.cthinhdzs1tg.meomaybeooptimizer.model.Feature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Handles snapshot creation and restoration for system settings. This manager
 * stores snapshots in-memory for demonstration purposes. In a real
 * implementation you would persist to DataStore or encrypted preferences as
 * described in the specifications.
 */
object BackupManager {
    private val snapshots: MutableMap<Feature, Map<String, Any?>> = mutableMapOf()

    /**
     * Create a snapshot for the given feature. The provided state is arbitrary
     * key/value pairs representing system properties that will be modified. The
     * snapshot is stored to allow later restoration.
     */
    suspend fun createSnapshot(feature: Feature, currentState: Map<String, Any?>) {
        withContext(Dispatchers.IO) {
            snapshots[feature] = currentState.toMutableMap()
        }
    }

    /**
     * Restore the state for the given feature. Returns the previously stored
     * state or null if none exists.
     */
    suspend fun restoreSnapshot(feature: Feature): Map<String, Any?>? {
        return withContext(Dispatchers.IO) {
            snapshots.remove(feature)
        }
    }
}