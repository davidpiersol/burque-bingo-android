package com.cabq.burquebingo.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabq.burquebingo.android.data.BingoCardTheme
import com.cabq.burquebingo.android.data.BingoProgressStore
import com.cabq.burquebingo.android.data.BingoSquare
import com.cabq.burquebingo.android.security.openCabqLearnMore
import com.cabq.burquebingo.android.theme.CabqColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayScreen(
    theme: BingoCardTheme,
    progressStore: BingoProgressStore,
    onBack: () -> Unit,
    onMarksChanged: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var marked by remember(theme.id) { mutableStateOf<Set<String>>(emptySet()) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showCelebration by remember { mutableStateOf(false) }

    LaunchedEffect(theme.id) {
        marked = progressStore.loadMarked(theme.id)
    }

    fun persistAndNotify(ids: Set<String>) {
        progressStore.saveMarked(theme.id, ids)
        onMarksChanged()
    }

    fun toggle(id: String) {
        val total = theme.squares.size
        val wasComplete = total > 0 && marked.size == total
        val next = marked.toMutableSet()
        if (!next.add(id)) next.remove(id)
        val isNowComplete = total > 0 && next.size == total
        marked = next
        persistAndNotify(next)
        if (!wasComplete && isNowComplete) showCelebration = true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(theme.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showResetDialog = true },
                    ) {
                        Icon(Icons.Filled.RestartAlt, contentDescription = "Reset card")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CabqColors.Primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,
                ),
            )
        },
        containerColor = CabqColors.Sand,
    ) { innerPadding ->
        val total = theme.squares.size
        val done = theme.squares.count { marked.contains(it.id) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            DesertBackdrop(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 32.dp),
                ) {
                    item {
                        ProgressHeader(done = done, total = total)
                    }
                    item {
                        BingoStrip(
                            squares = theme.squares,
                            marked = marked,
                            onToggle = { toggle(it) },
                        )
                    }
                    item {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Checklist",
                            style = MaterialTheme.typography.titleSmall,
                            color = CabqColors.InkMuted,
                            letterSpacing = 0.8.sp,
                        )
                    }
                    itemsIndexed(theme.squares, key = { _, s -> s.id }) { index, square ->
                        SquareChecklistTile(
                            index = index + 1,
                            square = square,
                            isMarked = marked.contains(square.id),
                            onToggle = { toggle(square.id) },
                            onLearnMore = {
                                val ok = openCabqLearnMore(context, square.learnMoreUrl)
                                if (!ok) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Link blocked or unavailable. Only cabq.gov HTTPS links open here.",
                                        )
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset this card?") },
            text = {
                Text(
                    "Clears every mark on this card on this device. " +
                        "Official links are still opened only on cabq.gov.",
                )
            },
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        showResetDialog = false
                        marked = emptySet()
                        persistAndNotify(emptySet())
                    },
                ) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            },
        )
    }

    if (showCelebration) {
        AlertDialog(
            onDismissRequest = { showCelebration = false },
            icon = {
                Icon(
                    Icons.Filled.Celebration,
                    contentDescription = null,
                    modifier = Modifier.size(52.dp),
                    tint = CabqColors.Secondary,
                )
            },
            title = { Text("Burque Bingo!") },
            text = {
                Text(
                    "You filled \"${theme.title}\". " +
                        "Thanks for getting out there and noticing Albuquerque.",
                )
            },
            confirmButton = {
                FilledTonalButton(onClick = { showCelebration = false }) {
                    Text("Keep exploring")
                }
            },
        )
    }
}

@Composable
private fun ProgressHeader(done: Int, total: Int) {
    val value = if (total == 0) 0f else done.toFloat() / total
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.88f))
            .border(1.dp, CabqColors.Primary.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .padding(18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$done of $total squares",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            if (done == total && total > 0) {
                Text(
                    text = "Complete",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = CabqColors.Secondary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(CabqColors.Secondary.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { value },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(10.dp)),
            color = CabqColors.Secondary,
            trackColor = CabqColors.SandDeep,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Tap a square or the strip above when you spot it. Learn more uses your browser.",
            style = MaterialTheme.typography.bodySmall,
            color = CabqColors.InkMuted,
        )
    }
}

@Composable
private fun BingoStrip(
    squares: List<BingoSquare>,
    marked: Set<String>,
    onToggle: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        for (square in squares) {
            val isOn = marked.contains(square.id)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isOn) CabqColors.Secondary.copy(alpha = 0.18f)
                        else Color.White.copy(alpha = 0.85f),
                    )
                    .border(
                        width = if (isOn) 2.dp else 1.dp,
                        color = if (isOn) CabqColors.Secondary.copy(alpha = 0.45f)
                        else CabqColors.Primary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(16.dp),
                    )
                    .clickable { onToggle(square.id) },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (isOn) Icons.Filled.Check else Icons.Filled.Add,
                    contentDescription = null,
                    tint = if (isOn) CabqColors.Secondary
                    else CabqColors.Primary.copy(alpha = 0.4f),
                    modifier = Modifier.size(30.dp),
                )
            }
        }
    }
}

@Composable
private fun SquareChecklistTile(
    index: Int,
    square: BingoSquare,
    isMarked: Boolean,
    onToggle: () -> Unit,
    onLearnMore: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isMarked) CabqColors.Secondary.copy(alpha = 0.18f)
                            else CabqColors.Primary.copy(alpha = 0.08f),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isMarked) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = CabqColors.Secondary,
                            modifier = Modifier.size(22.dp),
                        )
                    } else {
                        Text(
                            text = "$index",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = CabqColors.Primary,
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = square.label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isMarked) CabqColors.Ink.copy(alpha = 0.5f) else CabqColors.Ink,
                        textDecoration = if (isMarked) TextDecoration.LineThrough else null,
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = square.hint,
                        style = MaterialTheme.typography.bodySmall,
                        color = CabqColors.InkMuted,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            TextButton(
                onClick = onLearnMore,
                modifier = Modifier.align(Alignment.End),
            ) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Learn more (cabq.gov)")
            }
        }
    }
}
