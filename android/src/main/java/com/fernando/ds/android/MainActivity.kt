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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fernando.ds.application.FlashCardSession
import com.fernando.ds.application.LearningQuestionSource
import com.fernando.ds.knowledge.DataStructureKnowledge
import com.fernando.ds.knowledge.KnowledgeCatalog
import com.fernando.ds.knowledge.StructureId
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
                    MainAppScaffold()
                }
            }
        }
    }
}

enum class AppTab {
    EXPLORER,
    FLASHCARDS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold() {
    var currentTab by remember { mutableStateOf(AppTab.EXPLORER) }
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
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "DS Advisor",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 24.dp)
                        )
                        TabRow(
                            selectedTabIndex = currentTab.ordinal,
                            modifier = Modifier.width(320.dp),
                            containerColor = Color.Transparent
                        ) {
                            Tab(
                                selected = currentTab == AppTab.EXPLORER,
                                onClick = { currentTab = AppTab.EXPLORER },
                                text = { Text("Explorer") }
                            )
                            Tab(
                                selected = currentTab == AppTab.FLASHCARDS,
                                onClick = { currentTab = AppTab.FLASHCARDS },
                                text = { Text("Drill Flashcards") }
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
                AppTab.EXPLORER -> TwoPaneExplorer(registry, activeSubjectId)
                AppTab.FLASHCARDS -> FlashCardDrillScreen(registry, activeSubjectId)
            }
        }
    }
}

@Composable
fun TwoPaneExplorer(registry: SubjectProviderRegistry, activeSubjectId: SubjectId) {
    val catalog = remember { KnowledgeCatalog.getAll() }
    var selectedDs by remember { mutableStateOf(catalog.firstOrNull()) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left Master List
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

        // Right Detail Pane
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

                    // Foundation Exam High-Yield Summary Card (When C is Active)
                    if (activeSubjectId == SubjectId.C) {
                        FoundationExamCExamCard(ds)
                    }

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

                    // Educational Sections
                    if (contentOpt.isPresent) {
                        val educationalContent = contentOpt.get()

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

                        // Code Examples
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
                    }
                }
            } ?: Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Select a data structure from the left pane.")
            }
        }
    }
}

@Composable
fun FoundationExamCExamCard(ds: DataStructureKnowledge) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "COT3960 Foundation Exam Focus (C / CS1)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (ds.id()) {
                StructureId.DYNAMIC_ARRAY -> {
                    Text("• DMA Formula: ptr = malloc(sizeof(Type) * capacity);", fontFamily = FontFamily.Monospace, fontSize = 13.sp)
                    Text("• 2D Jagged Arrays: Allocate row pointers first (sizeof(int*)), then allocate each row (sizeof(int)). Free each row first, then free the root pointer.", fontSize = 13.sp)
                    Text("• Realloc: temp = realloc(arr, new_size * sizeof(int)); if (temp != NULL) arr = temp; to prevent leak on failure.", fontSize = 13.sp)
                }
                StructureId.STACK, StructureId.QUEUE, StructureId.DEQUE -> {
                    Text("• Linked Implementation: Use double pointers (node** head) or return new head for push/pop.", fontSize = 13.sp)
                    Text("• Array Implementation: Track top index for stack; use modulo arithmetic ((front + 1) % capacity) for circular queues.", fontSize = 13.sp)
                    Text("• Exam Edge Cases: Empty check before pop/peek; update both head and tail when dequeuing last element.", fontSize = 13.sp)
                }
                StructureId.ORDERED_SET, StructureId.PRIORITY_QUEUE -> {
                    Text("• Tree/Heap Traversal: Always guard root == NULL base case in recursive functions.", fontSize = 13.sp)
                    Text("• Deletion in BST: 0 children (free), 1 child (bypass & free), 2 children (find max of left subtree or min of right subtree, copy value, recursively delete that node).", fontSize = 13.sp)
                    Text("• Binary Heap in Array: Parent at (i-1)/2, Left child at 2*i + 1, Right child at 2*i + 2. Sift-down takes O(log n), Build-Heap takes O(n).", fontSize = 13.sp)
                }
                StructureId.HASH_SET, StructureId.HASH_MAP -> {
                    Text("• Probing collision formulas: Linear (h(k, i) = (h'(k) + i) % M), Quadratic (h(k, i) = (h'(k) + c1*i + c2*i^2) % M).", fontSize = 13.sp)
                    Text("• Chaining: Array of linked list head pointers (struct node** table). Free each chain iteratively on table destruction.", fontSize = 13.sp)
                }
                else -> {
                    Text("• Always check for NULL before dereferencing struct pointers (ptr->field).", fontSize = 13.sp)
                    Text("• Free memory in reverse order of allocation (leaves/elements first, parent container last).", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun FlashCardDrillScreen(registry: SubjectProviderRegistry, activeSubjectId: SubjectId) {
    val questionSource = remember { LearningQuestionSource() }
    val questions = remember { questionSource.all }
    var currentIndex by remember { mutableIntStateOf(0) }
    var session by remember {
        mutableStateOf(
            FlashCardSession(questions.first().structureId(), false)
        )
    }

    val currentQuestion = questions[currentIndex]
    val provider = registry.get(activeSubjectId)
    val representationOpt = provider.getRepresentation(currentQuestion.structureId())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Progress & Top Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Card ${currentIndex + 1} of ${questions.size}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            AssistChip(
                onClick = {
                    currentIndex = 0
                    session = FlashCardSession(questions.first().structureId(), false)
                },
                label = { Text("Restart Deck") },
                leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = null) }
            )
        }

        // Center Flashcard
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .weight(1f)
                .padding(vertical = 24.dp)
                .clickable {
                    session = FlashCardSession(
                        session.currentStructureId(),
                        !session.answerRevealed()
                    )
                },
            colors = CardDefaults.elevatedCardColors(
                containerColor = if (session.answerRevealed()) {
                    MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (session.answerRevealed()) Icons.Default.Check else Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentQuestion.prompt(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                if (session.answerRevealed()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(0.6f))
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = currentQuestion.answer(),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )

                    if (representationOpt.isPresent) {
                        Spacer(modifier = Modifier.height(18.dp))
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    "${provider.displayName()} Representation: ${representationOpt.get().name}"
                                )
                            }
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tap card to reveal answer",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Bottom Controls
        Row(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    if (currentIndex > 0) {
                        currentIndex--
                        session = FlashCardSession(questions[currentIndex].structureId(), false)
                    }
                },
                enabled = currentIndex > 0
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Previous")
            }

            Button(
                onClick = {
                    session = FlashCardSession(
                        session.currentStructureId(),
                        !session.answerRevealed()
                    )
                }
            ) {
                Text(if (session.answerRevealed()) "Hide Answer" else "Reveal Answer")
            }

            Button(
                onClick = {
                    if (currentIndex < questions.size - 1) {
                        currentIndex++
                        session = FlashCardSession(questions[currentIndex].structureId(), false)
                    }
                },
                enabled = currentIndex < questions.size - 1
            ) {
                Text("Next")
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}