package com.example.homiee.ui.screens.Residentflow

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.viewmodel.BookingViewModel
import kotlinx.coroutines.delay

private val GreenPrimary = GreenDark

/** TODO: put your real support address here. "Report an issue" opens an email to it. */
private const val SUPPORT_EMAIL = "support@homiee.app"

private enum class JobStage { UPCOMING, IN_PROGRESS, FINISHED }
private enum class StepState { DONE, ACTIVE, PENDING }

/**
 * Live job screen for one booking. Everything is derived from the real booking:
 *  - before the start time: countdown to the start
 *  - during the booking window: elapsed timer + time left
 *  - after the end time: finished, with a "Rate" button
 */
@Composable
fun ActivityScreen(
    bookingId: String,
    helperName: String,
    viewModel: BookingViewModel,
    onBack: () -> Unit = {},
    onRate: () -> Unit = {}
) {
    val bookings by viewModel.bookings.collectAsState()
    val detail by viewModel.detail.collectAsState()

    val booking = bookings.find { it.id == bookingId }
        ?: detail.booking?.takeIf { it.id == bookingId }

    // Fetch the latest copy of this booking (also keeps the timing fields fresh)
    LaunchedEffect(bookingId) { viewModel.loadDetail(bookingId) }

    ActivityContent(
        booking = booking,
        helperName = helperName,
        isLoading = detail.isLoading && booking == null,
        errorMessage = detail.errorMessage,
        onBack = onBack,
        onRate = onRate,
        onJobEnded = { viewModel.refresh() }   // lets the server mark it completed too
    )
}

