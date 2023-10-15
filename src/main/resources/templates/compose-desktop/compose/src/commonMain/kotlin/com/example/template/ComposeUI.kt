package com.example.template

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ComposeUI(versionInfo: String) {
    val dark = remember { mutableStateOf(false) }
    MaterialTheme(
        colors = if (dark.value) DarkColors else LightColors,
    ) {
        Scaffold {
            Content(PaddingValues(16.dp), versionInfo, dark)
        }
    }
}

@Composable
private fun Content(
    padding: PaddingValues,
    version: String,
    dark: MutableState<Boolean>,
) {
    val scrollState = rememberScrollState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().padding(padding),
    ) {
        Column(modifier = Modifier.verticalScroll(scrollState).fillMaxWidth().align(Alignment.TopStart)) {
            Text(
                style = MaterialTheme.typography.h3,
                text = "Template Project version $version"
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Use this switch to toggle dark mode:")
                Switch(dark.value, { dark.value = !dark.value })
            }
            val numLines = remember { mutableStateOf(32) }
            Text(
                "Use the buttons below to add or remove lines of text." +
                    " Currently, there are ${numLines.value} lines of text."
            )
            Row {
                OutlinedButton({ numLines.value += 1 }) {
                    Text("Increase")
                }
                OutlinedButton({ if (numLines.value > 0) numLines.value -= 1 }) {
                    Text("Decrease")
                }
            }
            for (i in 1..numLines.value) {
                Text("A line of text $i")
            }
        }
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
        )
    }
}
