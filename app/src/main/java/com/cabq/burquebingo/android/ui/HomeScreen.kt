package com.cabq.burquebingo.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cabq.burquebingo.android.data.BingoCardTheme
import com.cabq.burquebingo.android.theme.CabqColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    themes: List<BingoCardTheme>,
    markedByTheme: Map<String, Set<String>>,
    onOpenCard: (BingoCardTheme) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Burque Bingo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CabqColors.Primary,
                    titleContentColor = Color.White,
                ),
            )
        },
        containerColor = CabqColors.Sand,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        listOf(CabqColors.SkyWash.copy(alpha = 0.35f), CabqColors.Sand),
                    ),
                )
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { BrandingHeader() }
            item {
                Text(
                    text = "Pick a card, get outside (or scroll your camera roll), and tap squares as you spot them. " +
                        "Learn more opens verified cabq.gov pages in your browser only.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CabqColors.Ink.copy(alpha = 0.88f),
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.45f,
                )
            }
            item { ScrollForCardsHint() }
            items(themes, key = { it.id }) { theme ->
                val marked = markedByTheme[theme.id].orEmpty()
                val done = theme.squares.count { marked.contains(it.id) }
                CardTile(
                    theme = theme,
                    done = done,
                    onClick = { onOpenCard(theme) },
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
            item { SecurityNoteCard() }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun BrandingHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(CabqColors.Primary, CabqColors.PrimaryDeep),
                ),
            )
            .padding(22.dp),
    ) {
        Text(
            text = "City of Albuquerque",
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(alpha = 0.92f),
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Burque Bingo",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            CabqColors.Secondary.copy(alpha = 0.95f),
                            CabqColors.SecondarySoft.copy(alpha = 0.5f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )
    }
}

@Composable
private fun ScrollForCardsHint() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardDoubleArrowDown,
            contentDescription = null,
            tint = CabqColors.Primary.copy(alpha = 0.45f),
            modifier = Modifier.size(28.dp),
        )
        Text(
            text = "Scroll for bingo cards",
            style = MaterialTheme.typography.labelSmall,
            color = CabqColors.InkMuted,
        )
    }
}

@Composable
private fun CardTile(
    theme: BingoCardTheme,
    done: Int,
    onClick: () -> Unit,
) {
    val total = theme.squares.size
    val progress = if (total == 0) 0f else done.toFloat() / total
    val accent = accentBrush(theme.id)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accent),
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(CabqColors.Primary.copy(alpha = 0.08f))
                        .padding(10.dp),
                ) {
                    Icon(
                        imageVector = iconForTheme(theme.id),
                        contentDescription = null,
                        tint = CabqColors.Primary,
                        modifier = Modifier.size(26.dp),
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = theme.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = theme.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = CabqColors.InkMuted,
                    )
                    Spacer(Modifier.height(14.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = CabqColors.Secondary,
                        trackColor = CabqColors.SandDeep,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$done of $total found",
                        style = MaterialTheme.typography.labelSmall,
                        color = CabqColors.InkMuted,
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = CabqColors.InkMuted.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

private fun accentBrush(themeId: String): Brush {
    val colors = when (themeId) {
        "public_art" -> listOf(CabqColors.Secondary, CabqColors.SecondarySoft)
        "parks_open" -> listOf(CabqColors.ParksAccent, CabqColors.Primary)
        "burque_flavor" -> listOf(CabqColors.FlavorAccent, CabqColors.Secondary)
        else -> listOf(CabqColors.Primary, CabqColors.Secondary)
    }
    return Brush.verticalGradient(colors)
}

private fun iconForTheme(id: String): ImageVector = when (id) {
    "public_art" -> Icons.Filled.Palette
    "parks_open" -> Icons.Filled.Park
    "burque_flavor" -> Icons.Filled.Restaurant
    else -> Icons.Filled.Palette
}

@Composable
private fun SecurityNoteCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.72f))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Card “Learn more” links use HTTPS on cabq.gov.",
            style = MaterialTheme.typography.bodySmall,
            color = CabqColors.InkMuted,
            lineHeight = MaterialTheme.typography.bodySmall.lineHeight * 1.4f,
        )
    }
}
