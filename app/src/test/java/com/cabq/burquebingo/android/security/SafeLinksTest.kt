package com.cabq.burquebingo.android.security

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], manifest = Config.NONE)
class SafeLinksTest {

    @Test
    fun allowlistsCabqGovHttpsWithSafeHostRules() {
        assertTrue(isAllowedCabqUrl(Uri.parse("https://www.cabq.gov/")))
        assertTrue(isAllowedCabqUrl(Uri.parse("https://cabq.gov/path")))
        assertTrue(isAllowedCabqUrl(Uri.parse("https://www.cabq.gov:443/foo")))
        assertFalse(isAllowedCabqUrl(Uri.parse("http://www.cabq.gov/")))
        assertFalse(isAllowedCabqUrl(Uri.parse("https://evil.com/")))
        assertFalse(isAllowedCabqUrl(Uri.parse("https://www.cabq.gov.evil.com/")))
    }

    @Test
    fun trustedCityWebIncludesCabqAbqTodoAndSocialHosts() {
        assertTrue(isTrustedCityWebUrl(Uri.parse("https://www.cabq.gov/one-albuquerque")))
        assertTrue(isTrustedCityWebUrl(Uri.parse("https://abqtodo.com/")))
        assertTrue(isTrustedCityWebUrl(Uri.parse("https://www.abqtodo.com/")))
        assertTrue(isTrustedCityWebUrl(Uri.parse("https://twitter.com/cabq")))
        assertTrue(isTrustedCityWebUrl(Uri.parse("https://www.instagram.com/oneabq/")))
        assertTrue(
            isTrustedCityWebUrl(
                Uri.parse("https://www.linkedin.com/company/city-of-albuquerque"),
            ),
        )
        assertTrue(
            isTrustedCityWebUrl(
                Uri.parse("https://www.youtube.com/channel/UCdpRwD5FA0g3ynJWxGk7BVQ"),
            ),
        )
        assertFalse(isTrustedCityWebUrl(Uri.parse("https://evil.com/")))
    }

    @Test
    fun rejectsUserinfoAndNonDefaultPorts() {
        assertFalse(isAllowedCabqUrl(Uri.parse("https://user@www.cabq.gov/")))
        assertFalse(isAllowedCabqUrl(Uri.parse("https://user:pass@cabq.gov/")))
        assertFalse(isAllowedCabqUrl(Uri.parse("https://www.cabq.gov:8443/")))
        assertFalse(isAllowedCabqUrl(Uri.parse("https://www.cabq.gov:8080/path")))
        assertFalse(isTrustedCityWebUrl(Uri.parse("https://user@twitter.com/cabq")))
    }

    @Test
    fun safeLaunchUriStripsUserinfoFromEquivalentAllowedUri() {
        val raw = Uri.parse("https://www.cabq.gov/art/culture?q=1#section")
        assertTrue(isAllowedCabqUrl(raw))
        val safe = safeLaunchUri(raw)
        assertEquals("https", safe.scheme)
        assertEquals("www.cabq.gov", safe.host)
        assertTrue(safe.userInfo.isNullOrEmpty())
        assertEquals("/art/culture", safe.path)
        assertEquals("1", safe.getQueryParameter("q"))
        assertEquals("section", safe.fragment)
    }

    @Test
    fun safeLaunchUriUsesRootPathWhenEmpty() {
        val raw = Uri.parse("https://cabq.gov")
        assertTrue(isAllowedCabqUrl(raw))
        assertEquals("/", safeLaunchUri(raw).path)
    }
}
