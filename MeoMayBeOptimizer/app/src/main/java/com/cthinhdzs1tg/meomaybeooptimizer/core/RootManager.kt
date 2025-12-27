package com.cthinhdzs1tg.meomaybeooptimizer.core

import android.os.Build
import java.io.File

/**
 * Utility for detecting root on the device. This implementation performs a
 * lightweight check by examining the build tags and scanning typical SU binary
 * paths. The code is adapted from the sample provided by Talsec's root
 * detection guide【314613512690327†L187-L215】. It does not attempt to execute
 * SU commands to avoid spawning zombie processes or requiring additional
 * permissions.
 */
object RootManager {

    /**
     * Checks whether the device appears to be rooted. Returns true if either the
     * build tags contain "test-keys" or an su binary exists on typical paths.
     */
    val isDeviceRooted: Boolean
        get() = checkBuildTags() || checkSuPaths()

    private fun checkBuildTags(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkSuPaths(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }
}