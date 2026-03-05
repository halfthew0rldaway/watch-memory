package com.example.watchmemory.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.watchmemory.ui.theme.LocalBrutalColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GridBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 30.dp.toPx()
        val color = Color.Black.copy(alpha = 0.05f)
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = color,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1.dp.toPx()
            )
            x += step
        }
        var y = 0f
        while (y < size.height) {
            drawLine(
                color = color,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
            y += step
        }
    }
}

/**
 * Adds a slow, "cartoonish" floating and tiny rotation effect.
 */
@Composable
fun Modifier.brutalFloat(
    delayMillis: Int = 0,
    durationMillis: Int = 2000
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "brutalFloat")
    
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    
    val rotateAnim by infiniteTransition.animateFloat(
        initialValue = -1.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis + 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotate"
    )

    return this
        .graphicsLayer {
            translationY = floatAnim.dp.toPx()
            rotationZ = rotateAnim
        }
}

/**
 * A snappy "pop-in" entry animation using bouncy spring physics.
 */
@Composable
fun Modifier.brutalPopEntry(
    index: Int = 0
): Modifier {
    val animatedScale = remember { Animatable(0f) }
    val animatedSlide = remember { Animatable(50f) }
    
    LaunchedEffect(Unit) {
        delay(index * 60L) // Staggered entry
        launch {
            // Cartoonish overshoot: scale to 1.1 then settle at 1.0
            animatedScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = 0.5f, // More bouncy/overshoot
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            animatedSlide.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.6f,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }
    
    return this.graphicsLayer {
        scaleX = animatedScale.value
        scaleY = animatedScale.value
        translationY = animatedSlide.value.dp.toPx()
        alpha = animatedScale.value.coerceIn(0f, 1f)
    }
}

/**
 * A playful constant jiggle/wobble effect.
 */
@Composable
fun Modifier.brutalWobble(
    enabled: Boolean = true
): Modifier {
    if (!enabled) return this
    val infiniteTransition = rememberInfiniteTransition(label = "wobble")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rot"
    )
    
    return this.graphicsLayer {
        rotationZ = rotation
    }
}

/**
 * A subtle pulse effect for highlighting elements.
 */
@Composable
fun Modifier.brutalPulse(): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

@Composable
fun BrutalToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val brutal = LocalBrutalColors.current
    val shape = RoundedCornerShape(12.dp)
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(shape)
                .background(if (checked) brutal.accent else Color.White)
                .border(2.dp, brutal.border, shape),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = brutal.border,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Black,
            color = brutal.border
        )
    }
}
