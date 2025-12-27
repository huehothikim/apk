package com.cthinhdzs1tg.meomaybeooptimizer.model

/**
 * Represents global one-shot actions accessible from the main screen.
 */
enum class ActionType(val title: String, val subtitle: String) {
    Cleaner(title = "Dọn dẹp hệ thống", subtitle = "Dọn rác & tối ưu app nền an toàn"),
    Debloat(title = "Debloat", subtitle = "Gỡ bloatware theo máy, có khôi phục")
}