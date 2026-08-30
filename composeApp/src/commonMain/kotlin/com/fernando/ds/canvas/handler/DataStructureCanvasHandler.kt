package com.fernando.ds.canvas.handler

import androidx.compose.ui.graphics.Color
import com.fernando.ds.canvas.model.CanvasEdge
import com.fernando.ds.canvas.model.CanvasNode
import com.fernando.ds.canvas.model.NodeShape

interface DataStructureCanvasHandler {
    val structureName: String
    val defaultAnnotationLabel: String
    val supportsSubtreeMovement: Boolean
    val defaultShape: NodeShape
    val defaultColor: Color

    fun createInitialState(): Pair<List<CanvasNode>, List<CanvasEdge>>
    fun calculateAnnotation(node: CanvasNode, allNodes: List<CanvasNode>, edges: List<CanvasEdge>): String
    fun canConnect(fromNodeId: String, toNodeId: String, currentEdges: List<CanvasEdge>): Boolean
}