package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homiee.R
import com.example.homiee.data.model.HelperRatingDto
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.BookingRepository
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private val GreenPrimary  = Color(0xFF1A5C3A)
private val GreenLight    = Color(0xFFE8F5EE)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val StarColor     = Color(0xFFF4B400)
private val CardBg        = Color.White

// ── UI model + mapping ────────────────────────────────────────────────────────

data class ReviewUi(
    val id: Int,
    val score: Int,
    val feedback: String,
    val timeAgo: String
)

fun HelperRatingDto.toReviewUi() = ReviewUi(
    id = id,
    score = (score ?: 0).coerceIn(0, 5),
    feedback = feedback.orEmpty().trim(),
    timeAgo = timeAgoFrom(created_at)
)

/** "2026-07-12T10:30:00.123Z" -> "2 days ago". Day-level accuracy is plenty for a review list. */
fun timeAgoFrom(iso: String?): String {
    if (iso.isNullOrBlank() || iso.length < 19) return ""
    return try {
        val fmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val then = fmt.parse(iso.substring(0, 19))?.time ?: return ""
        val mins = ((System.currentTimeMillis() - then) / 60_000).coerceAtLeast(0)
        val hours = mins / 60
        val days = hours / 24
        when {
            mins < 1 -> "Just now"
            mins < 60 -> "$mins min ago"
            hours < 24 -> if (hours == 1L) "1 hour ago" else "$hours hours ago"
            days < 7 -> if (days == 1L) "1 day ago" else "$days days ago"
            days < 30 -> "${days / 7} week(s) ago"
            days < 365 -> "${days / 30} month(s) ago"
            else -> "${days / 365} year(s) ago"
        }
    } catch (e: Exception) {
        ""
    }
}

// ── ViewModel ─────────────────────────────────────────────────────────────────

data class ReviewsUiState(
    val isLoading: Boolean = true,
    val average: Float = 0f,
    val count: Int = 0,
    val reviews: List<ReviewUi> = emptyList(),
    val errorMessage: String? = null
)

class HelperReviewsViewModel(
    private val repo: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewsUiState())
    val state: StateFlow<ReviewsUiState> = _state.asStateFlow()

    private var lastId: String = ""

    fun load(helperId: String) {
        lastId = helperId
        val id = helperId.toIntOrNull()
        if (id == null) {
            _state.value = ReviewsUiState(isLoading = false, errorMessage = "Invalid helper.")
            return
        }
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val r = repo.helperDetail(id)) {
                is ApiResult.Success -> {
                    val d = r.data.data
                    _state.value = ReviewsUiState(
                        isLoading = false,
                        average = d?.avg_rating?.toFloatOrNull() ?: 0f,
                        count = d?.rating_count ?: 0,
                        reviews = d?.ratings.orEmpty().map { it.toReviewUi() }
                    )
                }
                is ApiResult.Error ->
                    _state.value = _state.value.copy(isLoading = false, errorMessage = r.message)
            }
        }
    }

    fun retry() = load(lastId)
}

// ── Screen (your existing design, now driven by the API) ──────────────────────

@Composable
fun HelperReviewsScreen(
    helperId: String = "",
    onBack: () -> Unit = {},
    viewModel: HelperReviewsViewModel = viewModel()
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    val state by viewModel.state.collectAsState()
    LaunchedEffect(helperId) { viewModel.load(helperId) }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Header ─────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(GreenLight)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint               = GreenPrimary,
                    modifier           = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text       = "Reviews",
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextPrimary
            )
        }

        HorizontalDivider(color = Color(0xFFF0F0F0))

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }

            state.errorMessage != null -> Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(state.errorMessage ?: "", color = TextSecondary, fontSize = 14.sp, textAlign = TextAlign.Center)
                TextButton(onClick = { viewModel.retry() }) {
                    Text("Retry", color = GreenPrimary, fontWeight = FontWeight.Bold)
                }
            }

            else -> Column(modifier = Modifier.fillMaxSize()) {

                // ── Summary ────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter            = painterResource(R.drawable.star),
                        contentDescription = null,
                        tint               = StarColor,
                        modifier           = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text       = String.format(Locale.US, "%.1f", state.average),
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text     = "based on ${state.count} ${if (state.count == 1) "review" else "reviews"}",
                        fontSize = 13.sp,
                        color    = TextSecondary
                    )
                }

                // ── Reviews list ───────────────────────────────────
                if (state.reviews.isEmpty()) {
                    Text(
                        text      = "No reviews yet",
                        fontSize  = 14.sp,
                        color     = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.fillMaxWidth().padding(top = 32.dp)
                    )
                } else {
                    LazyColumn(
                        modifier            = Modifier.fillMaxSize(),
                        contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.reviews, key = { it.id }) { review ->
                            Card(
                                modifier  = Modifier.fillMaxWidth(),
                                shape     = RoundedCornerShape(14.dp),
                                colors    = CardDefaults.cardColors(containerColor = CardBg),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier              = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment     = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            // The API only returns resident ids, not names
                                            Text(
                                                "Homiee resident",
                                                fontSize   = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color      = TextPrimary
                                            )
                                            StarRow(review.score, 12.dp)
                                        }
                                        Text(review.timeAgo, fontSize = 11.sp, color = TextSecondary)
                                    }
                                    if (review.feedback.isNotBlank()) {
                                        Spacer(Modifier.height(6.dp))
                                        Text(review.feedback, fontSize = 12.sp, color = TextSecondary, lineHeight = 18.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** 5 star icons, the first [score] filled. Also used by the REVIEWS card on the helper profile. */
@Composable
fun StarRow(score: Int, size: Dp = 12.dp) {
    Row {
        repeat(5) { i ->
            Icon(
                painter            = painterResource(R.drawable.star),
                contentDescription = null,
                tint               = if (i < score) StarColor else Color(0xFFDDDDDD),
                modifier           = Modifier.size(size)
            )
        }
    }
}