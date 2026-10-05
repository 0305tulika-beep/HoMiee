package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.homiee.ui.theme.CardBg
import com.example.homiee.ui.theme.ErrorRed
import com.example.homiee.ui.theme.GreenMid
import com.example.homiee.ui.theme.TextMuted
import com.example.homiee.ui.theme.TextPrimary

@Composable
fun CancelBookingDialog(
    isCancelling: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        // Ignore outside taps / back while the request is in flight
        onDismissRequest = { if (!isCancelling) onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = !isCancelling,
            dismissOnClickOutside = !isCancelling
        ),
        containerColor = CardBg,
        titleContentColor = TextPrimary,
        textContentColor = TextMuted,
        title = {
            Text(
                text = "Cancel booking?",
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text("Are you sure you want to cancel this booking? This can't be undone.")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isCancelling
            ) {
                if (isCancelling) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = ErrorRed
                    )
                } else {
                    Text(text = "Yes, cancel", color = ErrorRed)
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isCancelling
            ) {
                Text(text = "Keep booking", color = GreenMid)
            }
        }
    )
}