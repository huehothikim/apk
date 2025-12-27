package com.cthinhdzs1tg.meomaybeooptimizer.model

/**
 * Simple sealed class describing the top-level screens of the application.
 */
sealed class Screen {
    object Main : Screen()
    object ResolutionChanger : Screen()
}