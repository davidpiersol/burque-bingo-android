package com.cabq.burquebingo.android

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeToPlayNavigationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun openFirstCard_thenBack_returnsToHome() {
        composeRule.onNodeWithText("Burque Bingo").assertExists()

        composeRule.onNodeWithText("Murals & Public Art")
            .performScrollTo()
            .performClick()

        composeRule.onNodeWithText("Murals & Public Art").assertExists()
        composeRule.onNodeWithText("Checklist").assertExists()

        composeRule.onNodeWithContentDescription("Back").performClick()

        composeRule.onNodeWithText("Burque Bingo").assertExists()
    }
}
