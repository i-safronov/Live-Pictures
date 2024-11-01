package com.safronov.livepictures.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = Colors.Purple80,
    secondary = Colors.PurpleGrey80,
    tertiary = Colors.Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Colors.Purple40,
    secondary = Colors.PurpleGrey40,
    tertiary = Colors.Pink40
)

@Composable
fun LivePicturesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun SetStatusBarColor(statusBar: Color, navigationBar: Color) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(statusBar)
        systemUiController.setNavigationBarColor(navigationBar)
    }
}

@Stable
data class ColorValue(
    val enabled: Boolean = true,
    val enableColor: Color = Colors.White,
    val disableColor: Color = Colors.LightGray,
    val isActive: Boolean = false,
    val activeColor: Color = Colors.Active
) {
    fun colorByState() = if (enabled) { if (isActive) activeColor else enableColor } else disableColor
}