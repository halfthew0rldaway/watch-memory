package com.example.watchmemory.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchmemory.ui.components.*
import com.example.watchmemory.ui.theme.BrutalGreen
import com.example.watchmemory.ui.theme.BrutalPink
import com.example.watchmemory.ui.theme.BrutalYellow
import com.example.watchmemory.ui.theme.LocalBrutalColors
import com.example.watchmemory.viewmodel.ProfileViewModel

@Composable
fun AboutScreen(viewModel: ProfileViewModel) {
    val brutal = LocalBrutalColors.current
    val userName by viewModel.userName.collectAsState()
    val userTitle by viewModel.userTitle.collectAsState()
    var editingName by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(userName) }

    Box(modifier = Modifier.fillMaxSize()) {
        GridBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "USER PROFILE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = brutal.border.copy(alpha = 0.5f)
            )
            Text(
                text = "YOUR SPACE",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = brutal.border
            )

            Spacer(modifier = Modifier.height(24.dp))

            // User Identity Section
            BrutalContainer(title = "IDENTIFICATION", backgroundColor = Color.White, modifier = Modifier.brutalPopEntry(1)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .brutalPulse()
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(BrutalGreen)
                            .border(3.dp, brutal.border, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (userName.length >= 2) userName.take(2).uppercase() else userName.uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = brutal.border
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(20.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        if (editingName) {
                            BrutalTextField(
                                value = tempName,
                                onValueChange = { tempName = it },
                                label = "NAME",
                                pill = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                BrutalSmallButton(text = "SAVE", onClick = { 
                                    viewModel.updateName(tempName)
                                    editingName = false 
                                }, color = BrutalGreen)
                                BrutalSmallButton(text = "CANCEL", onClick = { 
                                    editingName = false 
                                    tempName = userName
                                }, color = Color.White)
                            }
                        } else {
                            Text(
                                text = userName.uppercase(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = brutal.border
                            )
                            Text(
                                text = userTitle.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = brutal.border.copy(alpha = 0.4f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            BrutalSmallButton(text = "EDIT NAME", onClick = { 
                                editingName = true 
                                tempName = userName
                            }, color = BrutalYellow)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title Selection Section
            @OptIn(ExperimentalLayoutApi::class)
            BrutalContainer(title = "CHOOSE YOUR TITLE", backgroundColor = Color.White) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    viewModel.availableTitles.forEach { title ->
                        val isSelected = userTitle == title
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) brutal.accent else Color.White)
                                .border(1.5.dp, brutal.border, RoundedCornerShape(8.dp))
                                .clickable { viewModel.updateTitle(title) }
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
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "APP INFORMATION",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = brutal.border
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App info card
            BrutalContainer(backgroundColor = BrutalPink, modifier = Modifier.brutalPopEntry(2)) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = brutal.border
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Version 2.0.0",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = brutal.border
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The ultimate neobrutalist tracking engine for viewing history.",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = brutal.border,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Features list
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                FeatureItem(
                    brutal = brutal,
                    icon = Icons.Outlined.Tv,
                    title = "TRACKING",
                    text = "Zero friction series management."
                )
                FeatureItem(
                    brutal = brutal,
                    icon = Icons.Outlined.Analytics,
                    title = "ANIMATION",
                    text = "Bouncy spring-physics experience."
                )
                FeatureItem(
                    brutal = brutal,
                    icon = Icons.Outlined.Widgets,
                    title = "WIDGETS",
                    text = "Dynamic home screen integration."
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "@halfthew0rldaway",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Black,
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                textAlign = TextAlign.Center,
                color = brutal.border.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun FeatureItem(
    brutal: com.example.watchmemory.ui.theme.BrutalColors,
    icon: ImageVector,
    title: String,
    text: String
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(brutal.cardBg)
            .border(brutal.borderWidth, brutal.border, shape)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(brutal.accent, RoundedCornerShape(8.dp))
                .border(2.dp, brutal.border, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = brutal.border
            )
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                color = brutal.border.copy(alpha = 0.5f)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = brutal.border
            )
        }
    }
}
