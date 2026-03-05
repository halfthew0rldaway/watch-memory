package com.example.watchmemory.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.LocalMovies
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchmemory.data.ShowEntity
import com.example.watchmemory.ui.theme.BrutalPalette
import com.example.watchmemory.ui.theme.LocalBrutalColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WatchCard(
    show: ShowEntity,
    onClick: () -> Unit,
    onIncrement: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val brutal = LocalBrutalColors.current
    val shape = RoundedCornerShape(20.dp)
    val cardColor = BrutalPalette[kotlin.math.abs(show.id.toInt()) % BrutalPalette.size]

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val offsetAnim by animateDpAsState(
        targetValue = if (isPressed) brutal.shadowOffset else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "press"
    )

    val tiltAnim by animateFloatAsState(
        targetValue = if (isPressed) -2f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "tilt"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = brutal.shadowOffset, end = brutal.shadowOffset)
            .graphicsLayer {
                rotationZ = tiltAnim
            }
    ) {
        // Shadow (Always static)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = brutal.shadowOffset, y = brutal.shadowOffset)
                .clip(shape)
                .background(brutal.shadow)
        )
        
        // Card Content (Animating offset)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = offsetAnim, y = offsetAnim)
                .clip(shape)
                .background(Color.White)
                .border(brutal.borderWidth, brutal.border, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null, 
                    onClick = onClick
                )
        ) {
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor)
                    .drawBehind {
                        val strokeWidth = brutal.borderWidth.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon in a Circle
                val categoryIcon = when(show.category) {
                    "Movie" -> Icons.Outlined.LocalMovies
                    "Anime" -> Icons.Outlined.AutoAwesome
                    else -> Icons.Outlined.Tv
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, brutal.border, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(categoryIcon, contentDescription = null, tint = brutal.border, modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = show.title.uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = brutal.border,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = show.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = brutal.border
                    )
                }
            }

            // Body Section
            Column(modifier = Modifier.padding(16.dp)) {
                val isMovie = show.category == "Movie"
                val hasTotal = show.totalEpisodes != null && show.totalEpisodes > 0
                val isCompleted = hasTotal && show.episode >= show.totalEpisodes!!
                val progress = if (isMovie) {
                    if (isCompleted) 1f else if (hasTotal) show.episode.toFloat() / show.totalEpisodes!!.toFloat() else 0f
                } else if (hasTotal) {
                    show.episode.toFloat() / show.totalEpisodes!!.toFloat()
                } else {
                    1f
                }
                
                Text(
                    text = when {
                        isMovie -> if (isCompleted) "STATUS: WATCHED (DONE)" else "MINUTES WATCHED: ${show.episode}${if (hasTotal) "/${show.totalEpisodes}" else ""} MIN"
                        hasTotal -> "PROGRESS: ${(progress * 100).toInt()}%"
                        else -> "EPISODES WATCHED: ${show.episode}"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = brutal.border
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Progress bar
                val animatedProgress by animateFloatAsState(
                    targetValue = progress.coerceIn(0f, 1f),
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "progress"
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(2.dp, brutal.border, RoundedCornerShape(10.dp))
                ) {
                    // Fill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .fillMaxHeight()
                            .background(if (isMovie && isCompleted) brutal.accent else cardColor)
                            .drawBehind {
                                if (!hasTotal && !isMovie) {
                                    // Striped pattern for infinite tracking
                                    val stripeWidth = 10.dp.toPx()
                                    clipRect {
                                        var x = 0f
                                        while (x < size.width + size.height) {
                                            val path = Path().apply {
                                                moveTo(x, 0f)
                                                lineTo(x + stripeWidth, 0f)
                                                lineTo(x, size.height)
                                                lineTo(x - stripeWidth, size.height)
                                                close()
                                            }
                                            drawPath(path, Color.Black.copy(alpha = 0.15f))
                                            x += stripeWidth * 2
                                        }
                                    }
                                }
                                
                                // End Border
                                if (animatedProgress > 0) {
                                    val strokeWidth = 2.dp.toPx()
                                    val xBorder = size.width - strokeWidth / 2
                                    drawLine(
                                        color = Color.Black,
                                        start = Offset(xBorder, 0f),
                                        end = Offset(xBorder, size.height),
                                        strokeWidth = strokeWidth
                                    )
                                }
                            }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "UPDATED ${formatDate(show.lastUpdated).uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = brutal.border.copy(alpha = 0.5f)
                    )

                    // Unified Action Button
                    val buttonText = when {
                        isMovie -> if (isCompleted) "REWATCH" else "MIN+10"
                        isCompleted -> "REWATCH"
                        else -> "EP+1"
                    }
                    
                    BrutalSmallButton(
                        text = buttonText,
                        onClick = onIncrement,
                        color = if (isCompleted) Color.White else cardColor
                    )
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM d", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
