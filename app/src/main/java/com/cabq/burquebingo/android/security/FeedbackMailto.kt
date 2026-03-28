package com.cabq.burquebingo.android.security

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.cabq.burquebingo.android.config.FeedbackConfig

private val MAILTO_BODY_CONTROL = Regex("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]")

fun sanitizeMailtoSubject(input: String, maxLength: Int = 180): String {
    var s = input.replace(Regex("[\r\n]+"), " ").trim()
    if (s.length > maxLength) s = s.substring(0, maxLength)
    return s
}

fun sanitizeMailtoBody(input: String, maxLength: Int = 4000): String {
    var s = MAILTO_BODY_CONTROL.replace(input, "")
    if (s.length > maxLength) s = s.substring(0, maxLength)
    return s
}

fun isAllowedFeedbackMailto(uri: Uri): Boolean {
    if (uri.scheme?.lowercase() != "mailto") return false
    val ssp = uri.schemeSpecificPart ?: return false
    val to = ssp.substringBefore('?').trim().lowercase()
    return to == FeedbackConfig.RECIPIENT_EMAIL.lowercase()
}

fun openFeedbackMailto(context: Context, subject: String, body: String): Boolean {
    val safeSubject = sanitizeMailtoSubject(subject)
    val safeBody = sanitizeMailtoBody(body)
    val uri = Uri.parse(
        "mailto:${FeedbackConfig.RECIPIENT_EMAIL}?subject=${Uri.encode(safeSubject)}&body=${Uri.encode(safeBody)}",
    )
    if (!isAllowedFeedbackMailto(uri)) return false
    val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return try {
        context.startActivity(intent)
        true
    } catch (_: ActivityNotFoundException) {
        false
    }
}
