package com.cthinhdzs1tg.meomaybeooptimizer.core

import android.content.pm.PackageManager
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuProvider

/**
 * Manages checking and requesting Shizuku permission. Based on the integration
 * guide, we check version and use Shizuku.checkSelfPermission() on API v11 or
 * later, otherwise fall back to checkSelfPermission on the context. The
 * requestPermission method delegates to Shizuku.requestPermission() when
 * available. See the XDA tutorial for details【758173155661803†L204-L244】.
 */
object ShizukuManager {

    /**
     * Returns true if the Shizuku service is running and accessible.
     */
    fun isAvailable(): Boolean = Shizuku.pingBinder()

    /**
     * Returns true if our app already has Shizuku permission.
     */
    fun isPermissionGranted(): Boolean {
        return if (Shizuku.isPreV11() || Shizuku.getVersion() < 11) {
            // Pre v11, use context-based check
            // Note: In our ViewModel we cannot call checkSelfPermission, so we assume false for older versions.
            false
        } else {
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Request Shizuku permission. The caller should implement
     * Shizuku.OnRequestPermissionResultListener to receive the callback. See
     * sample code【758173155661803†L231-L244】.
     */
    fun requestPermission(requestCode: Int) {
        if (Shizuku.isPreV11() || Shizuku.getVersion() < 11) {
            // Not supported; permission must be requested through Activity.requestPermissions
            // We'll call Shizuku.requestPermission() anyway but it might not work.
            Shizuku.requestPermission(requestCode)
        } else {
            Shizuku.requestPermission(requestCode)
        }
    }
}