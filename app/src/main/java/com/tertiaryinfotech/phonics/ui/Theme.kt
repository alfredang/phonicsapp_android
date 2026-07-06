package com.tertiaryinfotech.phonics.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tertiaryinfotech.phonics.data.PhonicsCategory

/**
 * Brand theme tokens — mirrors the iOS Theme 1:1. The iOS app uses a fixed light
 * palette, so the Android app pins the same light scheme in both system modes.
 */
object Brand {
    /** Primary accent — key buttons, active states, tab tint. (indigo) */
    val primary = Color(0xFF4F46E5)
    /** Secondary accent — links, selected accents. (teal) */
    val secondary = Color(0xFF0D9488)
    /** Highlight — badges, ratings, emphasis. (amber) */
    val highlight = Color(0xFFF59E0B)
    /** Accent for emotion/intonation. (coral) */
    val coral = Color(0xFFEC4899)

    /** Warm off-white app background. */
    val background = Color(0xFFF7F7FB)
    /** Card surface. */
    val card = Color(0xFFFFFFFF)
    /** Subtle fills, chips. */
    val chip = Color(0xFFEEEDFA)

    /** Primary text (explicit, AA-safe on bg). */
    val ink = Color(0xFF1B1A2E)
    /** Secondary text. */
    val mutedInk = Color(0xFF6B6A7B)

    /** Per-category accent used across Learn cards and phoneme detail. */
    fun accent(category: PhonicsCategory): Color = when (category) {
        PhonicsCategory.CONSONANT -> Color(0xFF4F46E5)
        PhonicsCategory.SHORT_VOWEL -> Color(0xFF0D9488)
        PhonicsCategory.LONG_VOWEL -> Color(0xFF0EA5E9)
        PhonicsCategory.DIGRAPH -> Color(0xFFF59E0B)
        PhonicsCategory.BLEND -> Color(0xFFEC4899)
        PhonicsCategory.VOWEL_TEAM -> Color(0xFF8B5CF6)
        PhonicsCategory.DIPHTHONG -> Color(0xFFEF4444)
        PhonicsCategory.R_CONTROLLED -> Color(0xFF059669)
        PhonicsCategory.SOFT_SOUND -> Color(0xFFD97706)
    }
}

private val LightColors = lightColorScheme(
    primary = Brand.primary,
    onPrimary = Color.White,
    primaryContainer = Brand.chip,
    onPrimaryContainer = Color(0xFF29257A),
    secondary = Brand.secondary,
    onSecondary = Color.White,
    background = Brand.background,
    onBackground = Brand.ink,
    surface = Brand.background,
    onSurface = Brand.ink,
    surfaceVariant = Brand.card,
    onSurfaceVariant = Brand.mutedInk,
    surfaceContainer = Brand.card,
    surfaceContainerHigh = Brand.chip,
)

@Composable
fun PhonicsTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