@Composable
internal fun ActivityContent(
    booking: BookingItem?,
    helperName: String,
    isLoading: Boolean,
    errorMessage: String?,
    onBack: () -> Unit,
    onRate: () -> Unit,
    onJobEnded: () -> Unit
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    val context = LocalContext.current

    // ── Clock: ticks once a second ──
    var now by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            now = System.currentTimeMillis()
        }
    }

    val start = booking?.startMillis ?: 0L
    val end = booking?.endMillis ?: 0L
    val hasTimes = start > 0L && end > start

    val finished = booking?.rawStatus.equals("completed", ignoreCase = true) ||
            (hasTimes && now >= end)

    val stage = when {
        booking == null                  -> JobStage.UPCOMING
        finished                         -> JobStage.FINISHED
        hasTimes && now >= start         -> JobStage.IN_PROGRESS
        else                             -> JobStage.UPCOMING
    }

    // Once the time is up, refresh so the booking is marked completed on the server
    LaunchedEffect(finished) { if (finished && booking != null) onJobEnded() }

    val displaySeconds: Long = when {
        !hasTimes                  -> 0L
        stage == JobStage.UPCOMING -> ((start - now) / 1000).coerceAtLeast(0L)
        stage == JobStage.IN_PROGRESS -> ((now - start) / 1000).coerceAtLeast(0L)
        else                       -> (end - start) / 1000
    }
    val timeString = String.format(
        "%02d:%02d:%02d",
        displaySeconds / 3600,
        (displaySeconds % 3600) / 60,
        displaySeconds % 60
    )

    val timerLabel = when (stage) {
        JobStage.UPCOMING    -> "Starts in"
        JobStage.IN_PROGRESS -> "Time elapsed"
        JobStage.FINISHED    -> "Total time"
    }

    val minutesLeft = if (hasTimes) ((end - now) / 60000L).coerceAtLeast(0L) else 0L
    val subLine = when (stage) {
        JobStage.UPCOMING    -> "Scheduled: ${booking?.bookingTime.orEmpty()} – ${booking?.endTime.orEmpty()}"
        JobStage.IN_PROGRESS -> "Estimated end: ${booking?.endTime.orEmpty()} · " +
                if (minutesLeft <= 0L) "less than a minute left" else "$minutesLeft min left"
        JobStage.FINISHED    -> "Ended at ${booking?.endTime.orEmpty()}"
    }

    val title = when (stage) {
        JobStage.UPCOMING    -> "Upcoming Job"
        JobStage.IN_PROGRESS -> "Active Job"
        JobStage.FINISHED    -> "Job Completed"
    }

    val steps = when (stage) {
        JobStage.UPCOMING    -> listOf(StepState.ACTIVE, StepState.PENDING, StepState.PENDING)
        JobStage.IN_PROGRESS -> listOf(StepState.DONE, StepState.ACTIVE, StepState.PENDING)
        JobStage.FINISHED    -> listOf(StepState.DONE, StepState.DONE, StepState.DONE)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {

        // ── Green header ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(GreenPrimary)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint               = Color.White,
                        modifier           = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text       = title,
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                    Text(
                        text     = booking?.helperName?.takeIf { it.isNotBlank() } ?: helperName,
                        fontSize = 13.sp,
                        color    = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // ── Loading / not found ──
        if (booking == null) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = GreenPrimary)
                } else {
                    Text(
                        text     = errorMessage ?: "Booking not found.",
                        fontSize = 14.sp,
                        color    = Color(0xFF7A7A7A),
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
            return@Column
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ── STATUS card ──
            Card(
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = "STATUS",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = GreenPrimary
                    )
                    Spacer(Modifier.height(12.dp))

                    // ── Timer box ──
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(GreenPrimary)
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text     = timerLabel,
                                fontSize = 12.sp,
                                color    = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text       = if (hasTimes) timeString else "--:--:--",
                                fontSize   = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color      = Color.White
                            )
                            Text(
                                text     = subLine,
                                fontSize = 12.sp,
                                color    = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Progress steps ──
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        ProgressStep(label = "Confirmed", state = steps[0])

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .padding(horizontal = 4.dp)
                                .background(if (stage != JobStage.UPCOMING) GreenPrimary else Color(0xFFDDDDDD))
                        )

                        ProgressStep(label = "In Progress", state = steps[1])

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .padding(horizontal = 4.dp)
                                .background(if (stage == JobStage.FINISHED) GreenPrimary else Color(0xFFDDDDDD))
                        )

                        ProgressStep(label = "Completed", state = steps[2])
                    }
                }
            }

            // ── Rate (only once the job is over) ──
            if (stage == JobStage.FINISHED) {
                Button(
                    onClick  = onRate,
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text       = "Rate ${booking.helperName.ifBlank { helperName }}",
                        color      = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 15.sp
                    )
                }
            }

            // ── BOOKING details card ──
            Card(
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = "BOOKING",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = GreenPrimary
                    )
                    Spacer(Modifier.height(6.dp))

                    InfoRow("Booking ID", "#${booking.id}")
                    if (booking.service.isNotBlank()) InfoRow("Service", booking.service)
                    if (booking.bookingDate.isNotBlank()) InfoRow("Date", booking.bookingDate)
                    InfoRow("Time", "${booking.bookingTime} – ${booking.endTime}")
                    if (booking.durationLabel.isNotBlank()) InfoRow("Duration", booking.durationLabel)
                    if (booking.address.isNotBlank()) InfoRow("Address", booking.address)
                    if (booking.totalAmount.isNotBlank()) InfoRow("Amount", "₹${booking.totalAmount}")
                }
            }

            // ── SAFETY card ──
            Card(
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = "SAFETY",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = GreenPrimary
                    )
                    Spacer(Modifier.height(10.dp))

                    SafetyItem(label = "Share live status") {
                        val text = buildString {
                            append("I have ${booking.service.ifBlank { "a service" }} booked with ")
                            append(booking.helperName.ifBlank { helperName })
                            append(" on ${booking.bookingDate}, ${booking.bookingTime} – ${booking.endTime}.")
                            if (booking.address.isNotBlank()) append("\nAddress: ${booking.address}")
                            append("\nBooking ID: #${booking.id}")
                        }
                        val send = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, text)
                        }
                        context.startActivity(Intent.createChooser(send, "Share live status"))
                    }

                    Spacer(Modifier.height(8.dp))

                    SafetyItem(label = "Report an issue") {
                        val mail = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf(SUPPORT_EMAIL))
                            putExtra(Intent.EXTRA_SUBJECT, "Issue with booking #${booking.id}")
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Booking #${booking.id} with ${booking.helperName.ifBlank { helperName }} " +
                                        "(${booking.bookingDate}, ${booking.bookingTime})\n\nPlease describe the issue:\n"
                            )
                        }
                        try {
                            context.startActivity(mail)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            // ── Emergency SOS: opens the dialer with 112 ready to call ──
            Button(
                onClick  = {
                    context.startActivity(Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:112") })
                },
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text       = "Emergency SOS",
                    color      = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ProgressStep(label: String, state: StepState) {
    val filled = state != StepState.PENDING
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (filled) GreenDark else Color(0xFFDDDDDD)),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                StepState.DONE -> Icon(
                    imageVector        = Icons.Default.Check,
                    contentDescription = null,
                    tint               = Color.White,
                    modifier           = Modifier.size(16.dp)
                )
                StepState.ACTIVE -> Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                StepState.PENDING -> {}
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = if (filled) GreenDark else Color(0xFFAAAAAA))
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = Color(0xFF7A7A7A))
        Spacer(Modifier.width(16.dp))
        Text(
            value,
            fontSize   = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color      = Color(0xFF1A1A1A),
            modifier   = Modifier.weight(1f, fill = false)
        )
    }
}

@Composable
private fun SafetyItem(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF7F7F7))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Text(label, fontSize = 14.sp, color = Color(0xFF1A1A1A))
    }
}