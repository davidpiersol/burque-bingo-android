package com.gothamconsulting.burquebingo.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BurqueBingoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        HomePlaceholder(
                            title = stringResource(R.string.app_name),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomePlaceholder(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier.padding(24.dp),
        style = MaterialTheme.typography.headlineMedium,
    )
}

@Composable
private fun BurqueBingoTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@Preview(showBackground = true)
@Composable
private fun HomePlaceholderPreview() {
    BurqueBingoTheme {
        HomePlaceholder(title = "Burque Bingo")
    }
}
