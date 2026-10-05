package com.example.homiee.viewmodel

import com.example.homiee.data.model.BookingDto
import com.example.homiee.ui.screens.Residentflow.BookingItem
import com.example.homiee.ui.screens.Residentflow.BookingTab
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Cancelled / rejected bookings don't belong to any tab, so lists skip them. */
fun BookingDto.toBookingItemOrNull(): BookingItem? =
    if (status.lowercase(Locale.ROOT) in setOf("cancelled", "rejected")) null else toBookingItem()

fun BookingDto.toBookingItem(): BookingItem {
    val s = status.lowercase(Locale.ROOT)
    val tab = when {
        s == "completed" -> BookingTab.COMPLETED
        // The backend has no "in progress" status, so a confirmed booking
        // whose time window is happening right now is shown as Active.
        s == "confirmed" && isHappeningNow(bookingDate, startTime, endTime) -> BookingTab.ACTIVE
        else -> BookingTab.UPCOMING
    }
    return BookingItem(
        id                  = id.toString(),
        helperName          = helperName,
        service             = service,
        rating              = 0f,                      // bookings API has no helper rating
        status              = tab,
        isPending           = s == "pending",
        bookingDate         = formatDate(bookingDate),
        bookingTime         = formatTime(startTime),
        helperId            = helperId.toString(),
        endTime             = formatTime(endTime),
        durationLabel       = durationLabel(startTime, endTime),
        totalAmount         = totalAmount,
        specialInstructions = specialInstructions.orEmpty(),
        rawStatus           = s
    )
}

private fun minutesOf(time: String): Int? {
    val p = time.split(":")
    val h = p.getOrNull(0)?.toIntOrNull() ?: return null
    val m = p.getOrNull(1)?.toIntOrNull() ?: 0
    return h * 60 + m
}

/** "10:00:00" -> "10:00 AM" */
private fun formatTime(time: String): String {
    val total = minutesOf(time) ?: return time
    val h = total / 60
    val m = total % 60
    val h12 = when {
        h == 0 -> 12
        h > 12 -> h - 12
        else   -> h
    }
    return String.format(Locale.ENGLISH, "%d:%02d %s", h12, m, if (h >= 12) "PM" else "AM")
}

/** "2026-06-12" -> "Jun 12, 2026" */
private fun formatDate(date: String): String = try {
    val parsed = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
    if (parsed != null) SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(parsed) else date
} catch (e: Exception) {
    date
}

private fun durationLabel(start: String, end: String): String {
    val s = minutesOf(start) ?: return ""
    val e = minutesOf(end) ?: return ""
    val mins = e - s
    if (mins <= 0) return ""
    val h = mins / 60
    val m = mins % 60
    return when {
        m == 0 -> if (h == 1) "1 hour" else "$h hours"
        h == 0 -> "$m min"
        else   -> "${h}h ${m}m"
    }
}

private fun isHappeningNow(date: String, start: String, end: String): Boolean {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().time)
    if (date != today) return false
    val cal = Calendar.getInstance()
    val now = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    val s = minutesOf(start) ?: return false
    val e = minutesOf(end) ?: return false
    return now in s until e
}