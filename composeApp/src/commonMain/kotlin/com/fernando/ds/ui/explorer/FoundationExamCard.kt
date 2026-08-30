package com.fernando.ds.ui.explorer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fernando.ds.knowledge.DataStructureKnowledge
import com.fernando.ds.knowledge.StructureId

@Composable
fun FoundationExamCard(ds: DataStructureKnowledge) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
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
                    Text("• 2D Jagged Arrays: Allocate row pointers first (sizeof(int*)), then allocate each row (sizeof(int)). Free rows first, then the root pointer.", fontSize = 13.sp)
                    Text("• Realloc: temp = realloc(arr, new_size * sizeof(int)); if (temp != NULL) arr = temp;", fontSize = 13.sp)
                }
                StructureId.STACK, StructureId.QUEUE, StructureId.DEQUE -> {
                    Text("• Linked Implementation: Use double pointers (node** head) or return new head for push/pop.", fontSize = 13.sp)
                    Text("• Array Implementation: Modulo arithmetic ((front + 1) % capacity) for circular queues.", fontSize = 13.sp)
                    Text("• Edge Cases: Check empty before pop; update head and tail on removing last element.", fontSize = 13.sp)
                }
                StructureId.ORDERED_SET, StructureId.PRIORITY_QUEUE -> {
                    Text("• BST Traversal: Always guard root == NULL base case.", fontSize = 13.sp)
                    Text("• BST Deletion: 0 child (free), 1 child (bypass & free), 2 children (replace with inorder predecessor/successor).", fontSize = 13.sp)
                    Text("• Heap Array: Parent (i-1)/2, Left 2*i + 1, Right 2*i + 2. Sift-down O(log n), Build-Heap O(n).", fontSize = 13.sp)
                }
                StructureId.HASH_SET, StructureId.HASH_MAP -> {
                    Text("• Probing: Linear ((h + i) % M), Quadratic ((h + c1*i + c2*i^2) % M).", fontSize = 13.sp)
                    Text("• Chaining: Array of node pointers (struct node** table). Free each chain iteratively.", fontSize = 13.sp)
                }
                else -> {
                    Text("• Check for NULL before dereferencing struct pointers (ptr->field).", fontSize = 13.sp)
                    Text("• Free memory in reverse order of allocation.", fontSize = 13.sp)
                }
            }
        }
    }
}