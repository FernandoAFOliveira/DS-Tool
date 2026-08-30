package com.fernando.ds.canvas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fernando.ds.canvas.drawing.ConnectEdgeIconButton
import com.fernando.ds.canvas.drawing.CutEdgeIconButton
import com.fernando.ds.canvas.handler.DataStructureCanvasHandler
import com.fernando.ds.canvas.model.CanvasEdge

@Composable
fun CanvasTopToolbar(
    handlers: List<DataStructureCanvasHandler>,
    selectedHandlerIndex: Int,
    onSelectHandler: (Int) -> Unit,
    moveSubtreeMode: Boolean,
    onToggleSubtreeMode: () -> Unit,
    activeColor: Color,
    onSelectColor: (Color) -> Unit,
    selectedNodeIds: Set<String>,
    edges: List<CanvasEdge>,
    onConnectNodes: () -> Unit,
    onCutEdge: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    scalePercent: Int,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onFitAll: () -> Unit,
    onResetZoom: () -> Unit,
    onClearAll: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val activeHandler = handlers[selectedHandlerIndex]
    val colorPalette = listOf(
        Color(0xFF81C784), Color(0xFFE53935), Color(0xFF263238),
        Color(0xFF42A5F5), Color(0xFFFFA726), Color(0xFFAB47BC),
        Color(0xFF26A69A), Color(0xFFFFEE58)
    )

    val twoSelected = selectedNodeIds.toList()
    val isTwoSelected = twoSelected.size == 2
    val existingEdge = if (isTwoSelected) {
        edges.find { (it.fromNodeId == twoSelected[0] && it.toNodeId == twoSelected[1]) || (it.fromNodeId == twoSelected[1] && it.toNodeId == twoSelected[0]) }
    } else null

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Controls
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilledTonalButton(
                    onClick = onNavigateBack,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Back", fontSize = 12.sp)
                }

                var menuExpanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(
                        onClick = { menuExpanded = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(activeHandler.structureName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        handlers.forEachIndexed { i, h ->
                            DropdownMenuItem(
                                text = { Text(h.structureName) },
                                onClick = {
                                    onSelectHandler(i)
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }

                FilterChip(
                    selected = moveSubtreeMode,
                    onClick = onToggleSubtreeMode,
                    label = { Text(if (moveSubtreeMode) "Subtree: ON" else "Single Node", fontSize = 11.sp) },
                    leadingIcon = { Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp)) }
                )
            }

            // Middle Palette
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                colorPalette.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(color, CircleShape)
                            .border(
                                width = if (activeColor == color) 3.dp else 1.dp,
                                color = if (activeColor == color) MaterialTheme.colorScheme.onSurface else Color.Gray,
                                shape = CircleShape
                            )
                            .clickable { onSelectColor(color) }
                    )
                }
            }

            // Right Actions
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (existingEdge != null) {
                    CutEdgeIconButton(enabled = true, onClick = onCutEdge)
                } else {
                    ConnectEdgeIconButton(enabled = isTwoSelected, onClick = onConnectNodes)
                }

                if (selectedNodeIds.isNotEmpty()) {
                    FilledTonalButton(
                        onClick = onDuplicate,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Clone", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        contentPadding = PaddingValues(horizontal = 6.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }

                // Fit to Screen (Overview of all trees)
                IconButton(onClick = onFitAll, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Home, contentDescription = "Overview", modifier = Modifier.size(18.dp))
                }

                IconButton(onClick = onZoomIn, modifier = Modifier.size(28.dp)) {
                    Text("+", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                IconButton(onClick = onZoomOut, modifier = Modifier.size(28.dp)) {
                    Text("-", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                OutlinedButton(onClick = onResetZoom, contentPadding = PaddingValues(horizontal = 6.dp)) {
                    Text("$scalePercent%", fontSize = 11.sp)
                }

                IconButton(onClick = onClearAll, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Refresh, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}