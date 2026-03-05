package com.example.watchmemory.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchmemory.ui.theme.LocalBrutalColors

@Composable
fun BrutalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    pill: Boolean = false // Option for fully rounded search-style
) {
    val brutal = LocalBrutalColors.current
    val shape = if (pill) CircleShape else RoundedCornerShape(16.dp)

    val interactionSource = remember { mutableStateOf(false) } // Placeholder for focus
    // Improved with focus checking
    var isFocused by remember { mutableStateOf(false) }
    val shadowOffsetAnim by animateDpAsState(
        targetValue = if (isFocused) 0.dp else brutal.shadowOffset,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "focus"
    )

    Box(modifier = modifier.padding(end = brutal.shadowOffset, bottom = brutal.shadowOffset)) {
        // Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = brutal.shadowOffset, y = brutal.shadowOffset)
                .clip(shape)
                .background(brutal.shadow)
        )
        // Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = shadowOffsetAnim, y = shadowOffsetAnim)
                .clip(shape)
                .background(brutal.cardBg)
                .border(brutal.borderWidth, brutal.border, shape)
                .padding(horizontal = 20.dp, vertical = if (pill) 14.dp else 18.dp)
        ) {
            if (value.isEmpty()) {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Black,
                    color = brutal.border.copy(alpha = 0.3f),
                    letterSpacing = 1.sp
                )
            }
            
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    if (maxLength == null || newValue.length <= maxLength) {
                        onValueChange(newValue)
                    }
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = brutal.border,
                    fontWeight = FontWeight.Black
                ),
                singleLine = singleLine,
                keyboardOptions = keyboardOptions,
                cursorBrush = SolidColor(brutal.border),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused }
            )
        }
    }
}
