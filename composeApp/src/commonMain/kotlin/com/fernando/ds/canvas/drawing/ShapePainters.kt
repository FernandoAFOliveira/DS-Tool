package com.fernando.ds.canvas.drawing

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.fernando.ds.canvas.model.NodeShape

fun DrawScope.drawNodeShape(
    shape: NodeShape,
    fillColor: Color,
    outlineColor: Color,
    strokeWidth: Float
) {
    when (shape) {
        NodeShape.CIRCLE -> {
            drawCircle(color = fillColor, radius = size.minDimension / 2f, style = Fill)
            drawCircle(color = outlineColor, radius = size.minDimension / 2f, style = Stroke(strokeWidth))
        }
        NodeShape.SQUARE -> {
            drawRect(color = fillColor, style = Fill)
            drawRect(color = outlineColor, style = Stroke(strokeWidth))
        }
        NodeShape.RECTANGLE -> {
            drawRoundRect(color = fillColor, style = Fill, cornerRadius = CornerRadius(16f, 16f))
            drawRoundRect(color = outlineColor, style = Stroke(strokeWidth), cornerRadius = CornerRadius(16f, 16f))
        }
        NodeShape.DIAMOND -> {
            val path = Path().apply {
                moveTo(size.width / 2f, 0f)
                lineTo(size.width, size.height / 2f)
                lineTo(size.width / 2f, size.height)
                lineTo(0f, size.height / 2f)
                close()
            }
            drawPath(path = path, color = fillColor, style = Fill)
            drawPath(path = path, color = outlineColor, style = Stroke(strokeWidth))
        }
        NodeShape.TRIANGLE -> {
            val path = Path().apply {
                moveTo(size.width / 2f, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path = path, color = fillColor, style = Fill)
            drawPath(path = path, color = outlineColor, style = Stroke(strokeWidth))
        }
    }
}