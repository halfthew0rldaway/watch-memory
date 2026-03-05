package com.example.watchmemory.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchmemory.ui.theme.LocalBrutalColors

@Composable
fun BrutalContainer(
    title: String? = null,
    backgroundColor: Color = Color.White,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val brutal = LocalBrutalColors.current
    val shape = RoundedCornerShape(16.dp)

    Box(modifier = modifier.padding(bottom = brutal.shadowOffset, end = brutal.shadowOffset)) {
        // Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = brutal.shadowOffset, y = brutal.shadowOffset)
                .clip(shape)
                .background(brutal.shadow)
        )
        // Container content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(backgroundColor)
                .border(brutal.borderWidth, brutal.border, shape)
        ) {
            title?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brutal.accent)
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
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = it.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = brutal.border,
                        letterSpacing = 2.sp
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}
