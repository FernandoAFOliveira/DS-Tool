package com.fernando.ds.canvas.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fernando.ds.canvas.drawing.drawNodeShape
import com.fernando.ds.canvas.model.CanvasNode
import com.fernando.ds.canvas.model.NodeShape
import com.fernando.ds.canvas.state.CanvasViewportState

@Composable
fun CanvasSidebar(
    activeColor: Color,
    selectedShapeType: NodeShape,
    onSelectShape: (NodeShape) -> Unit,
    primarySelectedNode: CanvasNode?,
    sidebarValueText: String,
    onValueChange: (String) -> Unit,
    sidebarAnnotationText: String,
    onAnnotationChange: (String) -> Unit,
    viewport: CanvasViewportState,
    canvasWindowTopLeft: Offset,
    onDraggingGhost: (NodeShape?, Offset?) -> Unit,
    onAddSpawnedNode: (CanvasNode) -> Unit
) {
    Card(
        modifier = Modifier
            .width(135.dp)
            .fillMaxHeight()
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 6.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Shapes", fontSize = 11.sp, fontWeight = FontWeight.Bold)

                NodeShape.values().forEach { shape ->
                    var itemWindowPos by remember { mutableStateOf(Offset.Zero) }
                    var currentDragOffset by remember { mutableStateOf(Offset.Zero) }
                    val isShapeSelected = selectedShapeType == shape

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .onGloballyPositioned { coordinates ->
                                itemWindowPos = coordinates.positionInWindow()
                            }
                            .border(
                                width = if (isShapeSelected) 2.5.dp else 0.dp,
                                color = if (isShapeSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .pointerInput(shape) {
                                detectTapGestures(onTap = { onSelectShape(shape) })
                            }
                            .pointerInput(shape, activeColor, sidebarValueText, sidebarAnnotationText, viewport.scale, viewport.offset, canvasWindowTopLeft) {
                                detectDragGestures(
                                    onDragStart = { startOffset ->
                                        currentDragOffset = startOffset
                                        val windowMouse = itemWindowPos + startOffset
                                        val canvasMouse = windowMouse - canvasWindowTopLeft
                                        onDraggingGhost(shape, canvasMouse)
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        currentDragOffset += dragAmount
                                        val windowMouse = itemWindowPos + currentDragOffset
                                        val canvasMouse = windowMouse - canvasWindowTopLeft
                                        onDraggingGhost(shape, canvasMouse)
                                    },
                                    onDragEnd = {
                                        val windowMouse = itemWindowPos + currentDragOffset
                                        val canvasMouse = windowMouse - canvasWindowTopLeft

                                        if (canvasMouse.x > 0f) {
                                            val worldX = (canvasMouse.x - viewport.offset.x) / viewport.scale
                                            val worldY = (canvasMouse.y - viewport.offset.y) / viewport.scale

                                            onAddSpawnedNode(
                                                CanvasNode(
                                                    value = sidebarValueText,
                                                    annotation = sidebarAnnotationText,
                                                    position = Offset(
                                                        x = worldX.coerceIn(40f, viewport.worldWidth - 40f),
                                                        y = worldY.coerceIn(40f, viewport.worldHeight - 40f)
                                                    ),
                                                    shape = shape,
                                                    color = activeColor
                                                )
                                            )
                                        }
                                        onDraggingGhost(null, null)
                                    },
                                    onDragCancel = {
                                        onDraggingGhost(null, null)
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(36.dp)) {
                            drawNodeShape(shape, activeColor, Color.DarkGray, 2f)
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                HorizontalDivider()
                Text("Node Edit", fontSize = 11.sp, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = sidebarValueText,
                    onValueChange = onValueChange,
                    label = { Text("Value", fontSize = 10.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = sidebarAnnotationText,
                    onValueChange = onAnnotationChange,
                    label = { Text("Annotation", fontSize = 10.sp) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 11.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}