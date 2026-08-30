package com.fernando.ds.canvas.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun ConnectEdgeIconButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .background(
                color = if (enabled) Color(0xFFFFEB3B) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.5.dp,
                color = if (enabled) Color(0xFFFBC02D) else Color(0xFFBDBDBD),
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        Canvas(modifier = Modifier.size(38.dp)) {
            // Draw horizontal solid bar representing an edge
            drawLine(
                color = if (enabled) Color(0xFF212121) else Color(0xFF9E9E9E),
                start = Offset(6.dp.toPx(), size.height / 2f),
                end = Offset(size.width - 6.dp.toPx(), size.height / 2f),
                strokeWidth = 4.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun CutEdgeIconButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .background(
                color = if (enabled) Color(0xFFFFEB3B) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.5.dp,
                color = if (enabled) Color(0xFFFBC02D) else Color(0xFFBDBDBD),
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        Canvas(modifier = Modifier.size(38.dp)) {
            // Draw baseline edge
            drawLine(
                color = if (enabled) Color(0xFF212121) else Color(0xFF9E9E9E),
                start = Offset(6.dp.toPx(), size.height / 2f),
                end = Offset(size.width - 6.dp.toPx(), size.height / 2f),
                strokeWidth = 4.5.dp.toPx(),
                cap = StrokeCap.Round
            )
            // Draw red cut cross
            if (enabled) {
                drawLine(
                    color = Color(0xFFD32F2F),
                    start = Offset(8.dp.toPx(), 8.dp.toPx()),
                    end = Offset(size.width - 8.dp.toPx(), size.height - 8.dp.toPx()),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFFD32F2F),
                    start = Offset(size.width - 8.dp.toPx(), 8.dp.toPx()),
                    end = Offset(8.dp.toPx(), size.height - 8.dp.toPx()),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}