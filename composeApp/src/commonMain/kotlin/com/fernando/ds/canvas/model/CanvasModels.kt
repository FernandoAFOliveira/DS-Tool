package com.fernando.ds.canvas.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import java.util.UUID

enum class NodeShape {
    CIRCLE, SQUARE, RECTANGLE, DIAMOND, TRIANGLE
}

class CanvasNode(
    val id: String = UUID.randomUUID().toString(),
    value: String = "",
    annotation: String = "",
    position: Offset = Offset(300f, 300f),
    shape: NodeShape = NodeShape.CIRCLE,
    color: Color = Color(0xFF81C784)
) {
    var value by mutableStateOf(value)
    var annotation by mutableStateOf(annotation)
    var position by mutableStateOf(position)
    var shape by mutableStateOf(shape)
    var color by mutableStateOf(color)
}

data class CanvasEdge(
    val id: String = UUID.randomUUID().toString(),
    val fromNodeId: String,
    val toNodeId: String
)