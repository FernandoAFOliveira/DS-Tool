package com.fernando.ds.canvas.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.fernando.ds.canvas.state.CanvasViewportState

@Composable
fun BoxScope.CanvasScrollbars(viewport: CanvasViewportState) {
    val barThickness = 16.dp

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.BottomEnd)
            .pointerInput(viewport.scale, viewport.offset, viewport.viewportSize) {
                detectTapGestures { tapPos ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    val thicknessPx = barThickness.toPx()

                    if (tapPos.y >= h - thicknessPx) {
                        viewport.setHorizontalNormalized(tapPos.x / (w - thicknessPx))
                    } else if (tapPos.x >= w - thicknessPx) {
                        viewport.setVerticalNormalized(tapPos.y / (h - thicknessPx))
                    }
                }
            }
            .pointerInput(viewport.scale, viewport.offset, viewport.viewportSize) {
                detectDragGestures { change, dragAmount ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    val touch = change.position
                    val thicknessPx = barThickness.toPx()
                    change.consume()

                    val maxPanX = (viewport.worldWidth * viewport.scale - w).coerceAtLeast(1f)
                    val maxPanY = (viewport.worldHeight * viewport.scale - h).coerceAtLeast(1f)

                    if (touch.y >= h - thicknessPx * 2.5f) {
                        val deltaNormalizedX = dragAmount.x / (w - thicknessPx)
                        viewport.panBy(Offset(-deltaNormalizedX * maxPanX, 0f))
                    } else if (touch.x >= w - thicknessPx * 2.5f) {
                        val deltaNormalizedY = dragAmount.y / (h - thicknessPx)
                        viewport.panBy(Offset(0f, -deltaNormalizedY * maxPanY))
                    }
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val thicknessPx = barThickness.toPx()

        val totalW = viewport.worldWidth * viewport.scale
        val totalH = viewport.worldHeight * viewport.scale

        val maxPanX = (totalW - w).coerceAtLeast(1f)
        val maxPanY = (totalH - h).coerceAtLeast(1f)

        val thumbW = ((w / totalW) * (w - thicknessPx)).coerceIn(50f, w - thicknessPx)
        val thumbH = ((h / totalH) * (h - thicknessPx)).coerceIn(50f, h - thicknessPx)

        val normX = (-viewport.offset.x / maxPanX).coerceIn(0f, 1f)
        val normY = (-viewport.offset.y / maxPanY).coerceIn(0f, 1f)

        val thumbX = normX * (w - thicknessPx - thumbW)
        val thumbY = normY * (h - thicknessPx - thumbH)

        // Bottom Track & Thumb
        drawRect(
            color = Color(0x33000000),
            topLeft = Offset(0f, h - thicknessPx),
            size = Size(w - thicknessPx, thicknessPx)
        )
        drawRoundRect(
            color = Color(0xFF424242),
            topLeft = Offset(thumbX, h - thicknessPx + 2f),
            size = Size(thumbW, thicknessPx - 4f),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // Right Track & Thumb
        drawRect(
            color = Color(0x33000000),
            topLeft = Offset(w - thicknessPx, 0f),
            size = Size(thicknessPx, h - thicknessPx)
        )
        drawRoundRect(
            color = Color(0xFF424242),
            topLeft = Offset(w - thicknessPx + 2f, thumbY),
            size = Size(thicknessPx - 4f, thumbH),
            cornerRadius = CornerRadius(4f, 4f)
        )
    }
}