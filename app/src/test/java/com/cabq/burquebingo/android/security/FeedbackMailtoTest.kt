package com.cabq.burquebingo.android.security

import android.net.Uri
import com.cabq.burquebingo.android.config.FeedbackConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], manifest = Config.NONE)
class FeedbackMailtoTest {

    @Test
    fun sanitizeMailtoSubjectStripsNewlinesAndTruncates() {
        assertEquals("a b c", sanitizeMailtoSubject("a\nb\r\nc"))
        assertEquals(180, sanitizeMailtoSubject("x".repeat(200)).length)
    }

    @Test
    fun sanitizeMailtoBodyStripsControlChars() {
        assertEquals("ok\nline", sanitizeMailtoBody("ok\nline"))
        assertEquals("ab", sanitizeMailtoBody("a\u0000b"))
    }

    @Test
    fun sanitizeMailtoBodyTruncatesAtMaxLength() {
        val long = "y".repeat(5000)
        assertEquals(4000, sanitizeMailtoBody(long).length)
    }

    @Test
    fun isAllowedFeedbackMailtoOnlyAllowsConfiguredRecipient() {
        val good = Uri.parse("mailto:${FeedbackConfig.RECIPIENT_EMAIL}?subject=s")
        assertTrue(isAllowedFeedbackMailto(good))
        assertFalse(isAllowedFeedbackMailto(Uri.parse("mailto:other@evil.com")))
        assertFalse(isAllowedFeedbackMailto(Uri.parse("https://cabq.gov/")))
    }
}
