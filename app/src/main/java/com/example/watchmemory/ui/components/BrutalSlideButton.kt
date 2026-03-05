package com.example.watchmemory.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.watchmemory.ui.theme.LocalBrutalColors
import kotlin.math.roundToInt
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import kotlinx.coroutines.launch

@Composable
fun BrutalSlideButton(
    text: String,
    onSlideComplete: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalBrutalColors.current.accent,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val brutal = LocalBrutalColors.current
    val shape = RoundedCornerShape(12.dp)
    val thumbPadding = 4.dp
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(bottom = brutal.shadowOffset, end = brutal.shadowOffset)
    ) {
        val thumbSize = maxHeight - (thumbPadding * 2)
        val maxWidthPx = with(density) { (maxWidth - thumbSize - (thumbPadding * 2)).toPx() }
        val dragOffset = remember { Animatable(0f) }
        var isCompleted by remember { mutableStateOf(false) }

        val draggableState = rememberDraggableState { delta ->
            if (!isCompleted && enabled && !isLoading) {
                val newOffset = (dragOffset.value + delta).coerceIn(0f, maxWidthPx)
                coroutineScope.launch {
                    dragOffset.snapTo(newOffset)
                }
                
                if (newOffset >= maxWidthPx * 0.95f) {
                    isCompleted = true
                    onSlideComplete()
                }
            }
        }

        val containerAlpha = if (enabled) 1f else 0.5f

        // Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = brutal.shadowOffset, y = brutal.shadowOffset)
                .clip(shape)
                .background(brutal.shadow.copy(alpha = containerAlpha))
        )
        // Background track
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(shape)
                .background(Color.White.copy(alpha = containerAlpha))
                .border(brutal.borderWidth, brutal.border.copy(alpha = containerAlpha), shape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = brutal.border.copy(alpha = 0.5f)
            )
        }
        
        // Thumb
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(thumbPadding)
                .offset { IntOffset(dragOffset.value.roundToInt(), 0) }
                .width(thumbSize)
                .clip(RoundedCornerShape(8.dp))
                .background(if (enabled) backgroundColor else Color.LightGray)
                .border(2.dp, brutal.border.copy(alpha = containerAlpha), RoundedCornerShape(8.dp))
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        if (!isCompleted) {
                            coroutineScope.launch {
                                dragOffset.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow)
                                )
                            }
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = brutal.border,
                    strokeWidth = 3.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Slide to complete",
                    tint = brutal.border.copy(alpha = containerAlpha),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
