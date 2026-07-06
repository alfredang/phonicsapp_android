package com.tertiaryinfotech.phonics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tertiaryinfotech.phonics.speech.Accent
import com.tertiaryinfotech.phonics.speech.Speaker

/** Standard white card surface — mirrors the iOS appCard modifier. */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val colors = CardDefaults.cardColors(containerColor = Brand.card)
    val elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    val shape = RoundedCornerShape(18.dp)
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier.fillMaxWidth(), shape = shape,
            colors = colors, elevation = elevation) { content() }
    } else {
        Card(modifier = modifier.fillMaxWidth(), shape = shape,
            colors = colors, elevation = elevation) { content() }
    }
}

/** A circular play button that demonstrates a sound / line via the speech engine. */
@Composable
fun SpeakButton(isActive: Boolean, tint: Color = Brand.primary, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(tint),
    ) {
        Icon(
            imageVector = if (isActive) Icons.Filled.VolumeUp else Icons.Filled.PlayArrow,
            contentDescription = if (isActive) "Stop" else "Play sound",
            tint = Color.White,
        )
    }
}

/** A small pill label. */
@Composable
fun Pill(text: String, color: Color = Brand.chip, textColor: Color = Brand.mutedInk) {
    Box(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

/** Section header with an icon. */
@Composable
fun SectionHeader(title: String, icon: ImageVector, tint: Color = Brand.primary) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Text(
            title,
            color = Brand.ink,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

/** A large tappable category / navigation tile — mirrors the iOS NavTile. */
@Composable
fun NavTile(title: String, subtitle: String, icon: ImageVector, tint: Color, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(tint),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = Color.White)
            }
            Column(
                Modifier.weight(1f).padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(title, color = Brand.ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Brand.mutedInk, fontSize = 14.sp, lineHeight = 18.sp)
            }
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Brand.mutedInk.copy(alpha = 0.6f),
            )
        }
    }
}

/** Large screen title + intro copy used at the top of each tab. */
@Composable
fun ScreenHeader(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, color = Brand.ink, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = Brand.mutedInk, fontSize = 14.sp, lineHeight = 19.sp)
    }
}

/** A compact accent toggle used in top app bars across the app — mirrors iOS AccentMenu. */
@Composable
fun AccentMenu(speaker: Speaker) {
    var open by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { open = true }) {
            Text(
                "${speaker.accent.flag} ${if (speaker.accent == Accent.BRITISH) "UK" else "US"}",
                color = Brand.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            Accent.entries.forEach { accent ->
                DropdownMenuItem(
                    text = { Text("${accent.flag}  ${accent.label}") },
                    onClick = {
                        speaker.chooseAccent(accent)
                        open = false
                    },
                    trailingIcon = {
                        if (speaker.accent == accent) {
                            Text("✓", color = MaterialTheme.colorScheme.primary)
                        }
                    },
                )
            }
        }
    }
}
