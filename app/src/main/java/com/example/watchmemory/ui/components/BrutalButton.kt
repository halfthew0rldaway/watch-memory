package com.example.watchmemory.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchmemory.ui.theme.LocalBrutalColors

@Composable
fun BrutalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    icon: (@Composable () -> Unit)? = null
) {
    val brutal = LocalBrutalColors.current
    val shape = CircleShape // Pill shape as seen in "Done" button
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val offsetAnimation by animateDpAsState(
        targetValue = if (isPressed) 0.dp else brutal.shadowOffset,
        animationSpec = tween(100),
        label = "press"
    )

    Box(modifier = modifier.padding(bottom = brutal.shadowOffset, end = brutal.shadowOffset)) {
        // Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = brutal.shadowOffset, y = brutal.shadowOffset)
                .clip(shape)
                .background(brutal.shadow)
        )
        // Content
        Box(
            modifier = Modifier
                .offset(x = offsetAnimation, y = offsetAnimation)
                .clip(shape)
                .background(if (enabled) backgroundColor else Color(0xFFCCCCCC))
                .border(brutal.borderWidth, brutal.border, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                )
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (enabled) brutal.border else Color(0xFF666666),
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun BrutalSmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    val brutal = LocalBrutalColors.current
    val shape = RoundedCornerShape(8.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val shadowOffset = 3.dp
    val offsetAnimation by animateDpAsState(
        targetValue = if (isPressed) 0.dp else shadowOffset,
        animationSpec = tween(80),
        label = "press"
    )

    Box(modifier = modifier.padding(bottom = shadowOffset, end = shadowOffset)) {
        // Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .clip(shape)
                .background(brutal.shadow)
        )
        // Content
        Box(
            modifier = Modifier
                .offset(x = offsetAnimation, y = offsetAnimation)
                .clip(shape)
                .background(color)
                .border(2.dp, brutal.border, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                color = brutal.border,
                fontSize = 10.sp
            )
        }
    }
}
