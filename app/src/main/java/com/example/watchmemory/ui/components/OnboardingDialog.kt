package com.example.watchmemory.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.watchmemory.ui.theme.BrutalGreen
import com.example.watchmemory.ui.theme.LocalBrutalColors

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun OnboardingDialog(
    onComplete: (name: String, title: String) -> Unit
) {
    val brutal = LocalBrutalColors.current
    val shape = RoundedCornerShape(20.dp)
    
    var name by remember { mutableStateOf("") }
    var selectedTitle by remember { mutableStateOf("WATCHER") }
    
    val availableTitles = listOf(
        "WATCHER",
        "MASTER COLLECTER",
        "SERIES BINGER",
        "CINEMA LOVER",
        "BRUTAL TRACKER",
        "VOID OBSERVER"
    )

    Dialog(
        onDismissRequest = { /* Cannot be dismissed by tapping outside */ },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = brutal.shadowOffset, end = brutal.shadowOffset)
        ) {
            // Shadow
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = brutal.shadowOffset, y = brutal.shadowOffset)
                    .clip(shape)
                    .background(brutal.shadow)
            )
            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(Color.White)
                    .border(brutal.borderWidth, brutal.border, shape)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrutalGreen)
                        .drawBehind {
                            val strokeWidth = 3.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = strokeWidth
                            )
                        }
                        .padding(16.dp)
                ) {
                    Text(
                        text = "NEW USER DETECTED",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = brutal.border
                    )
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Initialize your identity before archiving memories.",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = brutal.border.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    BrutalTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "ENTER YOUR NAME",
                        pill = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "SELECT YOUR TITLE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = brutal.border.copy(alpha = 0.5f),
                        letterSpacing = 1.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableTitles.forEach { title ->
                            val isSelected = selectedTitle == title
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) brutal.accent else Color.White)
                                    .border(1.5.dp, brutal.border, RoundedCornerShape(8.dp))
                                    .clickable { selectedTitle = title }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = brutal.border,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    BrutalSlideButton(
                        text = "SLIDE TO INITIALIZE",
                        onSlideComplete = {
                            val finalName = if (name.isBlank()) "ME" else name
                            onComplete(finalName, selectedTitle)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = brutal.accent
                    )
                }
            }
        }
    }
}
