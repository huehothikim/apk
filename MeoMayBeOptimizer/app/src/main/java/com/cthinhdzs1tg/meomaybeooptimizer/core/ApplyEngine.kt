package com.cthinhdzs1tg.meomaybeooptimizer.core

import android.util.Log
import com.cthinhdzs1tg.meomaybeooptimizer.model.Feature
import kotlinx.coroutines.delay

/**
 * Engine responsible for applying or reverting optimisation settings. The
 * implementation here is simplified: it waits for a short duration to simulate
 * work and logs the operation. In production this engine would execute shell
 * commands via Shizuku or Root to adjust system settings.
 */
object ApplyEngine {

    /**
     * Apply the given feature state. Assumes that a snapshot has been taken
     * beforehand. The [enable] flag indicates whether to enable or disable the
     * feature. If [isRoot] is true, deeper optimisations may be performed.
     */
    suspend fun applyFeature(feature: Feature, enable: Boolean, isRoot: Boolean) {
        // Simulate asynchronous work with a delay
        delay(300L)
        val state = if (enable) "enabled" else "disabled"
        Log.d("ApplyEngine", "${feature.title} -> $state (root=$isRoot)")
    }

    /**
     * Apply resolution changes. Accepts width, height and density values. If
     * [isRoot] is true, deeper modifications can be performed. This method
     * simulates the apply by waiting for a duration. Return true if apply is
     * successful.
     */
    suspend fun applyResolution(width: Int, height: Int, density: Int, isRoot: Boolean): Boolean {
        delay(300L)
        Log.d("ApplyEngine", "Applying resolution ${width}x${height} density $density (root=$isRoot)")
        return true
    }
}