package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)

private val LightColorScheme =
  lightColorScheme(
    primary = BentoPurpleDark,
    onPrimary = BentoWhite,
    primaryContainer = BentoPrimaryCard,
    onPrimaryContainer = BentoPurpleDark,
    secondary = BentoPurpleMedium,
    secondaryContainer = BentoSecondaryCard,
    onSecondaryContainer = BentoTextMain,
    tertiaryContainer = BentoTertiaryCard,
    background = BentoBackground,
    onBackground = BentoTextMain,
    surface = BentoBackground,
    onSurface = BentoTextMain,
    surfaceVariant = BentoWhite,
    onSurfaceVariant = BentoTextSub,
    outline = BentoBorder,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Force dynamic color off to show Bento theme by default
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
