package com.example.watchmemory.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchmemory.ui.components.*
import com.example.watchmemory.ui.theme.BrutalGreen
import com.example.watchmemory.ui.theme.BrutalPurple
import com.example.watchmemory.ui.theme.BrutalYellow
import com.example.watchmemory.ui.theme.LocalBrutalColors
import com.example.watchmemory.viewmodel.HomeUiState
import com.example.watchmemory.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onEditShow: (Long) -> Unit,
    onAddShow: () -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val brutal = LocalBrutalColors.current
    var showToDelete by remember { mutableStateOf<com.example.watchmemory.data.ShowEntity?>(null) }

    if (showToDelete != null) {
        BrutalDialog(
            title = "DELETE SHOW?",
            onConfirm = {
                showToDelete?.let { viewModel.deleteShow(it) }
                showToDelete = null
            },
            onDismiss = { showToDelete = null }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GridBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            HeaderSection(
                userName = uiState.userName,
                onProfileClick = onProfileClick,
                modifier = Modifier.brutalPopEntry(0)
            )

            Spacer(modifier = Modifier.height(24.dp))

            DashboardSection(uiState)

            Spacer(modifier = Modifier.height(24.dp))

            SearchSection(
                query = uiState.searchQuery,
                onQueryChange = viewModel::updateSearchQuery
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.shows.isEmpty() && uiState.searchQuery.isBlank()) {
                EmptyStateSection(onAddShow)
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(uiState.shows, key = { _, it -> it.id }) { index, show ->
                        WatchCard(
                            show = show,
                            onClick = { onEditShow(show.id) },
                            onIncrement = { viewModel.incrementEpisode(show) },
                            onLongPress = { showToDelete = show },
                            modifier = Modifier.brutalPopEntry(index + 3)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(userName: String, onProfileClick: () -> Unit, modifier: Modifier = Modifier) {
    val brutal = LocalBrutalColors.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "YOUR FEED",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = brutal.border.copy(alpha = 0.5f)
            )
            Text(
                text = userName.uppercase(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = brutal.border
            )
        }
        
        Box(
            modifier = Modifier
                .brutalFloat(durationMillis = 1800)
                .size(56.dp)
                .clip(CircleShape)
                .background(BrutalGreen)
                .border(3.dp, brutal.border, CircleShape)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
             Text(
                text = if (userName.length >= 2) userName.take(2).uppercase() else userName.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = brutal.border
             )
        }
    }
}

@Composable
fun DashboardSection(state: HomeUiState) {
    val brutal = LocalBrutalColors.current
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(modifier = Modifier.weight(1f).brutalPopEntry(1).brutalFloat(durationMillis = 2200)) {
            BrutalContainer(title = "EPISODES", backgroundColor = BrutalYellow) {
                Text(
                    text = state.totalEpisodesWatched.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = brutal.border
                )
            }
        }
        Box(modifier = Modifier.weight(1f).brutalPopEntry(2).brutalFloat(durationMillis = 2500, delayMillis = 400)) {
            BrutalContainer(title = "ANIME", backgroundColor = BrutalPurple) {
                Text(
                    text = state.animeCount.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = brutal.border
                )
            }
        }
    }
}

@Composable
fun SearchSection(query: String, onQueryChange: (String) -> Unit) {
    BrutalTextField(
        value = query,
        onValueChange = onQueryChange,
        label = "SEARCH YOUR SHOWS...",
        pill = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun EmptyStateSection(onAddShow: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "NO SHOWS YET",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = LocalBrutalColors.current.border.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        BrutalButton(
            text = "START TRACKING",
            onClick = onAddShow,
            backgroundColor = BrutalYellow
        )
    }
}
