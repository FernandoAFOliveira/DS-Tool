package com.fernando.ds.canvas.handler

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.fernando.ds.canvas.model.CanvasEdge
import com.fernando.ds.canvas.model.CanvasNode
import com.fernando.ds.canvas.model.NodeShape

class TrieHandler : DataStructureCanvasHandler {
    override val structureName: String = "Trie (Prefix Tree)"
    override val defaultAnnotationLabel: String = ""
    override val supportsSubtreeMovement: Boolean = true
    override val defaultShape: NodeShape = NodeShape.SQUARE
    override val defaultColor: Color = Color(0xFFBA68C8)

    override fun createInitialState(): Pair<List<CanvasNode>, List<CanvasEdge>> {
        val nodes = listOf(
            CanvasNode(value = "ROOT", annotation = "", position = Offset(520f, 120f), shape = NodeShape.RECTANGLE, color = Color(0xFF263238)),
            CanvasNode(value = "c", annotation = "", position = Offset(360f, 260f), shape = NodeShape.SQUARE, color = defaultColor),
            CanvasNode(value = "a", annotation = "end", position = Offset(360f, 400f), shape = NodeShape.DIAMOND, color = Color(0xFF81C784)),
            CanvasNode(value = "t", annotation = "end", position = Offset(360f, 540f), shape = NodeShape.DIAMOND, color = Color(0xFF81C784))
        )
        val edges = listOf(
            CanvasEdge(fromNodeId = nodes[0].id, toNodeId = nodes[1].id),
            CanvasEdge(fromNodeId = nodes[1].id, toNodeId = nodes[2].id),
            CanvasEdge(fromNodeId = nodes[2].id, toNodeId = nodes[3].id)
        )
        return Pair(nodes, edges)
    }

    override fun calculateAnnotation(node: CanvasNode, allNodes: List<CanvasNode>, edges: List<CanvasEdge>): String = node.annotation

    override fun canConnect(fromNodeId: String, toNodeId: String, currentEdges: List<CanvasEdge>): Boolean {
        return fromNodeId != toNodeId
    }
}