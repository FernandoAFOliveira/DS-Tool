package com.fernando.ds.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fernando.ds.canvas.TreeCanvasScreen
import com.fernando.ds.subject.CSubjectProvider
import com.fernando.ds.subject.JavaSubjectProvider
import com.fernando.ds.subject.SubjectId
import com.fernando.ds.subject.SubjectProviderRegistry
import com.fernando.ds.ui.explorer.TwoPaneExplorer
import com.fernando.ds.ui.flashcards.FlashCardDrillScreen

enum class AppTab {
    CANVAS, EXPLORER, FLASHCARDS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    var currentTab by remember { mutableStateOf(AppTab.CANVAS) }
    var activeSubjectId by remember { mutableStateOf(SubjectId.C) }

    val registry = remember {
        SubjectProviderRegistry(listOf(CSubjectProvider(), JavaSubjectProvider()))
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (currentTab == AppTab.CANVAS) {
                // Fullscreen Tree Canvas without top menu crowding
                TreeCanvasScreen(onNavigateBack = { currentTab = AppTab.EXPLORER })
            } else {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "DS Advisor",
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(end = 24.dp)
                                    )
                                    TabRow(
                                        selectedTabIndex = currentTab.ordinal,
                                        modifier = Modifier.width(440.dp),
                                        containerColor = Color.Transparent
                                    ) {
                                        Tab(
                                            selected = currentTab == AppTab.CANVAS,
                                            onClick = { currentTab = AppTab.CANVAS },
                                            text = { Text("Tree Canvas") }
                                        )
                                        Tab(
                                            selected = currentTab == AppTab.EXPLORER,
                                            onClick = { currentTab = AppTab.EXPLORER },
                                            text = { Text("Explorer") }
                                        )
                                        Tab(
                                            selected = currentTab == AppTab.FLASHCARDS,
                                            onClick = { currentTab = AppTab.FLASHCARDS },
                                            text = { Text("Flashcards") }
                                        )
                                    }
                                }
                            },
                            actions = {
                                Row(
                                    modifier = Modifier.padding(end = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    FilterChip(
                                        selected = activeSubjectId == SubjectId.C,
                                        onClick = { activeSubjectId = SubjectId.C },
                                        label = { Text("C (FE / CS1)") }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    FilterChip(
                                        selected = activeSubjectId == SubjectId.JAVA,
                                        onClick = { activeSubjectId = SubjectId.JAVA },
                                        label = { Text("Java (OOP)") }
                                    )
                                }
                            }
                        )
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        when (currentTab) {
                            AppTab.CANVAS -> TreeCanvasScreen(onNavigateBack = { currentTab = AppTab.EXPLORER })
                            AppTab.EXPLORER -> TwoPaneExplorer(registry, activeSubjectId)
                            AppTab.FLASHCARDS -> FlashCardDrillScreen(registry, activeSubjectId)
                        }
                    }
                }
            }
        }
    }
}