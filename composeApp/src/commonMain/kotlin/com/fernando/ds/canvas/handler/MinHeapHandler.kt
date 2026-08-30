package com.fernando.ds.canvas.handler

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.fernando.ds.canvas.model.CanvasEdge
import com.fernando.ds.canvas.model.CanvasNode
import com.fernando.ds.canvas.model.NodeShape

class MinHeapHandler : DataStructureCanvasHandler {
    override val structureName: String = "Min / Max Heap"
    override val defaultAnnotationLabel: String = "idx: 0"
    override val supportsSubtreeMovement: Boolean = true
    override val defaultShape: NodeShape = NodeShape.CIRCLE
    override val defaultColor: Color = Color(0xFFFFB74D)

    override fun createInitialState(): Pair<List<CanvasNode>, List<CanvasEdge>> {
        val nodes = listOf(
            CanvasNode(value = "5", annotation = "idx: 0", position = Offset(520f, 150f), color = defaultColor),
            CanvasNode(value = "12", annotation = "idx: 1", position = Offset(340f, 300f), color = defaultColor),
            CanvasNode(value = "8", annotation = "idx: 2", position = Offset(700f, 300f), color = defaultColor)
        )
        val edges = listOf(
            CanvasEdge(fromNodeId = nodes[0].id, toNodeId = nodes[1].id),
            CanvasEdge(fromNodeId = nodes[0].id, toNodeId = nodes[2].id)
        )
        return Pair(nodes, edges)
    }

    override fun calculateAnnotation(node: CanvasNode, allNodes: List<CanvasNode>, edges: List<CanvasEdge>): String {
        val index = allNodes.indexOfFirst { it.id == node.id }
        return if (index >= 0) "idx: $index" else ""
    }

    override fun canConnect(fromNodeId: String, toNodeId: String, currentEdges: List<CanvasEdge>): Boolean {
        return currentEdges.count { it.fromNodeId == fromNodeId } < 2 && fromNodeId != toNodeId
    }
}