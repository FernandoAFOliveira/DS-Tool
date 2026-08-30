package com.fernando.ds.canvas.handler

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.fernando.ds.canvas.model.CanvasEdge
import com.fernando.ds.canvas.model.CanvasNode
import com.fernando.ds.canvas.model.NodeShape

class AvlTreeHandler : DataStructureCanvasHandler {
    override val structureName: String = "AVL Tree"
    override val defaultAnnotationLabel: String = "BF: 0"
    override val supportsSubtreeMovement: Boolean = true
    override val defaultShape: NodeShape = NodeShape.CIRCLE
    override val defaultColor: Color = Color(0xFF81C784)

    override fun createInitialState(): Pair<List<CanvasNode>, List<CanvasEdge>> {
        val nodes = listOf(
            CanvasNode(value = "30", annotation = "BF: 0", position = Offset(520f, 150f), color = defaultColor),
            CanvasNode(value = "20", annotation = "BF: 0", position = Offset(320f, 300f), color = defaultColor),
            CanvasNode(value = "40", annotation = "BF: 0", position = Offset(720f, 300f), color = defaultColor),
            CanvasNode(value = "10", annotation = "BF: 0", position = Offset(220f, 470f), color = defaultColor),
            CanvasNode(value = "25", annotation = "BF: 0", position = Offset(420f, 470f), color = defaultColor),
            CanvasNode(value = "35", annotation = "BF: 0", position = Offset(620f, 470f), color = defaultColor),
            CanvasNode(value = "50", annotation = "BF: 0", position = Offset(820f, 470f), color = defaultColor)
        )
        val edges = listOf(
            CanvasEdge(fromNodeId = nodes[0].id, toNodeId = nodes[1].id),
            CanvasEdge(fromNodeId = nodes[0].id, toNodeId = nodes[2].id),
            CanvasEdge(fromNodeId = nodes[1].id, toNodeId = nodes[3].id),
            CanvasEdge(fromNodeId = nodes[1].id, toNodeId = nodes[4].id),
            CanvasEdge(fromNodeId = nodes[2].id, toNodeId = nodes[5].id),
            CanvasEdge(fromNodeId = nodes[2].id, toNodeId = nodes[6].id)
        )
        return Pair(nodes, edges)
    }

    override fun calculateAnnotation(node: CanvasNode, allNodes: List<CanvasNode>, edges: List<CanvasEdge>): String {
        return node.annotation.ifBlank { "BF: 0" }
    }

    override fun canConnect(fromNodeId: String, toNodeId: String, currentEdges: List<CanvasEdge>): Boolean {
        // A binary tree node can have at most 2 outgoing child edges
        val outgoingCount = currentEdges.count { it.fromNodeId == fromNodeId }
        return outgoingCount < 2 && fromNodeId != toNodeId
    }
}