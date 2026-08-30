package com.fernando.ds.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.fernando.ds.canvas.drawing.drawNodeShape
import com.fernando.ds.canvas.handler.*
import com.fernando.ds.canvas.model.CanvasEdge
import com.fernando.ds.canvas.model.CanvasNode
import com.fernando.ds.canvas.model.NodeShape
import com.fernando.ds.canvas.state.CanvasViewportState
import com.fernando.ds.canvas.ui.CanvasScrollbars
import com.fernando.ds.canvas.ui.CanvasSidebar
import com.fernando.ds.canvas.ui.CanvasTopToolbar
import java.util.UUID
import kotlin.math.hypot
import kotlin.math.roundToInt

@Composable
fun TreeCanvasScreen(onNavigateBack: () -> Unit = {}) {
    val handlers = remember { listOf(AvlTreeHandler(), BstHandler(), MinHeapHandler(), TrieHandler()) }
    var selectedHandlerIndex by remember { mutableIntStateOf(0) }
    val activeHandler = handlers[selectedHandlerIndex]

    val nodes = remember { mutableStateListOf<CanvasNode>() }
    val edges = remember { mutableStateListOf<CanvasEdge>() }
    val viewport = remember { CanvasViewportState(gridCols = 4, gridRows = 4) }

    LaunchedEffect(selectedHandlerIndex) {
        val (initNodes, initEdges) = activeHandler.createInitialState()
        nodes.clear()
        nodes.addAll(initNodes)
        edges.clear()
        edges.addAll(initEdges)
    }

    var activeColor by remember { mutableStateOf(activeHandler.defaultColor) }
    var selectedShapeType by remember { mutableStateOf(activeHandler.defaultShape) }
    var selectedNodeIds by remember { mutableStateOf(setOf<String>()) }
    var selectedEdgeId by remember { mutableStateOf<String?>(null) }
    var moveSubtreeMode by remember { mutableStateOf(activeHandler.supportsSubtreeMovement) }

    var marqueeStart by remember { mutableStateOf<Offset?>(null) }
    var marqueeEnd by remember { mutableStateOf<Offset?>(null) }
    val focusRequester = remember { FocusRequester() }

    val primarySelectedNode = nodes.find { it.id == selectedNodeIds.firstOrNull() }
    var sidebarValueText by remember { mutableStateOf("60") }
    var sidebarAnnotationText by remember { mutableStateOf(activeHandler.defaultAnnotationLabel) }

    LaunchedEffect(primarySelectedNode) {
        primarySelectedNode?.let {
            sidebarValueText = it.value
            sidebarAnnotationText = it.annotation
        }
    }

    fun getSubtreeIds(rootId: String): Set<String> {
        val result = mutableSetOf(rootId)
        val queue = ArrayDeque<String>().apply { add(rootId) }
        while (queue.isNotEmpty()) {
            val curr = queue.removeFirst()
            edges.filter { it.fromNodeId == curr }.map { it.toNodeId }.forEach { child ->
                if (result.add(child)) queue.add(child)
            }
        }
        return result
    }

    fun deleteSelected() {
        if (selectedNodeIds.isNotEmpty()) {
            val toDelete = selectedNodeIds
            nodes.removeAll { it.id in toDelete }
            edges.removeAll { it.fromNodeId in toDelete || it.toNodeId in toDelete }
            selectedNodeIds = emptySet()
        } else if (selectedEdgeId != null) {
            edges.removeAll { it.id == selectedEdgeId }
            selectedEdgeId = null
        }
    }

    fun duplicateSelected() {
        val selectedList = nodes.filter { it.id in selectedNodeIds }
        if (selectedList.isEmpty()) return

        val mapping = mutableMapOf<String, String>()
        val newNodes = selectedList.map { old ->
            val newId = UUID.randomUUID().toString()
            mapping[old.id] = newId
            CanvasNode(
                id = newId,
                value = old.value,
                annotation = old.annotation,
                position = old.position + Offset(120f, 120f),
                shape = old.shape,
                color = old.color
            )
        }
        val newEdges = edges.filter { it.fromNodeId in selectedNodeIds && it.toNodeId in selectedNodeIds }.map {
            CanvasEdge(
                fromNodeId = mapping[it.fromNodeId] ?: it.fromNodeId,
                toNodeId = mapping[it.toNodeId] ?: it.toNodeId
            )
        }
        nodes.addAll(newNodes)
        edges.addAll(newEdges)
        selectedNodeIds = newNodes.map { it.id }.toSet()
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.Delete, Key.Backspace -> { deleteSelected(); true }
                        Key.Escape -> { selectedNodeIds = emptySet(); selectedEdgeId = null; true }
                        Key.D -> if (event.isCtrlPressed || event.isMetaPressed) { duplicateSelected(); true } else false
                        else -> false
                    }
                } else false
            }
    ) {
        CanvasTopToolbar(
            handlers = handlers,
            selectedHandlerIndex = selectedHandlerIndex,
            onSelectHandler = { selectedHandlerIndex = it },
            moveSubtreeMode = moveSubtreeMode,
            onToggleSubtreeMode = { moveSubtreeMode = !moveSubtreeMode },
            activeColor = activeColor,
            onSelectColor = { color ->
                activeColor = color
                selectedNodeIds.forEach { id -> nodes.find { it.id == id }?.color = color }
            },
            selectedNodeIds = selectedNodeIds,
            edges = edges,
            onConnectNodes = {
                val list = selectedNodeIds.toList()
                if (list.size == 2 && activeHandler.canConnect(list[0], list[1], edges)) {
                    edges.add(CanvasEdge(fromNodeId = list[0], toNodeId = list[1]))
                }
                selectedNodeIds = emptySet()
            },
            onCutEdge = {
                val list = selectedNodeIds.toList()
                if (list.size == 2) {
                    edges.removeAll { (it.fromNodeId == list[0] && it.toNodeId == list[1]) || (it.fromNodeId == list[1] && it.toNodeId == list[0]) }
                    selectedNodeIds = emptySet()
                } else if (selectedEdgeId != null) {
                    edges.removeAll { it.id == selectedEdgeId }
                    selectedEdgeId = null
                }
            },
            onDuplicate = ::duplicateSelected,
            onDelete = ::deleteSelected,
            scalePercent = (viewport.scale * 100).toInt(),
            onZoomIn = { viewport.zoomAt(Offset(viewport.viewportSize.width / 2f, viewport.viewportSize.height / 2f), 1.15f) },
            onZoomOut = { viewport.zoomAt(Offset(viewport.viewportSize.width / 2f, viewport.viewportSize.height / 2f), 0.85f) },
            onFitAll = { viewport.zoomToFitAll(nodes) },
            onResetZoom = { viewport.resetToDefault() },
            onClearAll = {
                nodes.clear()
                edges.clear()
                selectedNodeIds = emptySet()
                selectedEdgeId = null
                viewport.resetToDefault()
            },
            onNavigateBack = onNavigateBack
        )

        Row(modifier = Modifier.fillMaxSize()) {
            CanvasSidebar(
                activeColor = activeColor,
                selectedShapeType = selectedShapeType,
                onSelectShape = { selectedShapeType = it },
                primarySelectedNode = primarySelectedNode,
                sidebarValueText = sidebarValueText,
                onValueChange = {
                    sidebarValueText = it
                    primarySelectedNode?.value = it
                },
                sidebarAnnotationText = sidebarAnnotationText,
                onAnnotationChange = {
                    sidebarAnnotationText = it
                    primarySelectedNode?.annotation = it
                },
                viewport = viewport,
                onAddSpawnedNode = { nodes.add(it) }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clipToBounds()
                    .background(Color(0xFFF1F8E9))
                    .onSizeChanged { viewport.viewportSize = it.toSize() }
                    // 1. Mouse Scroll Wheel Listener (Pass-through without blocking taps)
                    .pointerInput(viewport.scale, viewport.offset) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                if (event.type == PointerEventType.Scroll) {
                                    val delta = event.changes.firstOrNull()?.scrollDelta?.y ?: 0f
                                    if (delta != 0f) {
                                        val cursor = event.changes.first().position
                                        val factor = if (delta < 0) 1.12f else 0.88f
                                        viewport.zoomAt(cursor, factor)
                                    }
                                }
                            }
                        }
                    }
                    // 2. Right-Click Drag to Pan & Tap-Outside Deselect
                    .pointerInput(viewport.scale, viewport.offset) {
                        detectTapGestures(
                            onTap = {
                                selectedNodeIds = emptySet()
                                selectedEdgeId = null
                            }
                        )
                    }
                    .pointerInput(viewport.scale, viewport.offset) {
                        detectDragGestures(
                            onDragStart = { startPos ->
                                marqueeStart = (startPos - viewport.offset) / viewport.scale
                                marqueeEnd = marqueeStart
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (change.pressed && change.type.toString().contains("Mouse")) {
                                    // Secondary/Right-Click or Middle-Click Pan
                                    viewport.panBy(dragAmount)
                                } else {
                                    marqueeEnd = (change.position - viewport.offset) / viewport.scale
                                }
                            },
                            onDragEnd = {
                                val s = marqueeStart
                                val e = marqueeEnd
                                if (s != null && e != null) {
                                    val rect = Rect(minOf(s.x, e.x), minOf(s.y, e.y), maxOf(s.x, e.x), maxOf(s.y, e.y))
                                    if (rect.width > 15f || rect.height > 15f) {
                                        selectedNodeIds = nodes.filter { rect.contains(it.position) }.map { it.id }.toSet()
                                    }
                                }
                                marqueeStart = null
                                marqueeEnd = null
                            }
                        )
                    }
            ) {
                // Transformed World Space
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(0f, 0f)
                            scaleX = viewport.scale
                            scaleY = viewport.scale
                            translationX = viewport.offset.x
                            translationY = viewport.offset.y
                        }
                ) {
                    // Page Grid & Guidelines
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val pageWidth = viewport.viewportSize.width
                        val pageHeight = viewport.viewportSize.height

                        var x = pageWidth
                        while (x < viewport.worldWidth) {
                            drawLine(
                                color = Color(0x3337474F),
                                start = Offset(x, 0f),
                                end = Offset(x, viewport.worldHeight),
                                strokeWidth = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                            )
                            x += pageWidth
                        }

                        var y = pageHeight
                        while (y < viewport.worldHeight) {
                            drawLine(
                                color = Color(0x3337474F),
                                start = Offset(0f, y),
                                end = Offset(viewport.worldWidth, y),
                                strokeWidth = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                            )
                            y += pageHeight
                        }

                        drawRect(
                            color = Color(0x442E7D32),
                            topLeft = Offset.Zero,
                            size = Size(viewport.worldWidth, viewport.worldHeight),
                            style = Stroke(3f)
                        )
                    }

                    // Tree Edges & Marquee Overlay
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(edges.size) {
                                detectTapGestures { tapOffset ->
                                    val nodeMap = nodes.associateBy { it.id }
                                    val hit = edges.find { edge ->
                                        val p1 = nodeMap[edge.fromNodeId]?.position ?: return@find false
                                        val p2 = nodeMap[edge.toNodeId]?.position ?: return@find false
                                        distanceToSegment(tapOffset, p1, p2) <= 30f
                                    }
                                    selectedEdgeId = hit?.id
                                    if (hit != null) selectedNodeIds = emptySet()
                                }
                            }
                    ) {
                        val nodeMap = nodes.associateBy { it.id }
                        edges.forEach { edge ->
                            val from = nodeMap[edge.fromNodeId]
                            val to = nodeMap[edge.toNodeId]
                            if (from != null && to != null) {
                                val isSelected = selectedEdgeId == edge.id
                                drawLine(
                                    color = if (isSelected) Color(0xFFD32F2F) else Color(0xFF37474F),
                                    start = from.position,
                                    end = to.position,
                                    strokeWidth = if (isSelected) 8f else 5f,
                                    cap = StrokeCap.Round
                                )
                            }
                        }

                        val s = marqueeStart
                        val e = marqueeEnd
                        if (s != null && e != null) {
                            val rect = Rect(minOf(s.x, e.x), minOf(s.y, e.y), maxOf(s.x, e.x), maxOf(s.y, e.y))
                            drawRect(Color(0x331976D2), rect.topLeft, rect.size)
                            drawRect(
                                Color(0xFF1976D2),
                                rect.topLeft,
                                rect.size,
                                style = Stroke(2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                            )
                        }
                    }

                    // Canvas Nodes with Pure Direct Gesture Dispatching
                    nodes.forEach { node ->
                        val isSelected = node.id in selectedNodeIds
                        val isCharcoal = node.color == Color(0xFF263238)

                        Box(
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        (node.position.x - 38.dp.toPx()).roundToInt(),
                                        (node.position.y - 38.dp.toPx()).roundToInt()
                                    )
                                }
                                .pointerInput(node.id) {
                                    detectTapGestures(
                                        onTap = {
                                            selectedEdgeId = null
                                            selectedNodeIds = if (selectedNodeIds.contains(node.id)) {
                                                selectedNodeIds - node.id
                                            } else {
                                                selectedNodeIds + node.id
                                            }
                                        }
                                    )
                                }
                                .pointerInput(node.id, moveSubtreeMode, viewport.scale, selectedNodeIds) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        val scaled = dragAmount / viewport.scale

                                        val targets = if (node.id in selectedNodeIds && selectedNodeIds.size > 1) {
                                            selectedNodeIds
                                        } else if (moveSubtreeMode) {
                                            getSubtreeIds(node.id)
                                        } else {
                                            setOf(node.id)
                                        }

                                        targets.forEach { targetId ->
                                            nodes.find { it.id == targetId }?.let { targetNode ->
                                                targetNode.position = Offset(
                                                    x = (targetNode.position.x + scaled.x).coerceIn(40f, viewport.worldWidth - 40f),
                                                    y = (targetNode.position.y + scaled.y).coerceIn(40f, viewport.worldHeight - 40f)
                                                )
                                            }
                                        }
                                    }
                                }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (node.annotation.isNotBlank()) {
                                    Text(
                                        text = node.annotation,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF263238),
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }

                                Box(modifier = Modifier.size(76.dp), contentAlignment = Alignment.Center) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawNodeShape(
                                            node.shape,
                                            node.color,
                                            if (isSelected) Color(0xFF1976D2) else Color(0xFF1B5E20),
                                            if (isSelected) 8f else 3f
                                        )
                                    }
                                    Text(
                                        text = node.value,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCharcoal) Color.White else Color(0xFF1B5E20)
                                    )
                                }
                            }
                        }
                    }
                }

                // Interactive Scrollbars on top layer
                CanvasScrollbars(viewport)
            }
        }
    }
}

private fun distanceToSegment(p: Offset, v: Offset, w: Offset): Float {
    val l2 = (v.x - w.x) * (v.x - w.x) + (v.y - w.y) * (v.y - w.y)
    if (l2 == 0f) return hypot(p.x - v.x, p.y - v.y)
    val t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2
    val clampedT = t.coerceIn(0f, 1f)
    val projection = Offset(v.x + clampedT * (w.x - v.x), v.y + clampedT * (w.y - v.y))
    return hypot(p.x - projection.x, p.y - projection.y)
}