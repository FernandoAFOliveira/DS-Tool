package com.fernando.ds.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.fernando.ds.ui.MainAppScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "DS Advisor - Desktop Whiteboard",
        state = rememberWindowState(width = 1280.dp, height = 800.dp)
    ) {
        MainAppScreen()
    }
}