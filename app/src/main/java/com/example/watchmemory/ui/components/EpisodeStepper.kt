package com.example.watchmemory.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchmemory.ui.theme.LocalBrutalColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BrutalEpisodeStepper(
    episode: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val brutal = LocalBrutalColors.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CURRENT EPISODE",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Black,
            color = brutal.border.copy(alpha = 0.5f),
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Minus Button
            StepperButton(
                icon = Icons.Default.Remove,
                onClick = onDecrement,
                backgroundColor = Color.White,
                shadowColor = brutal.shadow,
                borderColor = brutal.border
            )

            // Animated counter
            AnimatedContent(
                targetState = episode,
                transitionSpec = {
                    (scaleIn(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(tween(150)))
                        .togetherWith(scaleOut(tween(100)) + fadeOut(tween(100)))
                },
                label = "episode_bounce"
            ) { ep ->
                Box(
                    modifier = Modifier.width(120.dp).height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ep.toString(),
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 56.sp),
                        fontWeight = FontWeight.Black,
                        color = brutal.border,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Plus Button
            var isLongPressing by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            StepperButton(
                icon = Icons.Default.Add,
                onClick = onIncrement,
                onLongPress = {
                    isLongPressing = true
                    scope.launch {
                        while (isLongPressing) {
                            onIncrement()
                            delay(100)
                        }
                    }
                },
                onRelease = { isLongPressing = false },
                backgroundColor = brutal.accent,
                shadowColor = brutal.shadow,
                borderColor = brutal.border
            )
        }
    }
}

@Composable
private fun StepperButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    onLongPress: (() -> Unit)? = null,
    onRelease: (() -> Unit)? = null,
    backgroundColor: Color,
    shadowColor: Color,
    borderColor: Color
) {
    val brutal = LocalBrutalColors.current
    var isPressed by remember { mutableStateOf(false) }
    val shape = CircleShape
    
    val offsetAnimation by animateDpAsState(
        targetValue = if (isPressed) 0.dp else brutal.shadowOffset,
        animationSpec = tween(80),
        label = "btnOffset"
    )

    Box(
        modifier = Modifier
            .size(64.dp)
            .padding(bottom = brutal.shadowOffset, end = brutal.shadowOffset)
    ) {
        // Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = brutal.shadowOffset, y = brutal.shadowOffset)
                .clip(shape)
                .background(shadowColor)
        )
        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = offsetAnimation, y = offsetAnimation)
                .clip(shape)
                .background(backgroundColor)
                .border(brutal.borderWidth, borderColor, shape)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onClick() },
                        onLongPress = { onLongPress?.invoke() },
                        onPress = {
                            isPressed = true
                            try {
                                awaitRelease()
                            } finally {
                                isPressed = false
                                onRelease?.invoke()
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = borderColor
            )
        }
    }
}
