package com.cabq.burquebingo.android.security

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

private val cabqHosts = setOf("www.cabq.gov", "cabq.gov")

private val trustedWebHosts = cabqHosts + setOf(
    "abqtodo.com",
    "www.abqtodo.com",
    "twitter.com",
    "www.twitter.com",
    "x.com",
    "instagram.com",
    "www.instagram.com",
    "linkedin.com",
    "www.linkedin.com",
    "youtube.com",
    "www.youtube.com",
    "m.youtube.com",
)

private fun baseUrlRules(uri: Uri): Boolean {
    if (uri.scheme?.lowercase() != "https") return false
    if (uri.host.isNullOrEmpty()) return false
    if (uri.userInfo != null && uri.userInfo!!.isNotEmpty()) return false
    val port = uri.port
    if (port != -1 && port != 443) return false
    return true
}

fun isAllowedCabqUrl(uri: Uri): Boolean {
    if (!baseUrlRules(uri)) return false
    val host = uri.host?.lowercase() ?: return false
    return cabqHosts.contains(host)
}

fun isTrustedCityWebUrl(uri: Uri): Boolean {
    if (!baseUrlRules(uri)) return false
    val host = uri.host?.lowercase() ?: return false
    return trustedWebHosts.contains(host)
}

fun safeLaunchUri(validated: Uri): Uri {
    val host = validated.host ?: return validated
    val path = validated.path?.takeIf { it.isNotEmpty() } ?: "/"
    return Uri.Builder()
        .scheme("https")
        .authority(host)
        .path(path)
        .apply {
            validated.encodedQuery?.let { encodedQuery(it) }
            validated.encodedFragment?.let { encodedFragment(it) }
        }
        .build()
}

/** Opens [url] in the default browser after cabq.gov validation. */
fun openCabqLearnMore(context: Context, url: String): Boolean {
    val uri = try {
        url.trim().toUri()
    } catch (_: Exception) {
        return false
    }
    if (!isAllowedCabqUrl(uri)) return false
    val launch = safeLaunchUri(uri)
    val intent = Intent(Intent.ACTION_VIEW, launch).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return try {
        context.startActivity(intent)
        true
    } catch (_: ActivityNotFoundException) {
        false
    }
}

/** Footer / initiative links (cabq.gov, abqtodo, social). */
fun openTrustedCityWebUrl(context: Context, url: String): Boolean {
    val uri = try {
        url.trim().toUri()
    } catch (_: Exception) {
        return false
    }
    if (!isTrustedCityWebUrl(uri)) return false
    val launch = safeLaunchUri(uri)
    val intent = Intent(Intent.ACTION_VIEW, launch).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return try {
        context.startActivity(intent)
        true
    } catch (_: ActivityNotFoundException) {
        false
    }
}
