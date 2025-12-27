package com.cthinhdzs1tg.meomaybeooptimizer.model

import androidx.annotation.StringRes
// R import is no longer needed since icons are provided via Compose Icons

/**
 * Represents the different optimisation modes supported by the app. Each mode
 * contains a user friendly title and subtitle. The subtitles are kept short as per
 * the spec (maximum 1 line, 28–45 characters). The benefit of the mode is clear
 * and simple.
 */
enum class Feature(val title: String, val subtitle: String) {
    Performance(
        title = "Chế độ hiệu suất",
        subtitle = "Mượt hơn, giữ FPS ổn định"
    ),
    TouchResponsiveness(
        title = "Tăng nhạy tối đa",
        subtitle = "Giảm delay chạm, phản hồi nhanh"
    ),
    MemoryClean(
        title = "Tăng RAM & dọn rác",
        subtitle = "Giải phóng RAM, giảm nặng máy"
    ),
    BatterySaver(
        title = "Tiết kiệm pin & giảm nóng",
        subtitle = "Mát hơn, pin bền mà vẫn mượt"
    ),
    FastCharging(
        title = "Hỗ trợ sạc nhanh",
        subtitle = "Tối ưu khi sạc, giảm nóng an toàn"
    ),
    NetworkOptimiser(
        title = "Tối ưu Internet",
        subtitle = "Ổn định ping, giảm giật mạng"
    );

    /**
     * @return The developer label used in snackbar messages.
     */
    fun developerLabel(): String = "Developer: Cthinhdzs1tg"
}