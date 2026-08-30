package com.fernando.ds.canvas.handler

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.fernando.ds.canvas.model.CanvasEdge
import com.fernando.ds.canvas.model.CanvasNode
import com.fernando.ds.canvas.model.NodeShape

class BstHandler : DataStructureCanvasHandler {
    override val structureName: String = "Binary Search Tree"
    override val defaultAnnotationLabel: String = ""
    override val supportsSubtreeMovement: Boolean = true
    override val defaultShape: NodeShape = NodeShape.CIRCLE
    override val defaultColor: Color = Color(0xFF64B5F6)

    override fun createInitialState(): Pair<List<CanvasNode>, List<CanvasEdge>> {
        val nodes = listOf(
            CanvasNode(value = "50", annotation = "", position = Offset(520f, 150f), color = defaultColor),
            CanvasNode(value = "30", annotation = "", position = Offset(340f, 300f), color = defaultColor),
            CanvasNode(value = "70", annotation = "", position = Offset(700f, 300f), color = defaultColor)
        )
        val edges = listOf(
            CanvasEdge(fromNodeId = nodes[0].id, toNodeId = nodes[1].id),
            CanvasEdge(fromNodeId = nodes[0].id, toNodeId = nodes[2].id)
        )
        return Pair(nodes, edges)
    }

    override fun calculateAnnotation(node: CanvasNode, allNodes: List<CanvasNode>, edges: List<CanvasEdge>): String = ""

    override fun canConnect(fromNodeId: String, toNodeId: String, currentEdges: List<CanvasEdge>): Boolean {
        return currentEdges.count { it.fromNodeId == fromNodeId } < 2 && fromNodeId != toNodeId
    }
}
