package com.example.watchmemory.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchmemory.ui.components.*
import com.example.watchmemory.ui.theme.LocalBrutalColors
import com.example.watchmemory.viewmodel.EditViewModel

@Composable
fun EditScreen(
    viewModel: EditViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val brutal = LocalBrutalColors.current

    LaunchedEffect(uiState.saveComplete) {
        if (uiState.saveComplete) onNavigateBack()
    }

    val episodeInt = uiState.episode.toIntOrNull() ?: 1
    val isMovie = uiState.category == "Movie"

    Box(modifier = Modifier.fillMaxSize()) {
        GridBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = (if (uiState.isNew) "NEW ${uiState.category}" else "EDIT ${uiState.category}").uppercase(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = brutal.border,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            BrutalContainer(title = "General Information") {
                LabelText("TITLE")
                Spacer(modifier = Modifier.height(8.dp))
                BrutalTextField(
                    value = uiState.title,
                    onValueChange = viewModel::updateTitle,
                    label = "What are you watching?",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                LabelText("CATEGORY")
                Spacer(modifier = Modifier.height(8.dp))
                CategorySelector(
                    selected = uiState.category,
                    onSelect = viewModel::updateCategory,
                    options = listOf("Series", "Anime", "Movie")
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isMovie) {
                // Movie Specific Tracking
                BrutalContainer(title = "Movie Progress") {
                    BrutalToggle(
                        checked = uiState.isWatched,
                        onCheckedChange = viewModel::updateWatched,
                        label = "DONE / WATCHED"
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    if (!uiState.isWatched) {
                        LabelText("CURRENT MINUTE")
                        Spacer(modifier = Modifier.height(8.dp))
                        BrutalTextField(
                            value = uiState.episode,
                            onValueChange = viewModel::updateEpisode,
                            label = "e.g. 45",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        LabelText("TOTAL DURATION (Minutes)")
                        Spacer(modifier = Modifier.height(8.dp))
                        BrutalTextField(
                            value = uiState.totalEpisodes,
                            onValueChange = viewModel::updateTotalEpisodes,
                            label = "e.g. 120",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            } else {
                // Series / Anime Tracking
                BrutalContainer(title = "Episode Tracking") {
                    BrutalEpisodeStepper(
                        episode = episodeInt,
                        onIncrement = viewModel::incrementEpisode,
                        onDecrement = viewModel::decrementEpisode
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    LabelText("TOTAL EPISODES")
                    Spacer(modifier = Modifier.height(8.dp))
                    BrutalTextField(
                        value = uiState.totalEpisodes,
                        onValueChange = viewModel::updateTotalEpisodes,
                        label = "e.g. 12",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            BrutalContainer(title = "Notes") {
                BrutalTextField(
                    value = uiState.note,
                    onValueChange = viewModel::updateNote,
                    label = "Write reminders here...",
                    modifier = Modifier.fillMaxWidth(),
                    maxLength = 40
                )
                Text(
                    text = "${uiState.note.length}/40",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = brutal.border.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            BrutalSlideButton(
                text = if (uiState.isNew) "SLIDE TO START" else "SLIDE TO UPDATE",
                onSlideComplete = viewModel::saveShow,
                modifier = Modifier.fillMaxWidth().height(72.dp),
                backgroundColor = brutal.accent,
                enabled = uiState.title.isNotBlank() && !uiState.isSaving,
                isLoading = uiState.isSaving
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun LabelText(text: String) {
    val brutal = LocalBrutalColors.current
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Black,
        color = brutal.border,
        letterSpacing = 1.sp
    )
}

@Composable
private fun CategorySelector(
    selected: String,
    onSelect: (String) -> Unit,
    options: List<String>
) {
    val brutal = LocalBrutalColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEach { option ->
            val isSelected = selected == option
            val backgroundColor = if (isSelected) brutal.accent else Color.White
            val shadowOffset = 3.dp
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = shadowOffset, end = shadowOffset)
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .offset(x = shadowOffset, y = shadowOffset)
                        .clip(CircleShape)
                        .background(brutal.shadow)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .border(2.dp, brutal.border, CircleShape)
                        .clickable { onSelect(option) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = brutal.border,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
