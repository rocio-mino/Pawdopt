package com.example.pawdopt.ui.theme

import OrangePrimary
import OrangeSecondary
import OrangeTertiary
import PawBackground
import PawOnBackground
import PawOnPrimary
import PawOnSecondary
import PawOnSurface
import PawOnTertiary
import PawSurface
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

// Tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = OrangeTertiary,
    secondary = OrangePrimary,
    tertiary = OrangeTertiary,
    background = androidx.compose.ui.graphics.Color(0xFF121212),
    surface = androidx.compose.ui.graphics.Color(0xFF1E1E1E),
    onPrimary = androidx.compose.ui.graphics.Color.Black,
    onSecondary = androidx.compose.ui.graphics.Color.Black,
    onTertiary = androidx.compose.ui.graphics.Color.Black,
    onBackground = PawSurface,
    onSurface = PawSurface
)

//  Tema claro
private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    secondary = OrangeSecondary,
    tertiary = OrangeTertiary,
    background = PawBackground,
    surface = PawSurface,
    onPrimary = PawOnPrimary,
    onSecondary = PawOnSecondary,
    onTertiary = PawOnTertiary,
    onBackground = PawOnBackground,
    onSurface = PawOnSurface
)

@Composable
fun PawdoptTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Soporte para Androids de 12+ (dynamic colors del sistema)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        //  Tema oscuro manual
        darkTheme -> DarkColorScheme

        //  Tema claro con tus nuevos colores
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        window.navigationBarColor = colorScheme.primary.toArgb()
        window.statusBarColor = colorScheme.primary.toArgb()
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content

    )
}