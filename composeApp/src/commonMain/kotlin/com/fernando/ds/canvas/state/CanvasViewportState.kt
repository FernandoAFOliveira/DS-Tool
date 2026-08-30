package com.fernando.ds.canvas.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.fernando.ds.canvas.model.CanvasNode

class CanvasViewportState(
    val gridCols: Int = 4,
    val gridRows: Int = 4
) {
    var scale by mutableFloatStateOf(1f)
    var offset by mutableStateOf(Offset.Zero)
    var viewportSize by mutableStateOf(Size(1200f, 800f))

    val worldWidth: Float get() = (viewportSize.width.takeIf { it > 0f } ?: 1200f) * gridCols
    val worldHeight: Float get() = (viewportSize.height.takeIf { it > 0f } ?: 800f) * gridRows

    val minScale: Float = 0.25f // 25% = full 4x4 overview
    val maxScale: Float = 1.50f // 150% = close inspection

    fun zoomAt(cursorPos: Offset, zoomFactor: Float) {
        val oldScale = scale
        val newScale = (oldScale * zoomFactor).coerceIn(minScale, maxScale)
        if (newScale == oldScale) return

        val worldPoint = (cursorPos - offset) / oldScale
        val targetOffset = cursorPos - worldPoint * newScale
        scale = newScale
        offset = clampOffset(targetOffset, newScale)
    }

    fun panBy(delta: Offset) {
        offset = clampOffset(offset + delta, scale)
    }

    fun setHorizontalNormalized(normX: Float) {
        val maxPanX = (worldWidth * scale - viewportSize.width).coerceAtLeast(0f)
        offset = clampOffset(Offset(-normX.coerceIn(0f, 1f) * maxPanX, offset.y), scale)
    }

    fun setVerticalNormalized(normY: Float) {
        val maxPanY = (worldHeight * scale - viewportSize.height).coerceAtLeast(0f)
        offset = clampOffset(Offset(offset.x, -normY.coerceIn(0f, 1f) * maxPanY), scale)
    }

    fun resetToDefault() {
        scale = 1f
        offset = Offset.Zero
    }

    fun zoomToFitAll(nodes: List<CanvasNode>) {
        if (nodes.isEmpty()) {
            resetToDefault()
            return
        }
        val minX = nodes.minOf { it.position.x } - 100f
        val maxX = nodes.maxOf { it.position.x } + 100f
        val minY = nodes.minOf { it.position.y } - 100f
        val maxY = nodes.maxOf { it.position.y } + 100f

        val boundingW = (maxX - minX).coerceAtLeast(200f)
        val boundingH = (maxY - minY).coerceAtLeast(200f)

        val fitScaleX = viewportSize.width / boundingW
        val fitScaleY = viewportSize.height / boundingH
        val targetScale = minOf(fitScaleX, fitScaleY).coerceIn(minScale, 1.2f)

        scale = targetScale
        offset = clampOffset(Offset(-minX * targetScale + 40f, -minY * targetScale + 40f), targetScale)
    }

    private fun clampOffset(target: Offset, currentScale: Float): Offset {
        val maxPanX = (worldWidth * currentScale - viewportSize.width).coerceAtLeast(0f)
        val maxPanY = (worldHeight * currentScale - viewportSize.height).coerceAtLeast(0f)
        return Offset(
            x = target.x.coerceIn(-maxPanX, 0f),
            y = target.y.coerceIn(-maxPanY, 0f)
        )
    }
}