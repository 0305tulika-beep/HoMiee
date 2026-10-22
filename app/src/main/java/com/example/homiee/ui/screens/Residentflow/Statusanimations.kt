package com.example.homiee.ui.screens.Residentflow

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** 1f = about 1 second total, 2f = about 2 seconds. Change this one number to speed up / slow down. */
private const val TIME_SCALE = 2f

private fun t(ms: Int): Int = (ms * TIME_SCALE).toInt()

/**
 * "Success tick pop" badge used by the Booking Confirmed (tick) and Booking Declined (cross) screens.
 *
 *  0 ms     a tiny dot at the centre turns into a tiny tick / cross (~15% of final size)   ~100 ms
 *  100 ms   the glyph pops up to ~110% and settles back to 100%, the soft circle fades in   ~250 ms
 *  100 ms+  three thin rings expand from behind it one after another, fading as they grow    ~600 ms each
 *  350 ms+  a few small sparkles flash around the circles and disappear                      ~300 ms
 *
 * Times above are for TIME_SCALE = 1f. TIME_SCALE is 2f, so everything plays once over about 2 seconds and ends on a crisp glyph inside a soft circle.
 */
@Composable
fun AnimatedStatusIcon(
    isSuccess: Boolean,
    accent: Color,          // glyph, rings and sparkles
    soft: Color,            // soft circle behind the glyph
    modifier: Modifier = Modifier
) {
    val glyphMorph = remember { Animatable(0f) }        // dot -> glyph
    val glyphScale = remember { Animatable(0.15f) }     // 15% -> 110% -> 100%
    val discAlpha  = remember { Animatable(0f) }        // soft circle fades in
    val rings      = remember { List(3) { Animatable(0f) } }
    val sparkle    = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // 1. tiny dot turns into a tiny glyph
        glyphMorph.animateTo(1f, tween(durationMillis = t(100), easing = LinearEasing))

        // 2. glyph pops with a slight overshoot, soft circle fades in
        launch {
            glyphScale.animateTo(
                1f,
                keyframes<Float> {
                    durationMillis = t(250)
                    1.10f at t(150) using FastOutSlowInEasing
                }
            )
        }
        launch { discAlpha.animateTo(1f, tween(durationMillis = t(200))) }

        // 3. rings ripple outwards one after another
        rings.forEachIndexed { i, ring ->
            launch {
                delay(t(120).toLong() * i)
                ring.animateTo(1f, tween(durationMillis = t(600), easing = LinearOutSlowInEasing))
            }
        }

        // 4. sparkles flash and fade
        launch {
            delay(t(350).toLong())
            sparkle.animateTo(1f, tween(durationMillis = t(300), easing = LinearEasing))
        }
    }

    Box(
        modifier = modifier.size(230.dp),
        contentAlignment = Alignment.Center
    ) {

        // ── Rings + sparkles (behind everything) ──
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = 55.dp.toPx()                  // radius of the soft circle
            val maxRadius = size.minDimension / 2f

            rings.forEach { ring ->
                val p = ring.value
                if (p > 0f && p < 1f) {
                    val radius = baseRadius + (maxRadius - baseRadius) * p
                    drawCircle(
                        color = accent.copy(alpha = 0.45f * (1f - p)),
                        radius = radius,
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }

            val sp = sparkle.value
            if (sp > 0f && sp < 1f) {
                val alpha = sin(PI * sp).toFloat()          // fades in, then out
                val ringRadius = 80.dp.toPx() + 14.dp.toPx() * sp
                val stroke = 1.8.dp.toPx()

                // angle in degrees, half-size in dp
                val sparks = listOf(
                    20f to 5f, 80f to 4f, 140f to 6f,
                    200f to 4f, 260f to 5f, 320f to 6f
                )
                sparks.forEach { (deg, halfDp) ->
                    val rad = Math.toRadians(deg.toDouble())
                    val c = Offset(
                        center.x + (cos(rad) * ringRadius).toFloat(),
                        center.y + (sin(rad) * ringRadius).toFloat()
                    )
                    val r = halfDp.dp.toPx() * (0.6f + 0.4f * alpha)
                    val color = accent.copy(alpha = alpha)
                    drawLine(color, Offset(c.x - r, c.y), Offset(c.x + r, c.y), stroke, StrokeCap.Round)
                    drawLine(color, Offset(c.x, c.y - r), Offset(c.x, c.y + r), stroke, StrokeCap.Round)
                }
            }
        }

        // ── Soft circle (fades in and pops together with the glyph) ──
        Box(
            modifier = Modifier
                .size(110.dp)
                .graphicsLayer {
                    // follows the glyph's pop: 40% -> 100% (+ the same slight overshoot)
                    val s = 0.4f + 0.6f * ((glyphScale.value - 0.15f) / 0.85f)
                    scaleX = s
                    scaleY = s
                    alpha = discAlpha.value
                }
                .clip(CircleShape)
                .background(soft)
        )

        // ── Glyph: dot -> tick / cross, with the pop scale ──
        Canvas(
            modifier = Modifier
                .size(52.dp)
                .graphicsLayer {
                    scaleX = glyphScale.value
                    scaleY = glyphScale.value
                }
        ) {
            val morph = glyphMorph.value
            val stroke = size.minDimension * 0.10f

            fun pt(x: Float, y: Float) = Offset(x * size.width, y * size.height)

            // the starting dot fades out while the glyph fades in
            if (morph < 1f) {
                drawCircle(
                    color = accent.copy(alpha = 1f - morph),
                    radius = size.minDimension * 0.10f,
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            }

            val glyphColor = accent.copy(alpha = morph)
            if (isSuccess) {
                val a = pt(0.14f, 0.54f)
                val b = pt(0.40f, 0.78f)
                val c = pt(0.86f, 0.24f)
                drawLine(glyphColor, a, b, stroke, StrokeCap.Round)
                drawLine(glyphColor, b, c, stroke, StrokeCap.Round)
            } else {
                drawLine(glyphColor, pt(0.22f, 0.22f), pt(0.78f, 0.78f), stroke, StrokeCap.Round)
                drawLine(glyphColor, pt(0.78f, 0.22f), pt(0.22f, 0.78f), stroke, StrokeCap.Round)
            }
        }
    }
}