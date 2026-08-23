package com.fernando.ds.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.fernando.ds.knowledge.KnowledgeCatalog
import com.fernando.ds.subject.CSubjectProvider
import com.fernando.ds.subject.JavaSubjectProvider
import com.fernando.ds.subject.SubjectId
import com.fernando.ds.subject.SubjectProviderRegistry

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TwoPaneAdvisorApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoPaneAdvisorApp() {
    val catalog = remember { KnowledgeCatalog.getAll() }
    var selectedDs by remember { mutableStateOf(catalog.firstOrNull()) }
    var activeSubjectId by remember { mutableStateOf(SubjectId.C) }

    val registry = remember {
        SubjectProviderRegistry(
            listOf(
                CSubjectProvider(),
                JavaSubjectProvider()
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Structure Advisor") },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = activeSubjectId == SubjectId.C,
                            onClick = { activeSubjectId = SubjectId.C },
                            label = { Text("C") }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FilterChip(
                            selected = activeSubjectId == SubjectId.JAVA,
                            onClick = { activeSubjectId = SubjectId.JAVA },
                            label = { Text("Java") }
                        )
                    }
                }
            )
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Left Master List (35% Width)
            LazyColumn(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(catalog) { ds ->
                    val isSelected = ds.id() == selectedDs?.id()
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedDs = ds }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = ds.displayName(),
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = ds.description(),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2
                            )
                        }
                    }
                }
            }

            VerticalDivider()

            // Right Detail Pane (65% Width)
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                selectedDs?.let { ds ->
                    val provider = registry.get(activeSubjectId)
                    val contentOpt = provider.getEducationalContent(ds.id())

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = ds.displayName(),
                            style = MaterialTheme.typography.headlineMedium
                        )

                        // Concept Card
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Concept Overview",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = ds.description(),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        // Subject Content Card
                        if (contentOpt.isPresent) {
                            val educationalContent = contentOpt.get()

                            // Render Sections
                            for (section in educationalContent.sections()) {
                                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "${provider.displayName()}: ${section.title()}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))

                                        for (para in section.paragraphs()) {
                                            Text(
                                                text = para,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                        }

                                        for (bullet in section.bulletItems()) {
                                            Text(
                                                text = "• $bullet",
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Render Code Examples
                            for (example in educationalContent.codeExamples()) {
                                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = example.title(),
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    color = Color(0xFF1E1E1E),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(12.dp)
                                                .horizontalScroll(rememberScrollState())
                                        ) {
                                            Text(
                                                text = example.source(),
                                                color = Color(0xFFD4D4D4),
                                                fontFamily = FontFamily.Monospace,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "${provider.displayName()} Guidance",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "No implementation notes available for ${provider.displayName()}.",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                } ?: Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Select a data structure from the list.")
                }
            }
        }
    }
}