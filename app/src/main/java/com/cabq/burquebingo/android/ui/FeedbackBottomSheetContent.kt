package com.cabq.burquebingo.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cabq.burquebingo.android.BuildConfig
import com.cabq.burquebingo.android.config.FeedbackConfig
import com.cabq.burquebingo.android.security.openFeedbackMailto
import com.cabq.burquebingo.android.security.openTrustedCityWebUrl
import com.cabq.burquebingo.android.theme.CabqColors

enum class FeedbackKind { Bug, Suggestion }

@Composable
fun FeedbackBottomSheetContent(
    onDismiss: () -> Unit,
    onMailResult: (Boolean) -> Unit,
    on311Result: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp, bottom = 24.dp),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
                .height(4.dp)
                .fillMaxWidth(0.12f)
                .background(
                    CabqColors.InkMuted.copy(alpha = 0.25f),
                    RoundedCornerShape(2.dp),
                ),
        )
        Text(
            text = "Beta feedback",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Help us improve Burque Bingo. Your message opens in the Mail app — nothing is sent automatically.",
            style = MaterialTheme.typography.bodySmall,
            color = CabqColors.InkMuted,
            lineHeight = MaterialTheme.typography.bodySmall.lineHeight * 1.4f,
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                onDismiss()
                val ok = openFeedbackMailto(
                    context,
                    subject = feedbackSubject(FeedbackKind.Bug),
                    body = feedbackBody(FeedbackKind.Bug),
                )
                onMailResult(ok)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Outlined.BugReport, contentDescription = null)
                Text("Report a bug")
            }
        }
        Spacer(Modifier.height(10.dp))
        FilledTonalButton(
            onClick = {
                onDismiss()
                val ok = openFeedbackMailto(
                    context,
                    subject = feedbackSubject(FeedbackKind.Suggestion),
                    body = feedbackBody(FeedbackKind.Suggestion),
                )
                onMailResult(ok)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Outlined.Lightbulb, contentDescription = null)
                Text("Suggest an improvement")
            }
        }
        Spacer(Modifier.height(16.dp))
        TextButton(
            onClick = {
                onDismiss()
                val ok = openTrustedCityWebUrl(context, FeedbackConfig.CITY_311_WEB_URL)
                on311Result(ok)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Outlined.SupportAgent, contentDescription = null)
                Text("City services & ABQ311")
            }
        }
    }
}

private fun feedbackSubject(kind: FeedbackKind): String {
    val prefix = when (kind) {
        FeedbackKind.Bug -> "Bug report"
        FeedbackKind.Suggestion -> "Suggestion"
    }
    return "[Burque Bingo Beta] $prefix (v${BuildConfig.VERSION_NAME}+${BuildConfig.VERSION_CODE})"
}

private fun feedbackBody(kind: FeedbackKind): String {
    val prefix = when (kind) {
        FeedbackKind.Bug -> "Bug report"
        FeedbackKind.Suggestion -> "Suggestion"
    }
    return """
$prefix — Burque Bingo

Please describe what happened or what you would like to see:



---
App: Burque Bingo ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})
Package: ${BuildConfig.APPLICATION_ID}
""".trimIndent()
}
