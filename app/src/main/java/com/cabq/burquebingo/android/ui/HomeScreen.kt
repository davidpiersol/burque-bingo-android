package com.cabq.burquebingo.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabq.burquebingo.android.config.FeedbackConfig
import com.cabq.burquebingo.android.data.BingoCardTheme
import com.cabq.burquebingo.android.security.openTrustedCityWebUrl
import com.cabq.burquebingo.android.theme.CabqColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val URL_ONE_ALBUQUERQUE = "https://www.cabq.gov/one-albuquerque"
private const val URL_ABQ_TODO = "https://abqtodo.com"
private const val URL_CABQ_HOME = "https://www.cabq.gov/"

private data class SocialLink(val shortLabel: String, val url: String, val contentDescription: String)

private val SOCIAL_LINKS = listOf(
    SocialLink("X", "https://twitter.com/cabq", "City of Albuquerque on X"),
    SocialLink("IG", "https://www.instagram.com/oneabq/", "City of Albuquerque on Instagram"),
    SocialLink("In", "https://www.linkedin.com/company/city-of-albuquerque", "City of Albuquerque on LinkedIn"),
    SocialLink("YT", "https://www.youtube.com/channel/UCdpRwD5FA0g3ynJWxGk7BVQ", "City of Albuquerque on YouTube"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    themes: List<BingoCardTheme>,
    markedByTheme: Map<String, Set<String>>,
    onOpenCard: (BingoCardTheme) -> Unit,
    onPullRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showFeedbackSheet by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()
    val onRefreshBlock: () -> Unit = {
        scope.launch {
            isRefreshing = true
            onPullRefresh()
            delay(350)
            isRefreshing = false
        }
    }

    if (showFeedbackSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFeedbackSheet = false },
            containerColor = CabqColors.Sand,
        ) {
            FeedbackBottomSheetContent(
                onDismiss = { showFeedbackSheet = false },
                onMailResult = { ok ->
                    if (!ok) {
                        scope.launch {
                            snackbarHostState.showSnackbar(FeedbackConfig.COULD_NOT_OPEN_MAIL_SNACK)
                        }
                    }
                },
                on311Result = { ok ->
                    if (!ok) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Could not open link.")
                        }
                    }
                },
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Burque Bingo") },
                actions = {
                    IconButton(
                        onClick = { showFeedbackSheet = true },
                    ) {
                        Icon(Icons.Filled.Feedback, contentDescription = "Beta feedback")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CabqColors.Primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                ),
            )
        },
        containerColor = CabqColors.Sand,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            DesertBackdrop(Modifier.fillMaxSize()) {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = onRefreshBlock,
                    state = pullRefreshState,
                    modifier = Modifier.fillMaxSize(),
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = isRefreshing,
                            state = pullRefreshState,
                            containerColor = CabqColors.Sand,
                            color = CabqColors.Primary,
                        )
                    },
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullToRefresh(
                                isRefreshing = isRefreshing,
                                state = pullRefreshState,
                                onRefresh = onRefreshBlock,
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
                    item { Spacer(Modifier.height(14.dp)) }
                    item {
                        CityFooter(
                            onOpenCabq = {
                                if (!openTrustedCityWebUrl(context, URL_CABQ_HOME)) {
                                    scope.launch { snackbarHostState.showSnackbar("Could not open link.") }
                                }
                            },
                            onOpenSocial = { url ->
                                if (!openTrustedCityWebUrl(context, url)) {
                                    scope.launch { snackbarHostState.showSnackbar("Could not open link.") }
                                }
                            },
                        )
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandingHeader() {
    val context = LocalContext.current
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
        Spacer(Modifier.height(14.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            TextButton(
                onClick = { openTrustedCityWebUrl(context, URL_ONE_ALBUQUERQUE) },
            ) {
                Text(
                    "One Albuquerque",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.96f),
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                )
            }
            Text(
                "·",
                color = Color.White.copy(alpha = 0.65f),
                style = MaterialTheme.typography.bodySmall,
            )
            TextButton(
                onClick = { openTrustedCityWebUrl(context, URL_ABQ_TODO) },
            ) {
                Text(
                    "Explore",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.96f),
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                )
            }
        }
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

@Composable
private fun SecurityNoteCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.72f))
            .border(1.dp, CabqColors.Primary.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            Icons.Outlined.VerifiedUser,
            contentDescription = null,
            tint = CabqColors.Primary.copy(alpha = 0.85f),
            modifier = Modifier.size(22.dp),
        )
        Text(
            text = "Card “Learn more” links use HTTPS on cabq.gov. Footer links use City-curated sites (initiatives, ABQToDo, official social).",
            style = MaterialTheme.typography.bodySmall,
            color = CabqColors.InkMuted,
            lineHeight = MaterialTheme.typography.bodySmall.lineHeight * 1.4f,
        )
    }
}

@Composable
private fun CityFooter(
    onOpenCabq: () -> Unit,
    onOpenSocial: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.82f))
            .border(1.dp, CabqColors.Primary.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        TextButton(
            onClick = onOpenCabq,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    Icons.Filled.AccountBalance,
                    contentDescription = null,
                    tint = CabqColors.Primary,
                    modifier = Modifier.size(38.dp),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "City of Albuquerque — cabq.gov",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CabqColors.Primary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 6.dp, bottom = 8.dp),
            color = CabqColors.InkMuted.copy(alpha = 0.12f),
        )
        Text(
            text = "Visit us on social media",
            style = MaterialTheme.typography.labelSmall,
            color = CabqColors.InkMuted,
            letterSpacing = 0.35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center,
        ) {
            SOCIAL_LINKS.forEach { link ->
                TextButton(
                    onClick = { onOpenSocial(link.url) },
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .semantics { contentDescription = link.contentDescription },
                ) {
                    Text(
                        link.shortLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = CabqColors.Primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = "© 2026 The City of Albuquerque. All Rights Reserved.",
            style = MaterialTheme.typography.labelSmall,
            color = CabqColors.InkMuted.copy(alpha = 0.88f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
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
