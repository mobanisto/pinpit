package com.example.template

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import de.topobyte.shared.preferences.SharedPreferences
import mu.KotlinLogging

private val LOG = KotlinLogging.logger {}

fun main() {
    val density = if (SharedPreferences.isUIScalePresent())
        SharedPreferences.getUIScale().toFloat() else null
    val version = Version.getVersion()
    LOG.info { "Template Project version $version" }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Template Project",
            icon = painterResource("icon.png")
        ) {
            window.minimumSize = DensityDimension(800, 600, density)
            window.preferredSize = DensityDimension(800, 600, density)
            val providers = mutableListOf<ProvidedValue<out Any>>()
            if (density != null) {
                providers.add(LocalDensity provides Density(density))
            }
            CompositionLocalProvider(*providers.toTypedArray()) {
                ComposeUI(version)
            }
        }
    }
}
