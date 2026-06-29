package com.example.homiee.ui.screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar

private val GreenDark    = Color(0xFF0F3D26)
private val TextPrimary  = Color(0xFF1A1A1A)
private val TextMuted    = Color(0xFF555555)

private val TERMS_TEXT = """
1. ACCEPTANCE OF TERMS

By registering and using the HoMiee application ("App"), you agree to be bound by these Terms & Conditions. If you do not agree, please do not use the App.

2. ELIGIBILITY

You must be at least 18 years of age and a registered resident of an eligible housing society or community to use this App as a Resident. HoMiee reserves the right to verify your residential status at any time.

3. ACCOUNT RESPONSIBILITY

You are responsible for maintaining the confidentiality of your login credentials. Any activity that occurs under your account is your responsibility. Notify us immediately at support@homiee.in if you suspect unauthorized access.

4. BOOKING OF HOUSEHOLD SERVICES

HoMiee connects Residents with verified household Helpers for services including but not limited to cleaning, cooking, laundry, and babysitting. HoMiee acts as an intermediary platform and is not the direct employer of any Helper.

5. HELPER VERIFICATION

All Helpers listed on HoMiee undergo background verification before onboarding. However, HoMiee does not guarantee the conduct or performance of any Helper. Residents are encouraged to use the in-app rating and review system after each booking.

6. EMERGENCY SOS

The Emergency SOS feature connects you directly with emergency services (112). HoMiee is not liable for response times or outcomes of emergency calls. This feature is provided solely as a convenience.

7. PAYMENTS & CANCELLATIONS

All service charges are communicated transparently before booking confirmation. Cancellations made less than 2 hours before a scheduled booking may incur a cancellation fee. Refund policies are subject to the specific service terms displayed at checkout.

8. PRIVACY & DATA

HoMiee collects personal data including name, address, and phone number solely for the purpose of facilitating service bookings. Your data will never be sold to third parties. Please refer to our Privacy Policy for full details.

9. PROHIBITED CONDUCT

Users must not misuse the platform for any unlawful purpose, harass or abuse Helpers, provide false information during registration, or attempt to book services outside the App to avoid platform fees.

10. LIMITATION OF LIABILITY

HoMiee shall not be liable for any indirect, incidental, or consequential damages arising from the use of the App or services booked through it, including property damage or personal injury caused by a Helper.

11. MODIFICATIONS

HoMiee reserves the right to update these Terms at any time. Continued use of the App after changes are posted constitutes acceptance of the revised Terms.

12. GOVERNING LAW

These Terms are governed by the laws of India. Any disputes shall be subject to the exclusive jurisdiction of the courts of Lucknow, Uttar Pradesh.

13. CONTACT US

For questions or concerns, please reach out to us at support@homiee.in or call our helpline at 1800-XXX-XXXX (Mon–Sat, 9 AM – 6 PM).
""".trimIndent()

@Composable
fun TermsAndConditionsScreen(
    onAgree: () -> Unit = {},
    onClose: () -> Unit = {}   // back → close app
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    // Backing from T&C should close app (same as pressing back on home)
    val context = LocalContext.current
    BackHandler {
        onClose()
    }

    Box(
        modifier          = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentAlignment  = Alignment.Center
    ) {

        // Dim scrim behind the card
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        )

        // ── Card ──────────────────────────────────────────────────────────
        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(horizontal = 16.dp),
            shape     = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 20.dp, bottomEnd = 20.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // Title
                Box(
                    modifier         = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp, bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = "TERMS & CONDITIONS",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                        textAlign  = TextAlign.Center
                    )
                }

                HorizontalDivider(color = Color(0xFFEEEEEE))

                // Scrollable body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text       = TERMS_TEXT,
                        fontSize   = 13.sp,
                        lineHeight = 20.sp,
                        color      = TextMuted
                    )
                }

                HorizontalDivider(color = Color(0xFFEEEEEE))

                // I AGREE button
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Button(
                        onClick  = onAgree,
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = GreenDark),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text       = "I AGREE",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color.White
                        )
                    }
                }
            }
        }
    }
}