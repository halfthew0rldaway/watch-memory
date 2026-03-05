package com.example.watchmemory.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.watchmemory.ui.theme.BrutalPink
import com.example.watchmemory.ui.theme.BrutalYellow
import com.example.watchmemory.ui.theme.LocalBrutalColors

@Composable
fun BrutalDialog(
    title: String,
    message: String = "This action cannot be undone.",
    confirmText: String = "YES, DELETE",
    dismissText: String = "CANCEL",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val brutal = LocalBrutalColors.current
    val shape = RoundedCornerShape(20.dp)

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = brutal.shadowOffset, end = brutal.shadowOffset)
        ) {
            // Shadow
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = brutal.shadowOffset, y = brutal.shadowOffset)
                    .clip(shape)
                    .background(brutal.shadow)
            )
            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(Color.White)
                    .border(brutal.borderWidth, brutal.border, shape)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrutalPink)
                        .drawBehind {
                            val strokeWidth = 3.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = strokeWidth
                            )
                        }
                        .padding(16.dp)
                ) {
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = brutal.border
                    )
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = brutal.border.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BrutalButton(
                            text = dismissText,
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            backgroundColor = Color.White
                        )
                        BrutalButton(
                            text = confirmText,
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f),
                            backgroundColor = BrutalYellow
                        )
                    }
                }
            }
        }
    }
}
