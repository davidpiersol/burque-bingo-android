package com.cabq.burquebingo.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.cabq.burquebingo.android.data.BingoCardTheme
import com.cabq.burquebingo.android.data.BingoProgressStore
import com.cabq.burquebingo.android.data.defaultCardThemes
import com.cabq.burquebingo.android.theme.CabqColors
import com.cabq.burquebingo.android.ui.HomeScreen
import com.cabq.burquebingo.android.ui.PlayScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dark = isSystemInDarkTheme()
            val context = LocalContext.current
            MaterialTheme(colorScheme = if (dark) cabqDarkColorScheme() else cabqLightColorScheme()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var openThemeId by rememberSaveable { mutableStateOf<String?>(null) }
                    val themes = remember { defaultCardThemes() }
                    val store = remember { BingoProgressStore(context.applicationContext) }
                    var markedByTheme by remember { mutableStateOf<Map<String, Set<String>>>(emptyMap()) }

                    fun refreshMarks() {
                        markedByTheme = themes.associate { t -> t.id to store.loadMarked(t.id) }
                    }

                    LaunchedEffect(openThemeId) {
                        refreshMarks()
                    }

                    val openTheme = openThemeId?.let { id -> themes.find { it.id == id } }
                    if (openTheme != null) {
                        PlayScreen(
                            theme = openTheme,
                            progressStore = store,
                            onBack = { openThemeId = null },
                            onMarksChanged = { refreshMarks() },
                        )
                    } else {
                        HomeScreen(
                            themes = themes,
                            markedByTheme = markedByTheme,
                            onOpenCard = { theme: BingoCardTheme -> openThemeId = theme.id },
                        )
                    }
                }
            }
        }
    }
}

private fun cabqLightColorScheme() = lightColorScheme(
    primary = CabqColors.Primary,
    onPrimary = Color.White,
    secondary = CabqColors.Secondary,
    surface = CabqColors.Sand,
    onSurface = CabqColors.Ink,
    surfaceContainerHighest = CabqColors.SandDeep,
    outline = CabqColors.InkMuted.copy(alpha = 0.35f),
)

private fun cabqDarkColorScheme() = darkColorScheme(
    primary = CabqColors.SecondarySoft,
    onPrimary = CabqColors.Ink,
    secondary = CabqColors.Secondary,
    surface = CabqColors.PrimaryDeep,
    onSurface = Color.White,
)
