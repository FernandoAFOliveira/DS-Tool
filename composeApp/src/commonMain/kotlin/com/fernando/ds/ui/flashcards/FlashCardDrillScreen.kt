package com.fernando.ds.ui.flashcards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fernando.ds.application.FlashCardSession
import com.fernando.ds.application.LearningQuestionSource
import com.fernando.ds.subject.SubjectId
import com.fernando.ds.subject.SubjectProviderRegistry

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

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .weight(1f)
                .padding(vertical = 24.dp)
                .clickable {
                    session = FlashCardSession(session.currentStructureId(), !session.answerRevealed())
                },
            colors = CardDefaults.elevatedCardColors(
                containerColor = if (session.answerRevealed()) MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp) else MaterialTheme.colorScheme.primaryContainer
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
                            label = { Text("${provider.displayName()} Representation: ${representationOpt.get().name}") }
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
                    session = FlashCardSession(session.currentStructureId(), !session.answerRevealed())
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