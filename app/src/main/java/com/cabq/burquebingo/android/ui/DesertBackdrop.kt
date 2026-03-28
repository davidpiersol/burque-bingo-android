package com.cabq.burquebingo.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cabq.burquebingo.android.theme.CabqColors

/**
 * Soft high-desert atmosphere behind scrollable content (no network assets).
 * Matches Flutter desert_backdrop.dart.
 */
@Composable
fun DesertBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to CabqColors.SkyWash,
                            0.42f to CabqColors.Sand,
                            1f to CabqColors.DesertWarm,
                        ),
                    ),
                ),
        )
        Box(
            Modifier
                .size(280.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-100).dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        0f to CabqColors.Secondary.copy(alpha = 0.14f),
                        0.35f to CabqColors.SecondarySoft.copy(alpha = 0.04f),
                        1f to Color.Transparent,
                    ),
                ),
        )
        Box(
            Modifier
                .size(200.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-40).dp, y = 60.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            CabqColors.Primary.copy(alpha = 0.08f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )
        Box(Modifier.fillMaxSize()) {
            content()
        }
    }
}
